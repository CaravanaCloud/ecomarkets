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


module "web_task" {
    source = "../modules/ecs-task"
    
    task_id = "web"
    path_pattern = "/*"
    container_image = "caravanacloud/ecomarkets-web:0.0.1"
    container_port = 9090
    container_cpu = 1024
    container_mem = 2048

    aws_region = var.aws_region
    vpc_id = var.vpc_id
    ecs_subnets = split(",", var.ecs_subnets)
    env_id = var.env_id

    cluster_id = var.cluster_id
    task_execution_role = var.task_execution_role

    db_endpoint = var.db_endpoint
    db_name = var.db_name
    
    db_app_username = var.db_app_username
    db_app_password = var.db_app_password

    oidc_provider = var.oidc_provider
    oidc_client_id = var.oidc_client_id
    oidc_client_secret = var.oidc_client_secret

    twilio_account_sid = var.twilio_account_sid
    twilio_auth_token = var.twilio_auth_token
    twilio_phone_from = var.twilio_phone_from



}

