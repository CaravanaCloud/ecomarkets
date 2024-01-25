#!/bin/bash
set -ex
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
PDIR="$(dirname "$DIR")"
ENV_ID=${ENV_ID:-$USER}
echo "Deploying infrastructure for $ENV_ID"

SAM_XARGS="--resolve-s3 --capabilities CAPABILITY_NAMED_IAM CAPABILITY_AUTO_EXPAND --parameter-overrides EnvId=$ENV_ID --disable-rollback --no-fail-on-empty-changeset"

sam deploy --stack-name "$ENV_ID-net" \
    --template-file infra_net.cfn.yaml \
    $SAM_XARGS

sam deploy --stack-name "$ENV_ID-params" \
    --template-file infra_params.cfn.yaml \
    $SAM_XARGS

sam deploy --stack-name "$ENV_ID-db" \
    --template-file infra_db.cfn.yaml \
    $SAM_XARGS

# run build if function.zip does not exist
if [ ! -f "$PDIR/ecomarkets/target/function.zip" ]; then
    echo "Building function.zip"
    mvn -f "$PDIR/ecomarkets" -fn clean package 
fi

sam deploy --stack-name "$ENV_ID-api-fn" \
    --template-file infra_fn.cfn.yaml \
    $SAM_XARGS

sam deploy --stack-name "$ENV_ID-api-dns" \
    --template-file infra_api_dns.cfn.yaml \
    --parameter-overrides EnvId=$ENV_ID EnvAlias=$ENV_ALIAS HostedZoneId=$ZONE_ID DomainName=$DOMAIN_NAME RegionalCertificateArn=$CERTIFICATE_ARN \
    --resolve-s3 \
    --capabilities CAPABILITY_NAMED_IAM CAPABILITY_AUTO_EXPAND \
    --disable-rollback \
    --no-fail-on-empty-changeset

sam deploy --stack-name "$ENV_ID-api-dns-latest" \
    --template-file infra_api_dns.cfn.yaml \
    --parameter-overrides EnvId=$ENV_ID EnvAlias=latest HostedZoneId=$ZONE_ID DomainName=$DOMAIN_NAME RegionalCertificateArn=$CERTIFICATE_ARN \
    --resolve-s3 \
    --capabilities CAPABILITY_NAMED_IAM CAPABILITY_AUTO_EXPAND \
    --disable-rollback \
    --no-fail-on-empty-changeset

#sam deploy --stack-name "$ENV_ID-eks" \
#    --template-file ecomarkets-app/infra_eks.cfn.yaml \
#    $SAM_XARGS

sam deploy --stack-name "$ENV_ID-cdn" \
    --template-file infra_cdn.cfn.yaml \
    --parameter-overrides EnvId=$ENV_ID EnvAlias=$ENV_ALIAS DomainName=$DOMAIN_NAME CertificateArn=$CERTIFICATE_ARN \
    --resolve-s3 \
    --capabilities CAPABILITY_IAM CAPABILITY_AUTO_EXPAND \
    --disable-rollback \
    --no-fail-on-empty-changeset



echo "done deploy"

# Deploy 
# EKS_CLUSTER_NAME="${EnvId}-EKS-Cluster"
# aws eks update-kubeconfig \
#     --region $AWS_REGION \
#     --name $EKS_CLUSTER_NAME

# kubectl create namespace ecomarkets-ns


echo "done config"

echo "done."