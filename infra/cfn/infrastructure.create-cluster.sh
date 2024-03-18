#!/bin/bash
set -ex
export DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

check_variables() {
  for var in "$@"; do
    if [ -z "${!var}" ]; then
      echo "Error: Variable '$var' is not set or has zero length." >&2
      exit 1
    fi
  done
}

export AWS_REGION=${AWS_REGION:-"us-east-1"}
export CLUSTER_NAME=${CLUSTER_NAME:-"okd$RANDOM"}
export OCP_BASE_DOMAIN=${DOMAIN_NAME:-"qa.ecofeiras.com"}
export SSH_KEY=$(cat $HOME/.ssh/id_rsa.pub)
export INSTANCE_TYPE=${INSTANCE_TYPE:-"t3.2xlarge"}
export DRY_RUN=${DRY_RUN:-"false"}


echo "Pre-flight checks..."
check_variables "AWS_REGION" "OCP_BASE_DOMAIN" "PULL_SECRET" "SSH_KEY"
aws sts get-caller-identity
envsubst < "install-config.env.yaml" > "install-config.yaml"
cp "install-config.yaml" "install-config.bak.yaml"

echo "Creating cluster $CLUSTER_NAME"
echo "WARNING: This will run 1 x $INSTANCE_TYPE instances on your AWS account."
sleep 5

if [ "$DRY_RUN" == "true" ]; then
  echo "Dry run, exiting."
  exit 0
fi

./openshift-install create cluster --log-level debug | tee create-cluster.log 
if [ $? -ne 0 ]; then
    echo "Create cluster failed, exiting."
    exit 1
fi


#./openshift-install wait-for install-complete

echo export KUBECONFIG="$DIR/auth/kubeconfig" "$HOME/.kube/config"
export KUBECONFIG="$DIR/auth/kubeconfig"

./oc cluster-info

echo "cluster $CLUSTER_NAME created."
