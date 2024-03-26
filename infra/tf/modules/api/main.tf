data "aws_ssm_parameter" "db_username" {
  name = var.db_username_param
}

data "aws_ssm_parameter" "db_password" {
  name = var.db_password_param
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

resource "aws_s3_object" "lambda_code" {
  bucket      = var.bucket_name
  key         = "${var.env_id}/function.zip"
  source      = local.object_source
  source_hash = filemd5(local.object_source)
}

resource "aws_lambda_function" "that_lambda" {
  function_name    = "that_lambda_function"
  handler          = "io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest"
  role             = aws_iam_role.lambda_role.arn
  runtime          = "java21"
  s3_bucket        = var.bucket_name
  s3_key           = aws_s3_object.lambda_code.key


  environment {
    variables = {
      QUARKUS_DATASOURCE_JDBC_URL = "jdbc:postgresql://${var.db_endpoint}/${var.db_name}"
      QUARKUS_DATASOURCE_USERNAME = data.aws_ssm_parameter.db_username.value
      QUARKUS_DATASOURCE_PASSWORD = data.aws_ssm_parameter.db_password.value
      QUARKUS_OIDC_PROVIDER = var.oidc_provider 
      QUARKUS_OIDC_CLIENT_ID = var.oidc_client_id
      QUARKUS_OIDC_CREDENTIALS_SECRET = var.oidc_client_secret
    }
  }
}


resource "aws_cloudwatch_log_group" "api_log_group" {
  name              = "/aws/lambda/${aws_lambda_function.that_lambda.function_name}"
  retention_in_days = 7 # Set the retention period for the log events in the log group
}

resource "aws_apigatewayv2_api" "that" {
  name          = "apigw"
  protocol_type = "HTTP"
}

resource "aws_apigatewayv2_integration" "lambda_integration" {
  api_id                 = aws_apigatewayv2_api.that.id
  integration_type       = "AWS_PROXY"
  integration_uri        = aws_lambda_function.that_lambda.invoke_arn
  payload_format_version = "2.0"
}

resource "aws_apigatewayv2_route" "that_route" {
  api_id    = aws_apigatewayv2_api.that.id
  route_key = "ANY /{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.lambda_integration.id}"
}

resource "aws_apigatewayv2_stage" "that_stage" {
  api_id      = aws_apigatewayv2_api.that.id
  name        = "default"
  auto_deploy = true
}

resource "aws_lambda_permission" "api_gw_lambda" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.that_lambda.function_name
  principal     = "apigateway.amazonaws.com"
  source_arn    = "${aws_apigatewayv2_api.that.execution_arn}/*/*"
}
