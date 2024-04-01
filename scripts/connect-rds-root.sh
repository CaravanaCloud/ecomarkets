#!/bin/bash
set -x

SDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
DIR="$SDIR/.."

export TF_VAR_db_endpoint=$(terraform -chdir=$DIR/infra/tf/env-base output -raw db_endpoint)
export TF_VAR_db_name=$(terraform -chdir=$DIR/infra/tf/env-base output -raw db_name)

PGPASSWORD="$TF_VAR_db_password_text" psql -h "TF_VAR_db_endpoint" -U "root" -p 5432 -d $TF_VAR_db_name
