variable aws_region {}
variable vpc_id {}
variable env_id {}
variable domain_name {}
variable certificate_arn {}
variable zone_id {}
variable ecs_subnets {}


variable db_publicly_accessible {
  type    = bool
  default = false
}