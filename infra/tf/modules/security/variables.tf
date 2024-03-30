variable env_id {
    type = string
}

variable db_username_text {
    type = string
    default = "root"
}

variable db_password_text {
    type = string
    default = ""
}

variable db_app_username_text {
    type = string
    default = "app_user"
}

variable db_app_password_text {
    type = string
    default = ""
}

variable docker_username {
    type = string
    default = "caravanacloud"
}

variable docker_password {
    type = string
    default = ""
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