#!/bin/bash
set -x

ENV_ID=${ENV_ID:-$USER}
echo "deleting infrastructure for $ENV_ID"


sam delete --no-prompts --stack-name "$ENV_ID-cdn" 

sam delete --no-prompts --stack-name "$ENV_ID-eks" 

sam delete --no-prompts --stack-name "$ENV_ID-api-dns" 

sam delete --no-prompts --stack-name "$ENV_ID-api-fn" 

sam delete --no-prompts --stack-name "$ENV_ID-db" 

sam delete --no-prompts --stack-name "$ENV_ID-net" 

sam delete --no-prompts --stack-name "$ENV_ID-params" 

echo "done delete "
