output "db_app_username" {
  value = aws_ssm_parameter.db_app_username.name
}

output "db_app_password" {
  value = aws_ssm_parameter.db_app_password.name
}

output "twilio_account_sid" {
  value = aws_ssm_parameter.twilio_account_sid.name
}

output "twilio_auth_token" {
  value = aws_ssm_parameter.twilio_auth_token.name
}

output "twilio_phone_from" {
  value = aws_ssm_parameter.twilio_phone_from.name
}

output "oidc_client_id" {
  value = aws_ssm_parameter.oidc_client_id.name
}

output "oidc_client_secret" {
  value = aws_ssm_parameter.oidc_client_secret.name
}