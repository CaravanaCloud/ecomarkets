#!/bin/bash
set -x

ENV_ID=${ENV_ID:-$USER}

sam delete --stack-name "$ENV_ID-eks" --no-prompts
sam delete --stack-name "$ENV_ID-api-dns" --no-prompts
sam delete --stack-name "$ENV_ID-fn" --no-prompts
sam delete --stack-name "$ENV_ID-db" --no-prompts
sam delete --stack-name "$ENV_ID-net" --no-prompts
sam delete --stack-name "$ENV_ID-params" --no-prompts
