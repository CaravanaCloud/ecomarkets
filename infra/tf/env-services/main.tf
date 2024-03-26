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
    container_image = "caravanacloud/ecomarkets-app:0.0.1"
    container_port = 3000
}

module "api" {
    source = "../modules/api"
    env_id = var.env_id
    api_subnet_ids = split(",", var.api_subnet_ids)
    db_endpoint = var.db_endpoint
    db_name = var.db_name
    bucket_name = var.infra_bucket_name
    db_username_param = var.db_username_param
    db_password_param = var.db_password_param
    oidc_provider = var.oidc_provider
    oidc_client_id = var.oidc_client_id
    oidc_client_secret = var.oidc_client_secret
}
