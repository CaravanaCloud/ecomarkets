#!/bin/bash
set -x

DIR="$(dirname $( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd ))"

sleep 30
cd  $DIR/app
npm run dev
