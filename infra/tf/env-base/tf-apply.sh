#!/bin/bash
set -x 

export TF_VAR_aws_region=$(terraform -chdir=../env-security output -raw aws_region)
export TF_VAR_env_id=$(terraform -chdir=../env-security output -raw env_id)
export TF_VAR_db_app_username=$(terraform -chdir=../env-security output -raw db_app_username)
export TF_VAR_db_app_password=$(terraform -chdir=../env-security output -raw db_app_password)

terraform apply -auto-approve 
