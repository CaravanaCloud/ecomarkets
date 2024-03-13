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