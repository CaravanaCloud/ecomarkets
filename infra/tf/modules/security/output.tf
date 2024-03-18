output "db_username" {
  value = aws_ssm_parameter.db_username.name
}

output "db_password" {
  value = aws_ssm_parameter.db_password.name
}