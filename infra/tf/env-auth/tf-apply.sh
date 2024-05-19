#!/bin/bash
set -x 

export TF_VAR_aws_region=$(tofu -chdir=../env-security output -raw aws_region)
export TF_VAR_env_id=$(tofu -chdir=../env-security output -raw env_id)

export TF_VAR_oidc_google_id_ssm=$(tofu -chdir=../env-security output -raw oidc_client_id)
export TF_VAR_oidc_google_secret_ssm=$(tofu -chdir=../env-security output -raw oidc_client_secret)

tofu apply -auto-approve 
