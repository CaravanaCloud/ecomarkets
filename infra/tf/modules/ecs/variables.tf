variable aws_region {}
variable env_id {}
variable vpc_id {}
variable ecs_subnets {
    description = "The subnet ids for the ECS service"
    type        = list(string)
}

variable "container_image" {
    type = string
    default = "nginx"
}

variable "container_cpu" {
    type = number
    default = 256
}

variable "container_mem" {
    type = number
    default = 512
}

variable "container_port" {
    type = number
    default = 80
}