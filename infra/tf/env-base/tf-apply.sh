#!/bin/bash
set -x 

terraform apply -auto-approve 
# aws eks update-kubeconfig \
#     --name $(terraform output -raw eks_cluster_name) \
#     --region $(aws configure get region)

