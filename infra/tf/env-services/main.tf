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
    health_check_path = "/_hc"
    path_pattern = "/*"
    priority = 95
    container_image = "caravanacloud/ecomarkets-web:0.0.1"
    container_port = 9090
    container_cpu = 1024
    container_mem = 2048

    env_id = var.env_id
    aws_region = var.aws_region
    vpc_id = var.vpc_id
    ecs_subnets = split(",", var.ecs_subnets)
    listener_arn = var.listener_arn

    cluster_id = var.cluster_id
    task_execution_role = var.task_execution_role

    db_endpoint = var.db_endpoint
    db_name = var.db_name
    
    db_app_username = var.db_app_username
    db_app_password = var.db_app_password

    oidc_auth_server_url = var.oidc_auth_server_url
    oidc_client_id = var.oidc_client_id
    oidc_client_secret = var.oidc_client_secret

    twilio_account_sid = var.twilio_account_sid
    twilio_auth_token = var.twilio_auth_token
    twilio_phone_from = var.twilio_phone_from

    count_instances = 1

}

module "api_task" {
    source = "../modules/ecs-task"
    
    task_id = "api"
    health_check_path = "/api/_hc"
    path_pattern = "/api*"
    priority = 50
    container_image = "caravanacloud/ecomarkets-api:0.0.1"
    container_port = 9091
    container_cpu = 1024
    container_mem = 2048

    env_id = var.env_id
    aws_region = var.aws_region
    vpc_id = var.vpc_id
    ecs_subnets = split(",", var.ecs_subnets)
    listener_arn = var.listener_arn

    cluster_id = var.cluster_id
    task_execution_role = var.task_execution_role

    db_endpoint = var.db_endpoint
    db_name = var.db_name
    
    db_app_username = var.db_app_username
    db_app_password = var.db_app_password

    oidc_auth_server_url = var.oidc_auth_server_url
    oidc_client_id = var.oidc_client_id
    oidc_client_secret = var.oidc_client_secret

    twilio_account_sid = var.twilio_account_sid
    twilio_auth_token = var.twilio_auth_token
    twilio_phone_from = var.twilio_phone_from

    count_instances = 1
}

module "vdn_task" {
    source = "../modules/ecs-task"
    
    task_id = "vdn"
    health_check_path = "/vdn/_hc"
    path_pattern = "/vdn*"
    priority = 40
    container_image = "caravanacloud/ecomarkets-vdn:0.0.1"
    container_port = 9092
    container_cpu = 1024
    container_mem = 2048

    env_id = var.env_id
    aws_region = var.aws_region
    vpc_id = var.vpc_id
    ecs_subnets = split(",", var.ecs_subnets)
    listener_arn = var.listener_arn

    cluster_id = var.cluster_id
    task_execution_role = var.task_execution_role

    db_endpoint = var.db_endpoint
    db_name = var.db_name
    
    db_app_username = var.db_app_username
    db_app_password = var.db_app_password

    oidc_auth_server_url = var.oidc_auth_server_url
    oidc_client_id = var.oidc_client_id
    oidc_client_secret = var.oidc_client_secret

    twilio_account_sid = var.twilio_account_sid
    twilio_auth_token = var.twilio_auth_token
    twilio_phone_from = var.twilio_phone_from

    count_instances = 1
}

module "app_task" {
    source = "../modules/ecs-task"
    
    task_id = "app"
    health_check_path = "/app/"
    path_pattern = "/app*"
    priority = 30
    container_image = "caravanacloud/ecomarkets-app:0.0.1"
    container_port = 9093
    container_cpu = 1024
    container_mem = 2048

    env_id = var.env_id
    aws_region = var.aws_region
    vpc_id = var.vpc_id
    ecs_subnets = split(",", var.ecs_subnets)
    listener_arn = var.listener_arn

    cluster_id = var.cluster_id
    task_execution_role = var.task_execution_role

    db_endpoint = var.db_endpoint
    db_name = var.db_name
    
    db_app_username = var.db_app_username
    db_app_password = var.db_app_password

    oidc_auth_server_url = var.oidc_auth_server_url
    oidc_client_id = var.oidc_client_id
    oidc_client_secret = var.oidc_client_secret

    twilio_account_sid = var.twilio_account_sid
    twilio_auth_token = var.twilio_auth_token
    twilio_phone_from = var.twilio_phone_from

    count_instances = 0 //there is nothing for while
}