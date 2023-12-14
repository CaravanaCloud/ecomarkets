#!/bin/bash
set -ex

#if [ ! -f "target/functions.zip" ]; then
#   mvn -B -fn --no-transfer-progress install
# fi

DEFAULT_ENV_ID=$(echo -n "$USER" | openssl dgst -md5 | cut -f2 -d" " | cut -c1-10)
CWD_NAME=$(basename $PWD)
ENV_ID=${ENV_ID:-"${CWD_NAME}${DEFAULT_ENV_ID}"}
STACK_NAME="${ENV_ID}"

echo "Checking AWS authentication"
aws sts get-caller-identity

echo "Deploying [$STACK_NAME]"
sleep 3


sam deploy \
  --template-file "infrastructure.cfn.yaml" \
  --stack-name  "${STACK_NAME}" \
  --capabilities "CAPABILITY_IAM" "CAPABILITY_AUTO_EXPAND" \
  --resolve-s3 \
  --parameter-overrides "EnvId=$ENV_ID" \
  --disable-rollback
