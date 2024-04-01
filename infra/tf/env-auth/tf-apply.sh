./#!/bin/bash
set -x 

export TF_VAR_env_id=$(terraform -chdir=../env-security output -raw env_id)

terraform apply -auto-approve 
