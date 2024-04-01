#!/bin/bash
set -x 

export TF_VAR_aws_region=$(terraform -chdir=../env-security output -raw aws_region)
export TF_VAR_env_id=$(terraform -chdir=../env-security output -raw env_id)

terraform apply -auto-approve 
