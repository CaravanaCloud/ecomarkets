#!/bin/bash

#if [ ! -f "target/functions.zip" ]; then
#   mvn -B -fn --no-transfer-progress install
# fi

sam deploy \
  --template-file "infrastructure.cfn.yaml" \
  --stack-name  "ecomarkets-dev" \
  --capabilities "CAPABILITY_IAM" "CAPABILITY_AUTO_EXPAND" \
  --resolve-s3
