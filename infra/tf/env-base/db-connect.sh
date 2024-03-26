#!/bin/bash
set -x 

export DB_ENDPOINT=$(terraform -chdir=../env-base output -raw db_endpoint)
export DB_NAME=$(terraform -chdir=../env-base output -raw db_name)
export DB_USERNAME_PARAM=$(terraform -chdir=../env-base output -raw db_username_param)
export DB_PASSWORD_PARAM=$(terraform -chdir=../env-base output -raw db_password_param)
export DB_USERNAME=$(aws ssm get-parameter --name $DB_USERNAME_PARAM --query Parameter.Value --output text)
export DB_PASSWORD=$(aws ssm get-parameter --name $DB_PASSWORD_PARAM --query Parameter.Value --output text)

#connect to psql
psql "host=$DB_ENDPOINT dbname=$DB_NAME user=$DB_USERNAME password=$DB_PASSWORD sslmode=require"
