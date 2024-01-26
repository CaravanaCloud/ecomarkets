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
export OCP_BASE_DOMAIN=${OCP_BASE_DOMAIN:-"devcluster.openshift.com"}
export SSH_KEY=$(cat $HOME/.ssh/id_rsa.pub)
export INSTANCE_TYPE=${INSTANCE_TYPE:-"m6i.2xlarge"}

echo "Pre-flight checks..."
check_variables "AWS_REGION" "OCP_BASE_DOMAIN" "PULL_SECRET" "SSH_KEY"
aws sts get-caller-identity
envsubst < "install-config.env.yaml" > "install-config.yaml"
cp "install-config.yaml" "install-config.bak.yaml"

echo "Creating cluster $CLUSTER_NAME"
echo "WARNING: This will run 1 x $INSTANCE_TYPE instances on your AWS account."
sleep 5

openshift-install create cluster --log-level debug | tee create-cluster.log 
openshift-install wait-for install-complete

mkdir -p "$HOME/.kube"
cp "$DIR/auth/kubeconfig" "$HOME/.kube/config"

oc cluster-info

# some things to try
# kubectl get nodes
# kubectl get deployments --all-namespaces
# kubectl run -it --rm --restart=Never --image=busybox:1.33.1 testpod -- /bin/sh


echo "cluster $CLUSTER_NAME created."
