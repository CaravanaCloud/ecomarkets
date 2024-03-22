#!/bin/bash
set -x 

export TF_VAR_aws_region=$(terraform -chdir=../env-base output -raw aws_region)
export TF_VAR_env_id=$(terraform -chdir=../env-base output -raw env_id)
export TF_VAR_vpc_id=$(terraform -chdir=../env-base output -raw vpc_id)
export TF_VAR_ecs_subnets=$(terraform -chdir=../env-base output -raw public_subnet_ids_str)
export TF_VAR_api_subnet_ids=$(terraform -chdir=../env-base output -raw public_subnet_ids_str)
export TF_VAR_db_endpoint=$(terraform -chdir=../env-base output -raw db_endpoint)
export TF_VAR_db_name=$(terraform -chdir=../env-base output -raw db_name)
export TF_VAR_bucket_name=$(terraform -chdir=../env-base output -raw storage_bucket_name)

terraform apply -auto-approve 
# aws eks update-kubeconfig \
#     --name $(terraform output -raw eks_cluster_name) \
#     --region $(aws configure get region)

