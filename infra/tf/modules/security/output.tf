output "db_username" {
  value = aws_ssm_parameter.db_username.name
}

output "db_password" {
  value = aws_ssm_parameter.db_password.name
}

output "db_app_username" {
  value = aws_ssm_parameter.db_app_username.name
}

output "db_app_password" {
  value = aws_ssm_parameter.db_app_password.name
}


