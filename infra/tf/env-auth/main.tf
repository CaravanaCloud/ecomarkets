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

resource "aws_cognito_identity_pool" "main" {
  identity_pool_name        = "${var.env_id}_identity_pool"
  allow_unauthenticated_identities = true

  cognito_identity_providers {
    client_id               = var.google_client_id
    provider_name           = "accounts.google.com"
    server_side_token_check = true
  }
}

resource "aws_iam_role" "authenticated_role" {
  name = "Cognito_AuthenticatedRole"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRoleWithWebIdentity"
        Effect = "Allow"
        Principal = {
          Federated = "cognito-identity.amazonaws.com"
        }
        Condition = {
          StringEquals = {
            "cognito-identity.amazonaws.com:aud" = "YOUR_IDENTITY_POOL_ID"
          }
          "ForAnyValue:StringLike" = {
            "cognito-identity.amazonaws.com:amr" = "authenticated"
          }
        }
      },
    ]
  })
}

resource "aws_iam_role" "unauthenticated_role" {
  name = "Cognito_UnauthenticatedRole"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRoleWithWebIdentity"
        Effect = "Allow"
        Principal = {
          Federated = "cognito-identity.amazonaws.com"
        }
        Condition = {
          StringEquals = {
            "cognito-identity.amazonaws.com:aud" = "YOUR_IDENTITY_POOL_ID"
          }
          "ForAnyValue:StringLike" = {
            "cognito-identity.amazonaws.com:amr" = "unauthenticated"
          }
        }
      },
    ]
  })
}

resource "aws_iam_policy" "s3_get_object" {
  name        = "S3GetObjectPolicy"
  description = "Policy for allowing GetObject from a specific S3 bucket"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "s3:GetObject",
        ]
        Effect   = "Allow"
        Resource = [
          "arn:aws:s3:::your-s3-bucket-name/*",
        ]
      },
    ]
  })
}

resource "aws_iam_role_policy_attachment" "authenticated_role_attachment" {
  role       = aws_iam_role.authenticated_role.name
  policy_arn = aws_iam_policy.s3_get_object.arn
}

resource "aws_iam_role_policy_attachment" "unauthenticated_role_attachment" {
  role       = aws_iam_role.unauthenticated_role.name
  policy_arn = aws_iam_policy.s3_get_object.arn
}


resource "aws_cognito_identity_pool_roles_attachment" "main_roles_attachment" {
  identity_pool_id = aws_cognito_identity_pool.main.id

  roles = {
    # Define roles here
    "authenticated"   =  aws_iam_role.authenticated_role.arn
    "unauthenticated" =  aws_iam_role.unauthenticated_role.arn
  }
}

resource "aws_cognito_user_pool" "main" {
  name = "${var.env_id}_user_pool"
}

resource "aws_cognito_user_pool_client" "app_client" {
  name         = "app_client"
  user_pool_id = aws_cognito_user_pool.main.id

  # Additional configurations can be added here based on your requirements
}


