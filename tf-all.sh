#!/bin/bash
set -x
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

aws sts get-caller-identity
sleep 10

# Backend
terraform -chdir="$DIR/infra/tf/tf-backend" init 
terraform -chdir="$DIR/infra/tf/tf-backend" apply -auto-approve

#Base
pushd infra/tf/env-base
./tf-init.sh
./tf-apply.sh
popd

pushd infra/tf/env-services
./tf-init.sh
./tf-apply.sh
popd

echo done
