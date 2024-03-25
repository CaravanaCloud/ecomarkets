
output "identity_pool_id" {
  value = aws_cognito_identity_pool.main.id
}

output "user_pool_id" {
  value = aws_cognito_user_pool.main.id
}

output "app_client_id" {
  value = aws_cognito_user_pool_client.app_client.id
}
