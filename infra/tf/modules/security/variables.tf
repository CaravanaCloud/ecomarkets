variable "env_id" {
  type = string
}

variable "db_username_text" {
  type    = string
  default = "root"
  sensitive = true
}

variable "db_password_text" {
  type    = string
  default = ""
  sensitive = true
}

variable "db_app_username_text" {
  type    = string
  default = "app_user"
  sensitive = true
}

variable "db_app_password_text" {
  type    = string
  default = ""
  sensitive = true
}

variable "docker_username_text" {
  type      = string
  default   = "caravanacloud"
  sensitive = true
}

variable "docker_password_text" {
  type      = string
  default   = ""
  sensitive = true
}

variable "twilio_account_sid_text" {
  type      = string
  sensitive = true
}

variable "twilio_auth_token_text" {
  type      = string
  sensitive = true
}

variable "twilio_phone_from_text" {
  type      = string
  sensitive = true
}

variable "oidc_client_id_text" {
  type      = string
  sensitive = true
}

variable "oidc_client_secret_text" {
  type      = string
  sensitive = true
}

variable "oidc_provider_text" {
  type      = string
  sensitive = true
}
