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

    db_app_username_text = var.db_app_username_text
    db_app_password_text = var.db_app_password_text

    oidc_client_id_text = var.oidc_client_id_text
    oidc_client_secret_text = var.oidc_client_secret_text

    twilio_account_sid_text = var.twilio_account_sid_text
    twilio_auth_token_text = var.twilio_auth_token_text
    twilio_phone_from_text = var.twilio_phone_from_text
}
