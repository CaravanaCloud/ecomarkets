variable env_id {}

variable domain_name {}

variable aws_region {
  default = "us-east-1"
}

variable "oidc_google_id_ssm" {
  description = "The Client ID from Google"
  type        = string
}

variable "oidc_google_secret_ssm" {
  description = "The Client secrete from Google"
  type        = string
}

