#!/bin/bash
set -x
SDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
DIR="$SDIR/.."

aws sts get-caller-identity
sleep 10

# Apply Backend
terraform -chdir="$DIR/infra/tf/tf-backend" init 
terraform -chdir="$DIR/infra/tf/tf-backend" apply -auto-approve

# Apply Backend
pushd infra/tf/env-security
./tf-init.sh
./tf-apply.sh
popd

pushd infra/tf/env-base
./tf-init.sh
./tf-apply.sh
popd

pushd infra/tf/env-integration
./tf-init.sh
./tf-apply.sh
popd

pushd infra/tf/env-services
./tf-init.sh
./tf-apply.sh
popd

# AUTH

echo done
