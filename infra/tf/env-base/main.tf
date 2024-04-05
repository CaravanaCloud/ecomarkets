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


module "infra_storage" {
    source = "../modules/storage"
    bucket_prefix = "infra-"
}

module "network" {
    source = "../modules/network"
    env_id = var.env_id
}

module "database" {
    source = "../modules/database"
    vpc_id = module.network.vpc_id
    db_subnet_ids = var.db_publicly_accessible ? module.network.public_subnet_ids : module.network.private_subnet_ids
    publicly_accessible = var.db_publicly_accessible
}

module "ecs-cluster"{
  source = "../modules/ecs-cluster"
  env_id = var.env_id
}