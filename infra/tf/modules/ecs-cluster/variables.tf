variable env_id {}
variable vpc_id {}
variable ecs_subnets {
    description = "The subnet ids for the ECS service"
    type        = list(string)
}
variable "certificate_arn" {}
variable "zone_id" {}
