variable aws_region {}

variable env_id {}

variable vpc_id {}

variable "certificate_arn" {
    type = string
}

variable ecs_subnets {
    description = "The subnet ids for the ECS service"
    type        = list(string)
}

variable "container_port" {
    type = number
    default = 8080
}

variable "container_api_port" {
    type = number
    default = 8080
}


variable "container_image" {
    type = string
    default = "nginx"
}

variable "container_cpu" {
    type = number
    default = 256
}

variable "container_api_mem" {
    type = number
    default = 1024
}

variable "container_api_image" {
    type = string
    default = "nginx"
}

variable "container_api_cpu" {
    type = number
    default = 256
}

variable "container_mem" {
    type = number
    default = 1024
}


variable "hosted_zone_id" {
  type = string
}


variable "domain_name" {
    type = string
}
