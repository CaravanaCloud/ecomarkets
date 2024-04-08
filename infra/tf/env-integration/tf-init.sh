#!/bin/bash
set -x 

TFSTATE_BUCKET=$(terraform -chdir=../tf-backend output -raw bucket_name)
TFSTATE_KEY="env-dev"
TFSTATE_REGION=$(aws configure get region)
terraform init -reconfigure \
    -backend-config="bucket=${TFSTATE_BUCKET}" \
    -backend-config="key=${TFSTATE_KEY}" \
    -backend-config="region=${TFSTATE_REGION}"
