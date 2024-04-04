variable env_id {}
variable aws_region {}
variable vpc_id {}

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

variable "code_package_path" {
    type = string
    default = "../../../api/target/function.zip"
}

variable "db_username_param" {}
variable "db_password_param" {}
variable "oidc_provider" {} 
variable "oidc_client_id" {}
variable "oidc_client_secret" {}

variable "memory_size" {
    description = "The amount of memory to allocate to the lambda"
    type        = number
    default     = 1024
}

variable "timeout" {
    type = number
    default = 15
}