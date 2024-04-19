#!/bin/bash
set -x 

export TF_VAR_aws_region=$(tofu -chdir=../env-security output -raw aws_region)
export TF_VAR_env_id=$(tofu -chdir=../env-security output -raw env_id)
export TF_VAR_db_app_username=$(tofu -chdir=../env-security output -raw db_app_username)
export TF_VAR_db_app_password=$(tofu -chdir=../env-security output -raw db_app_password)
export TF_VAR_ecs_subnets=$(tofu -chdir=../env-base output -raw public_subnet_ids_str)
export TF_VAR_vpc_id=$(tofu -chdir=../env-base output -raw vpc_id)

sleep 15

tofu apply -auto-approve 
