variable env_id {
  default = "ecomarkets_dev"
}

variable aws_region {
  default = "us-east-1"
}

variable vpc_id {}

variable ecs_subnets {
  type = string
}

variable api_subnet_ids {
  type = string
}

variable db_endpoint {}
variable db_name {}
variable infra_bucket_name {}

variable oidc_client_id {}
variable oidc_client_secret {}
variable oidc_provider {}
variable zone_id {}
variable domain_name {}
variable certificate_arn {}

variable db_app_username {}
variable db_app_password {}

variable "twilio_account_sid" {}
variable "twilio_auth_token" {}
variable "twilio_phone_from" {}

