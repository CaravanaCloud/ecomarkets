#!/bin/bash
set -x

SDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
DIR="$(dirname $SDIR)"

source $SDIR/utils.sh

awaitTCP 65432

cd  $DIR/vdn
quarkus dev
