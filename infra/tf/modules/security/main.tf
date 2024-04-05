resource "aws_ssm_parameter" "db_app_password" {
  name  = "/${var.env_id}/db_app_password"
  type  = "String"
  value = var.db_app_password_text != "" ? var.db_app_password_text : uuid()
}

resource "aws_ssm_parameter" "db_app_username" {
  name  = "/${var.env_id}/db_app_username"
  type  = "String"
  value = var.db_app_username_text != "" ? var.db_app_username_text : "app_user"
}

resource "aws_ssm_parameter" "twilio_account_sid" {
  name  = "/${var.env_id}/twilio_account_sid"
  type  = "String"
  value = var.twilio_account_sid_text
}

resource "aws_ssm_parameter" "twilio_auth_token" {
  name  = "/${var.env_id}/twilio_auth_token"
  type  = "String"
  value = var.twilio_auth_token_text
}

resource "aws_ssm_parameter" "twilio_phone_from" {
  name  = "/${var.env_id}/twilio_phone_from"
  type  = "String"
  value = var.twilio_phone_from_text
}

resource "aws_ssm_parameter" "oidc_client_id" {
  name  = "/${var.env_id}/oidc_client_id"
  type  = "String"
  value = var.oidc_client_id_text
}

resource "aws_ssm_parameter" "oidc_client_secret" {
  name  = "/${var.env_id}/oidc_client_secret"
  type  = "String"
  value = var.oidc_client_secret_text
}

resource "aws_ssm_parameter" "oidc_provider" {
  name  = "/${var.env_id}/oidc_provider"
  type  = "String"
  value = var.oidc_provider_text
}

resource "aws_secretsmanager_secret" "docker_credentials" {
  name        = "/${var.env_id}/docker_registry_credentials"
  description = "Credentials for Docker Registry"
}

resource "aws_secretsmanager_secret_version" "docker_credentials" {
  secret_id     = aws_secretsmanager_secret.docker_credentials.id
  secret_string = jsonencode({
    username = var.docker_username_text
    password = var.docker_password_text
  })
}
