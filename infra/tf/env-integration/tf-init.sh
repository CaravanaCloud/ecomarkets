#!/bin/bash
set -x 

TFSTATE_BUCKET=$(tofu -chdir=../tf-backend output -raw bucket_name)
TFSTATE_KEY="env-integration"
TFSTATE_REGION=$(aws configure get region)
tofu init -reconfigure \
    -backend-config="bucket=${TFSTATE_BUCKET}" \
    -backend-config="key=${TFSTATE_KEY}" \
    -backend-config="region=${TFSTATE_REGION}"
