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

data "aws_ssm_parameter" "oidc_google_id_text" {
  name = var.oidc_google_id_ssm
}

data "aws_ssm_parameter" "oidc_google_secret_text" {
  name = var.oidc_google_secret_ssm
}


resource "aws_cognito_user_pool" "main" {
  name = "${replace(var.env_id, "/[^a-zA-Z0-9_]/", "")}_user_pool"
  auto_verified_attributes  = [
    "email",
  ]
#  deletion_protection       = "ACTIVE"
  mfa_configuration         = "OFF"
  tags                      = {}
  tags_all                  = {}
  username_attributes       = [
    "email",
  ]

  account_recovery_setting {
    recovery_mechanism {
      name     = "verified_email"
      priority = 1
    }
  }

  admin_create_user_config {
    allow_admin_create_user_only = false
  }

  email_configuration {
    email_sending_account = "COGNITO_DEFAULT"
  }

  password_policy {
    minimum_length                   = 8
    require_lowercase                = true
    require_numbers                  = true
    require_symbols                  = true
    require_uppercase                = true
    temporary_password_validity_days = 7
  }

  schema {
    attribute_data_type      = "String"
    developer_only_attribute = false
    mutable                  = true
    name                     = "email"
    required                 = true

    string_attribute_constraints {
      max_length = "2048"
      min_length = "0"
    }
  }

  user_attribute_update_settings {
    attributes_require_verification_before_update = [
      "email",
    ]
  }

  username_configuration {
    case_sensitive = false
  }

  verification_message_template {
    default_email_option = "CONFIRM_WITH_CODE"
  }
}

resource "aws_cognito_user_pool_client" "app_client" {
  name         = "${replace(var.env_id, "/[^a-zA-Z0-9_]/", "")}_client"
  user_pool_id = aws_cognito_user_pool.main.id
  access_token_validity                         = 60
  allowed_oauth_flows                           = [
    "code",
  ]

  generate_secret                               = true

  allowed_oauth_flows_user_pool_client          = true
  allowed_oauth_scopes                          = [
    "email",
    "openid",
    "phone",
  ]
  auth_session_validity                         = 3
  callback_urls                                 = [
    "https://${var.env_id}.${var.domain_name}/vdn",
    "https://${var.env_id}.${var.domain_name}/api",
    "https://${var.env_id}.${var.domain_name}/web",
    "https://${var.env_id}.${var.domain_name}/",
  ]
  enable_propagate_additional_user_context_data = false
  enable_token_revocation                       = true
  explicit_auth_flows                           = [
    "ALLOW_REFRESH_TOKEN_AUTH",
    "ALLOW_USER_SRP_AUTH",
  ]
  id_token_validity                             = 60
  logout_urls                                   = []
  prevent_user_existence_errors                 = "ENABLED"
  read_attributes                               = [
    "email",
  ]
  refresh_token_validity                        = 30
  supported_identity_providers                  = [
    "COGNITO",
    "Google",
  ]
  write_attributes                              = [
    "email",
  ]

  token_validity_units {
    access_token  = "minutes"
    id_token      = "minutes"
    refresh_token = "days"
  }
}

resource "aws_cognito_identity_provider" "google" {
  user_pool_id  = aws_cognito_user_pool.main.id
  provider_name = "Google"
  provider_type = "Google"

  provider_details = {
    authorize_scopes = "openid email profile"
    client_id        = data.aws_ssm_parameter.oidc_google_id_text.value
    client_secret    = data.aws_ssm_parameter.oidc_google_secret_text.value
  }

  attribute_mapping = {
    email    = "email"
    username = "sub"
  }
}

resource "aws_cognito_user_pool_domain" "main" {
  domain       = "ecomarkets"
  user_pool_id = aws_cognito_user_pool.main.id
}



