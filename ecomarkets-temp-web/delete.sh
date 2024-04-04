#!/bin/bash

DEFAULT_ENV_ID=$(echo -n "$USER" | openssl dgst -md5 | cut -f2 -d" " | cut -c1-10)
CWD_NAME=$(basename $PWD)
ENV_ID=${ENV_ID:-"${CWD_NAME}${DEFAULT_ENV_ID}"}
STACK_NAME="$ENV_ID"

echo "Deleting [$STACK_NAME]"

sam delete \
  --no-prompts \
  --stack-name  "$STACK_NAME"
  