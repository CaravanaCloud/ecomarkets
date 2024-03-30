#!/bin/bash
set -x 

export TF_VAR_domain_name=$(terraform -chdir=../env-security output -raw domain_name)
export TF_VAR_aws_region=$(terraform -chdir=../env-security output -raw aws_region)
export TF_VAR_env_id=$(terraform -chdir=../env-security output -raw env_id)
export TF_VAR_db_app_username=$(terraform -chdir=../env-security output -raw db_username)
export TF_VAR_db_app_password=$(terraform -chdir=../env-security output -raw db_password)


export TF_VAR_vpc_id=$(terraform -chdir=../env-base output -raw vpc_id)
export TF_VAR_ecs_subnets=$(terraform -chdir=../env-base output -raw public_subnet_ids_str)
export TF_VAR_api_subnet_ids=$(terraform -chdir=../env-base output -raw public_subnet_ids_str)
export TF_VAR_db_endpoint=$(terraform -chdir=../env-base output -raw db_endpoint)
export TF_VAR_db_name=$(terraform -chdir=../env-base output -raw db_name)
export TF_VAR_infra_bucket_name=$(terraform -chdir=../env-base output -raw infra_bucket_name)


terraform destroy -auto-approve 
