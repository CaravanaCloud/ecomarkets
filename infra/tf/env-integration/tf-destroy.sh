#!/bin/bash
set -x 

export TF_VAR_env_id=$(terraform -chdir=../env-security output -raw env_id)
export TF_VAR_aws_region=$(terraform -chdir=../env-security output -raw aws_region)
export TF_VAR_domain_name=$(terraform -chdir=../env-security output -raw domain_name)

export TF_VAR_db_app_username=$(terraform -chdir=../env-security output -raw db_app_username)
export TF_VAR_db_app_password=$(terraform -chdir=../env-security output -raw db_app_password)

export TF_VAR_db_endpoint=$(terraform -chdir=../env-base output -raw db_endpoint)
export TF_VAR_db_port=$(terraform -chdir=../env-base output -raw db_port)
export TF_VAR_db_name=$(terraform -chdir=../env-base output -raw db_name)

export TF_VAR_db_master_user_secret=$(terraform -chdir=../env-base output -raw db_master_user_secret)

terraform destroy -auto-approve 
