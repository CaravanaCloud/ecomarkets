## secrets do banco
resource "aws_ssm_parameter" "db_username" {
  name  = "/${var.env_id}/db_username"
  type  = "String"
  value = var.db_username != "" ? var.db_username : "root"
}

resource "aws_ssm_parameter" "db_password" {
  name  = "/${var.env_id}/db_password"
  type  = "String"
  value = var.db_password != "" ? var.db_password : uuid()
}

resource "aws_secretsmanager_secret" "docker_credentials" {
  name        = "/${var.env_id}/docker_registry_credentials"
  description = "Credentials for Docker Registry"
}

resource "aws_secretsmanager_secret_version" "docker_credentials" {
  secret_id     = aws_secretsmanager_secret.docker_credentials.id
  secret_string = jsonencode({
    username = var.docker_username
    password = var.docker_password
  })
}
