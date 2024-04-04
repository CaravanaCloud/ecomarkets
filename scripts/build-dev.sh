#!/bin/bash

SDIR="$(cd "$(dirname "${BASH_SOURCE[0]}" )" && pwd)"
source $SDIR/utils.sh
DIR=$(dirname $SDIR)

info "Building mvn modules"
cd $DIR
mvn -fn -DskipTests install

info "Building npm modules"
cd app
npm install
npm run build
cd ..

VERSION=$(cat VERSION)
info "Building docker images for version $VERSION"

info "Building web docker image"
docker build -f web/Containerfile --no-cache --progress=plain -t caravanacloud/ecomarkets-web:$VERSION .

info "Building api docker image"
info "Building app docker image"
info "Building vdn docker image"


info "Checking docker login"
docker login --username="$DOCKER_USERNAME" --password="$DOCKER_PASSWORD"

info "Pushing web docker image"
docker push caravanacloud/ecomarkets-web:$(cat VERSION)

echo done
