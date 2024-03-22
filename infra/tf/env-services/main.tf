terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }

  backend "s3" {}
}

provider "aws" {
  region = var.aws_region
}

module "ecs" {
    source = "../modules/ecs"
    aws_region = var.aws_region
    env_id = var.env_id
    vpc_id = var.vpc_id
    ecs_subnets = split(",", var.ecs_subnets)
}

module "api" {
    source = "../modules/api"
    api_subnet_ids = split(",", var.api_subnet_ids)
    db_endpoint = var.db_endpoint
    db_name = var.db_name
    bucket_name = var.bucket_name
#    db_username = module.security.db_username
#    db_password = module.security.db_password
}
