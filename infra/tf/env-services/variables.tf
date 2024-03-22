variable env_id {
  default = "ecomarkets_dev"
}

variable aws_region {
  default = "us-east-1"
}


variable vpc_id {}

variable ecs_subnets {
  type = string
}

variable api_subnet_ids {
  type = string
}

variable db_endpoint {}
variable db_name {}
variable bucket_name {}
