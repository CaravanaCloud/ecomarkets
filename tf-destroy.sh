#!/bin/bash
set -x
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

aws sts get-caller-identity
sleep 10


#Base
pushd infra/tf/env-base
./tf-destroy.sh
popd

pushd infra/tf/env-services
./tf-destroy.sh
popd

echo done
