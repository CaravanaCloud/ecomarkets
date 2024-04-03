#!/bin/bash
set -x

SDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
DIR="$SDIR/.."

export TF_VAR_db_endpoint=$(terraform -chdir=$DIR/infra/tf/env-base output -raw db_endpoint)
export TF_VAR_db_name=$(terraform -chdir=$DIR/infra/tf/env-base output -raw db_name)
export SECRET_ARN=$(terraform -chdir=$DIR/infra/tf/env-base output -raw db_master_user_secret)
export SECRET_TXT=$(aws secretsmanager get-secret-value --secret-id $SECRET_ARN --query SecretString --output text)
export PGPASSWORD=$(echo $SECRET_TXT | jq -r .password)
echo $PGPASSWORD


psql -h "$TF_VAR_db_endpoint" -U "root" -p 5432 -d $TF_VAR_db_name
