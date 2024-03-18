#!/bin/bash
set -ex
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
PDIR="$(dirname "$DIR")"
ENV_ID=${ENV_ID:-$USER}

if [ ! -f "$PDIR/ecomarkets/target/function.zip" ]; then
    echo "Building function.zip"
    mvn -f "$PDIR/ecomarkets" -fn clean package 
fi

sam deploy --stack-name "$ENV_ID-api-fn" \
    --template-file infra_fn.cfn.yaml \
    $SAM_XARGS