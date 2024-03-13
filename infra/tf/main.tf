terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
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

## TODO
# DB
# API
# APP 

