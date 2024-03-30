variable aws_region {
  default = "us-east-1"
}

variable env_id {
  default = "ecomarkets_dev"
}

variable domain_name {
  default = "omega.ecomarkets.com"
}

variable "twilio_account_sid" {
  type = string
}

variable "twilio_auth_token" {
  type = string
}

variable "twilio_phone_from" {
  type = string
}