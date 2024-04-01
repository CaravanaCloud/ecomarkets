#!/bin/bash
set -x
SDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
DIR="$SDIR/.."

aws sts get-caller-identity
sleep 10


pushd infra/tf/env-services
./tf-destroy.sh
popd

pushd infra/tf/env-base
./tf-destroy.sh
popd

pushd infra/tf/env-security
./tf-destroy.sh
popd

echo done
