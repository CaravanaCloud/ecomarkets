#!/bin/bash
set -x

SDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
DIR="$SDIR/.."

export DB_PORT=5432
export db_endpoint=$(terraform -chdir=$DIR/infra/tf/env-base output -raw db_endpoint)
export db_name=$(terraform -chdir=$DIR/infra/tf/env-base output -raw db_name)
export db_app_username=$(terraform -chdir=$DIR/infra/tf/env-security output -raw db_app_username)
export db_app_password=$(terraform -chdir=$DIR/infra/tf/env-security output -raw db_app_password)
export db_app_username_text=$(aws ssm get-parameter --name $db_app_username --query Parameter.Value --output text)
export db_app_password_text=$(aws ssm get-parameter --name $db_app_password --query Parameter.Value --output text)
export PGPASSWORD="$db_app_password_text"

psql -h "$db_endpoint" -U "$db_app_username_text" -p 5432 -d $db_name
