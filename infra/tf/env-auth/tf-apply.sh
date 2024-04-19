#!/bin/bash
set -x 

export TF_VAR_aws_region=$(tofu -chdir=../env-security output -raw aws_region)
export TF_VAR_env_id=$(tofu -chdir=../env-security output -raw env_id)

tofu apply -auto-approve 
