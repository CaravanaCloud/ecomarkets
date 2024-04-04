#!/bin/bash
SDIR="$(cd "$(dirname "${BASH_SOURCE[0]}" )" && pwd)"
source $SDIR/utils.sh

awaitTCP 9090

docker compose down
docker compose up

