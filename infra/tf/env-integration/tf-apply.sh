#!/bin/bash
set -x 


export TF_VAR_env_id=$(tofu -chdir=../env-security output -raw env_id)
export TF_VAR_aws_region=$(tofu -chdir=../env-security output -raw aws_region)
export TF_VAR_domain_name=$(tofu -chdir=../env-security output -raw domain_name)

export TF_VAR_db_app_username=$(tofu -chdir=../env-security output -raw db_app_username)
export TF_VAR_db_app_password=$(tofu -chdir=../env-security output -raw db_app_password)

export TF_VAR_db_endpoint=$(tofu -chdir=../env-base output -raw db_endpoint)
export TF_VAR_db_port=$(tofu -chdir=../env-base output -raw db_port)
export TF_VAR_db_name=$(tofu -chdir=../env-base output -raw db_name)

export TF_VAR_db_master_user_secret=$(tofu -chdir=../env-base output -raw db_master_user_secret)

tofu apply -auto-approve 
