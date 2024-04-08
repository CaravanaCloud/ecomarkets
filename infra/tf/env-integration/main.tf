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


module "database-user" {
    source = "../modules/database-user"
    
    db_endpoint = var.db_endpoint
    db_port = var.db_port
    db_name = var.db_name
    db_master_user_secret = var.db_master_user_secret
    db_app_username = var.db_app_username
    db_app_password =var.db_app_password
}
