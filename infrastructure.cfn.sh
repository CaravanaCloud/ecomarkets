#!/bin/bash
set -x

ENV_ID=${ENV_ID:-$USER}
echo "Deploying infrastructure for $ENV_ID"

SAM_XARGS="--resolve-s3 --capabilities CAPABILITY_IAM CAPABILITY_AUTO_EXPAND --parameter-overrides EnvId=$ENV_ID --disable-rollback --no-fail-on-empty-changeset"

sam deploy --stack-name "$ENV_ID-params" \
    --template-file ecomarkets/infra_params.cfn.yaml \
    $SAM_XARGS

sam deploy --stack-name "$ENV_ID-net" \
    --template-file ecomarkets/infra_net.cfn.yaml \
    $SAM_XARGS

sam deploy --stack-name "$ENV_ID-db" \
    --template-file ecomarkets/infra_db.cfn.yaml \
    $SAM_XARGS

# run build if function.zip does not exist
if [ ! -f ecomarkets/target/function.zip ]; then
    echo "Building function.zip"
    mvn -f ecomarkets -fn clean package 
fi

sam deploy --stack-name "$ENV_ID-api-fn" \
    --template-file ecomarkets/infra_fn.cfn.yaml \
    $SAM_XARGS

ZONE_ID="Z03610681TY8GXFI3PJQE"
DOMAIN_NAME="temp.ecofeiras.com"
CERTIFICATE_ARN="arn:aws:acm:us-east-1:932529686876:certificate/7c0f9779-0f92-4beb-bb9e-43c9d9675625"

sam deploy --stack-name "$ENV_ID-api-dns" \
    --template-file ecomarkets/infra_api_dns.cfn.yaml \
    --parameter-overrides EnvId=$ENV_ID HostedZoneId=$ZONE_ID DomainName=$DOMAIN_NAME RegionalCertificateArn=$CERTIFICATE_ARN \
    --resolve-s3 \
    --capabilities CAPABILITY_IAM CAPABILITY_AUTO_EXPAND \
    --disable-rollback \
    --no-fail-on-empty-changeset

sam deploy --stack-name "$ENV_ID-eks" \
    --template-file ecomarkets-app/infra_eks.cfn.yaml \
    $SAM_XARGS

sam deploy --stack-name "$ENV_ID-cdn" \
    --template-file ecomarkets/infra_cdn.cfn.yaml \
    --parameter-overrides EnvId=$ENV_ID CertificateArn=$CERTIFICATE_ARN \
    --resolve-s3 \
    --capabilities CAPABILITY_IAM CAPABILITY_AUTO_EXPAND \
    --disable-rollback \
    --no-fail-on-empty-changeset

echo "done deploy"

# Deploy 
EKS_CLUSTER_NAME="${EnvId}-EKS-Cluster"
aws eks --region $AWS_REGION \
     update-kubeconfig \
     --name $EKS_CLUSTER_NAME

# kubectl create namespace ecomarkets-ns


echo "done config"

echo "done."