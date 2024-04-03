output env_id {
    value = var.env_id
}
output aws_region {
    value = var.aws_region
}

output "db_app_username" {
    value = module.security.db_app_username
}

output "db_app_password" {
    value = module.security.db_app_password
}

output "twilio_account_sid" {
  value = module.security.twilio_account_sid
}

output "twilio_auth_token" {
  value = module.security.twilio_auth_token
}

output "twilio_phone_from" {
  value = module.security.twilio_phone_from
}

output "domain_name" {
    value = var.domain_name
}

