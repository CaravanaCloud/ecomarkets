#!/bin/bash
set -x

DIR="$(dirname $( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd ))"

cd  $DIR/core
quarkus dev
