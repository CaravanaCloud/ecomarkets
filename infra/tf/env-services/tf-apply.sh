#!/bin/bash
set -x 


export TF_VAR_env_id=$(tofu -chdir=../env-security output -raw env_id)
export TF_VAR_aws_region=$(tofu -chdir=../env-security output -raw aws_region)
export TF_VAR_domain_name=$(tofu -chdir=../env-security output -raw domain_name)

export TF_VAR_db_app_username=$(tofu -chdir=../env-security output -raw db_app_username)
export TF_VAR_db_app_password=$(tofu -chdir=../env-security output -raw db_app_password)

export TF_VAR_twilio_account_sid=$(tofu -chdir=../env-security output -raw twilio_account_sid)
export TF_VAR_twilio_auth_token=$(tofu -chdir=../env-security output -raw twilio_auth_token)
export TF_VAR_twilio_phone_from=$(tofu -chdir=../env-security output -raw twilio_phone_from)

export TF_VAR_oidc_client_id=$(tofu -chdir=../env-security output -raw oidc_client_id)
export TF_VAR_oidc_client_secret=$(tofu -chdir=../env-security output -raw oidc_client_secret)
export TF_VAR_oidc_provider=$(tofu -chdir=../env-security output -raw oidc_provider)

export TF_VAR_vpc_id=$(tofu -chdir=../env-base output -raw vpc_id)
export TF_VAR_ecs_subnets=$(tofu -chdir=../env-base output -raw public_subnet_ids_str)
export TF_VAR_api_subnet_ids=$(tofu -chdir=../env-base output -raw public_subnet_ids_str)

export TF_VAR_db_endpoint=$(tofu -chdir=../env-base output -raw db_endpoint)
export TF_VAR_db_name=$(tofu -chdir=../env-base output -raw db_name)

export TF_VAR_infra_bucket_name=$(tofu -chdir=../env-base output -raw infra_bucket_name)
export TF_VAR_cluster_id=$(tofu -chdir=../env-base output -raw cluster_id)
export TF_VAR_task_execution_role=$(tofu -chdir=../env-base output -raw task_execution_role)
export TF_VAR_listener_arn=$(tofu -chdir=../env-base output -raw listener_arn)



tofu apply -auto-approve 
