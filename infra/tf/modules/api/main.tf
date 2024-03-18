data "aws_ssm_parameter" "db_username_param" {
  name = var.db_username
}

data "aws_ssm_parameter" "db_password_param" {
  name = var.db_password
}

resource "aws_iam_role" "lambda_role" {
  name = "lambda_execution_role"

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
  name = "lambda_policy"
  role = aws_iam_role.lambda_role.id

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action = [
          "logs:CreateLogGroup",
          "logs:CreateLogStream",
          "logs:PutLogEvents",
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

resource "aws_s3_bucket_object" "lambda_code" {
  bucket = var.bucket_name
  key    = "function.zip"
  source = "../../ecomarkets/target/function.zip"
}

resource "aws_lambda_function" "that_lambda" {
  function_name    = "that_lambda_function"
  handler          = "io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest"
  role             = aws_iam_role.lambda_role.arn
  runtime          = "java21"
  s3_bucket        = var.bucket_name
  s3_key           = aws_s3_bucket_object.lambda_code.key
  source_code_hash = filebase64sha256("../../ecomarkets/target/function.zip")

  environment {
    variables = {
      QUARKUS_DATASOURCE_JDBC_URL = "jdbc:postgresql://${var.db_endpoint}/${var.db_name}"
      QUARKUS_DATASOURCE_USERNAME = data.aws_ssm_parameter.db_username_param.value
      QUARKUS_DATASOURCE_USERNAME = data.aws_ssm_parameter.db_password_param.value
    }
  }
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
