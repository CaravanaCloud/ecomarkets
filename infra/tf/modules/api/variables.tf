variable "db_username" {}

variable "db_password" {}

variable "api_subnet_ids" {
    description = "The subnet ids for the API"
    type        = list(string)
}

variable "db_endpoint" {
    description = "The endpoint of the RDS cluster"
    type        = string
}

variable "db_name" {
    description = "The endpoint of the RDS cluster"
    type        = string
}

variable "bucket_name" {
    description = "The name of the S3 bucket for the code"
    type        = string
}

