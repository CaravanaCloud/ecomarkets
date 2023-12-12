#!/bin/bash


export ENV_ID=$(echo -n "$USER" | openssl dgst -md5 | cut -f2 -d" ")
export STACK_NAME="ecomarkets$ENV_ID"

sam delete \
  --no-prompts \
  --stack-name  "$STACK_NAME"
  
