variable env_id {
  default = "ecomarkets_dev"
}

variable aws_region {
  default = "us-east-1"
}

variable "google_client_id" {
  description = "The Client ID from Google"
  type        = string
}

variable "google_client_secret" {
  description = "The Client secrte from Google"
  type        = string
}
