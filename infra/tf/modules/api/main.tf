data "aws_ssm_parameter" "db_app_username" {
  name = var.db_app_username_param
}

data "aws_ssm_parameter" "db_app_password" {
  name = var.db_app_password_param
}

resource "aws_iam_role" "lambda_role" {
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action = "sts:AssumeRole",
        Effect = "Allow",
        Principal = {
          Service = "lambda.amazonaws.com"
        },
      },
    ],
  })
}

resource "aws_iam_role_policy" "lambda_policy" {
  name_prefix = "lambda_policy"
  role = aws_iam_role.lambda_role.id

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action = [
          "ec2:*",
          "logs:*",
          "rds-data:*",
          "rds:*",
          "secretsmanager:GetSecretValue",
        ],
        Resource = "*",
        Effect   = "Allow",
      },
    ],
  })
}

locals {
  object_source = var.code_package_path
}


resource "aws_security_group" "lambda_sg" {
  name_prefix  = "lambda_security_group"
  description = "Allow outbound traffic from Lambda"
  vpc_id      = var.vpc_id

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "LambdaSG"
  }
}


resource "aws_s3_object" "lambda_code" {
  bucket      = var.bucket_name
  key         = "${var.env_id}/function.zip"
  source      = local.object_source
  source_hash = filemd5(local.object_source)
}

resource "aws_lambda_function" "api_lambda" {
  function_name    = "${var.env_id}_api_lambda"
  handler          = "io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest"
  role             = aws_iam_role.lambda_role.arn
  runtime          = "java17"
  s3_bucket        = var.bucket_name
  s3_key           = aws_s3_object.lambda_code.key
  memory_size      = var.memory_size
  timeout          = var.timeout

  vpc_config {
    subnet_ids         = var.api_subnet_ids
    security_group_ids = [aws_security_group.lambda_sg.id]
  }

  environment {
    variables = {
      QUARKUS_DATASOURCE_JDBC_URL = "jdbc:postgresql://${var.db_app_endpoint}/${var.db_app_name}"
      QUARKUS_DATASOURCE_USERNAME = data.aws_ssm_parameter.db_app_username.value
      QUARKUS_DATASOURCE_PASSWORD = data.aws_ssm_parameter.db_app_password.value
      QUARKUS_OIDC_PROVIDER = var.oidc_provider 
      QUARKUS_OIDC_CLIENT_ID = var.oidc_client_id
      QUARKUS_OIDC_CREDENTIALS_SECRET = var.oidc_client_secret
    }
  }


}

resource "aws_lambda_function_url" "function_url" {
  function_name      = aws_lambda_function.api_lambda.function_name
  authorization_type = "NONE"
}


resource "aws_cloudwatch_log_group" "api_log_group" {
  name              = "/aws/lambda/${aws_lambda_function.api_lambda.function_name}"
  retention_in_days = 7 # Set the retention period for the log events in the log group
}

resource "aws_api_gateway_rest_api" "that" {
  name        = "that"
  description = "REST API integrated with Lambda"
}

resource "aws_api_gateway_resource" "that" {
  rest_api_id = aws_api_gateway_rest_api.that.id
  parent_id   = aws_api_gateway_rest_api.that.root_resource_id
  path_part   = "{proxy+}"
}

resource "aws_api_gateway_method" "that" {
  rest_api_id   = aws_api_gateway_rest_api.that.id
  resource_id   = aws_api_gateway_resource.that.id
  http_method   = "ANY"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "that" {
  rest_api_id = aws_api_gateway_rest_api.that.id
  resource_id = aws_api_gateway_resource.that.id
  http_method = aws_api_gateway_method.that.http_method

  type              = "AWS_PROXY"
  integration_http_method = "POST" # AWS Lambda uses POST for proxy integration
  uri               = aws_lambda_function.api_lambda.invoke_arn
}

resource "aws_api_gateway_deployment" "that" {
  depends_on = [
    aws_api_gateway_integration.that,
  ]

  rest_api_id = aws_api_gateway_rest_api.that.id

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_api_gateway_stage" "that" {
  deployment_id = aws_api_gateway_deployment.that.id
  rest_api_id   = aws_api_gateway_rest_api.that.id
  stage_name    = "${var.env_id}_stage"
}

resource "aws_lambda_permission" "that" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.api_lambda.function_name
  principal     = "apigateway.amazonaws.com"
  
  # The source_arn specifies that ANY method on the specified resource can invoke the Lambda
  source_arn    = "${aws_api_gateway_rest_api.that.execution_arn}/*/*/*"
}