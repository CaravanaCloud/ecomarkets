#!/bin/bash

SDIR="$(cd "$(dirname "${BASH_SOURCE[0]}" )" && pwd)"
source $SDIR/utils.sh

awaitTCP 65432

DIR="$(dirname $SDIR)"
cd "$DIR/api"
quarkus dev
