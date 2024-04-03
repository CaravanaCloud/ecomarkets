#!/bin/bash
set -x

SDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
DIR="$SDIR/.."

export TF_VAR_db_endpoint=$(terraform -chdir=$DIR/infra/tf/env-base output -raw db_endpoint)
export TF_VAR_db_name=$(terraform -chdir=$DIR/infra/tf/env-base output -raw db_name)
export SECRET_ARN='arn:aws:secretsmanager:us-east-1:932529686876:secret:rds!cluster-837f0b7f-c829-485d-8fdf-5c10912f57e6-lTnS9I'
export SECRET_TXT=$(aws secretsmanager get-secret-value --secret-id $SECRET_ARN --query SecretString --output text)
export PGPASSWORD=$(echo $SECRET_TXT | jq -r .password)
echo $PGPASSWORD


psql -h "$TF_VAR_db_endpoint" -U "root" -p 5432 -d $TF_VAR_db_name
