variable aws_region {}

variable env_id {}
variable task_id {}

variable vpc_id {}

variable cluster_id {}

variable ecs_subnets {
    description = "The subnet ids for the ECS service"
    type        = list(string)
}

variable "container_port" {
    type = number
    default = 80
}

variable "container_image" {
    type = string
    default = "nginx"
}

variable "container_cpu" {
    type = number
    default = 1024
}

variable "container_mem" {
    type = number
    default = 2048
}

variable "db_endpoint" {}
variable "db_name" {}
variable "db_app_username" {}
variable "db_app_password" {}

variable "oidc_provider" {} 
variable "oidc_client_id" {}
variable "oidc_client_secret" {}

variable "twilio_account_sid" {}
variable "twilio_auth_token" {}
variable "twilio_phone_from" {}

variable "task_execution_role" {}
variable "path_pattern" {
  
}