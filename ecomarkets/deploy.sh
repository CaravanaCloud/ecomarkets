#!/bin/bash

#if [ ! -f "target/functions.zip" ]; then
#   mvn -B -fn --no-transfer-progress install
# fi

export ENV_ID=$(echo -n "$USER" | openssl dgst -md5 | cut -f2 -d" ")
export STACK_NAME="ecomarkets$ENV_ID"

sam deploy \
  --template-file "infrastructure.cfn.yaml" \
  --stack-name  "$STACK_NAME" \
  --capabilities "CAPABILITY_IAM" "CAPABILITY_AUTO_EXPAND" \
  --resolve-s3
