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
    certificate_arn = var.certificate_arn

    container_image = "caravanacloud/ecomarkets-vdn:0.0.1"
    container_port = 9091

    container_api_image = "caravanacloud/ecomarkets-api:0.0.1"
    container_api_port = 9090

    db_endpoint = var.db_endpoint
    db_name = var.db_name
    db_username_param = var.db_username_param
    db_password_param = var.db_password_param
    oidc_provider = var.oidc_provider
    oidc_client_id = var.oidc_client_id
    oidc_client_secret = var.oidc_client_secret

    hosted_zone_id = var.hosted_zone_id
    domain_name = var.domain_name
}


# module "api" {
#    source = "../modules/api"
#    aws_region = var.aws_region
#    env_id = var.env_id
#    vpc_id = var.vpc_id
#    api_subnet_ids = split(",", var.api_subnet_ids)
#    db_endpoint = var.db_endpoint
#    db_name = var.db_name
#    bucket_name = var.infra_bucket_name
#    db_username_param = var.db_username_param
#    db_password_param = var.db_password_param
#    oidc_provider = var.oidc_provider
#    oidc_client_id = var.oidc_client_id
#    oidc_client_secret = var.oidc_client_secret
#}
