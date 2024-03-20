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

module "security" {
    source = "./modules/security"
    env_id = var.env_id
}

module "storage" {
    source = "./modules/storage"
}

module "network" {
    source = "./modules/network"
    env_id = var.env_id
}

module "database" {
    source = "./modules/database"
    depends_on = [ module.security ]
    vpc_id = module.network.vpc_id
    db_subnet_ids = var.db_publicly_accessible ? module.network.public_subnet_ids : module.network.private_subnet_ids
    db_username = module.security.db_username
    db_password = module.security.db_password
}

module "ecs" {
    source = "./modules/ecs"
    depends_on = [ module.security ]
    aws_region = var.aws_region
    env_id = var.env_id
    vpc_id = module.network.vpc_id
    ecs_subnets = module.network.public_subnet_ids
}

module "api" {
    source = "./modules/api"
    api_subnet_ids = module.network.public_subnet_ids
    db_endpoint = module.database.db_endpoint
    db_name = module.database.db_name
    bucket_name = module.storage.bucket_name
#    db_username = module.security.db_username
#    db_password = module.security.db_password
}

## TODO
# DB
# API
# APP 

