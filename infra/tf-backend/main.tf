# terraform init 
# terraform apply -auto-approve
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

variable "aws_region" {
  default     = "us-east-1"
}

provider "aws" {
  region = var.aws_region
}

resource "aws_s3_bucket" "backend_bucket" {
}

output "bucket_name" {
  value = aws_s3_bucket.backend_bucket.id
}
