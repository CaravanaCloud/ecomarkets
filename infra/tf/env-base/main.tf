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
    source = "../modules/security"
    env_id = var.env_id
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
    depends_on = [ module.security ]
    vpc_id = module.network.vpc_id
    db_subnet_ids = var.db_publicly_accessible ? module.network.public_subnet_ids : module.network.private_subnet_ids
    db_username = module.security.db_username
    db_password = module.security.db_password
    publicly_accessible = var.db_publicly_accessible
}

module "database-user" {
    source = "../modules/database-user"
    
    db_host = module.database.db_host
    db_port = module.database.db_port
    db_name = module.database.db_name
    db_username = module.security.db_username
    db_password = module.security.db_password
    db_app_username = module.security.db_app_username
    db_app_password = module.security.db_app_password
}
