#!/bin/bash
set -x 

TFSTATE_BUCKET=$(terraform -chdir=../tf-backend output -raw bucket_name)
TFSTATE_KEY="tfstate"
TFSTATE_REGION="us-east-1"
terraform init -migrate-state \
    -backend-config="bucket=${TFSTATE_BUCKET}" \
    -backend-config="key=${TFSTATE_KEY}" \
    -backend-config="region=${TFSTATE_REGION}"
