#!/bin/bash
set -x

DIR="$(dirname $( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd ))"

sleep 45
docker compose down
docker compose up

