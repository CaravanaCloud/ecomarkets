variable aws_region {
  default = "us-east-1"
}

variable env_id {
  default = "ecomarkets_dev"
}

variable db_publicly_accessible {
  type    = bool
  default = false
}

variable domain_name {
  default = "alpha.ecofeiras.com"
}

variable db_app_username {}
variable db_app_password {}
variable db_username {}
variable db_password {}