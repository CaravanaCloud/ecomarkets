#!/bin/bash

SDIR="$(cd "$(dirname "${BASH_SOURCE[0]}" )" && pwd)"
source $SDIR/utils.sh
DIR=$(dirname $SDIR)

info "Building mvn modules"
cd $DIR
mvn -fn -DskipTests install

info "Building npm modules"
cd app
npm install vite
npm install
npm run build
cd ..

VERSION=$(cat VERSION)
info "Building docker images for version $VERSION"

DOCKER_XARGS="--no-cache --progress=plain --build-arg VERSION=$VERSION"

info "Building build docker image"
docker build -f core/build.Containerfile $DOCKER_XARGS -t caravanacloud/ecomarkets-core-build:$VERSION .

info "Building core docker image"
docker build -f core/Containerfile $DOCKER_XARGS -t caravanacloud/ecomarkets-core:$VERSION .

info "Building web docker image"
docker build -f web/Containerfile $DOCKER_XARGS -t caravanacloud/ecomarkets-web:$VERSION .

info "Building api docker image"
docker build -f api/Containerfile $DOCKER_XARGS -t caravanacloud/ecomarkets-api:$VERSION .

info "Building vdn docker image"
docker build -f vdn/Containerfile $DOCKER_XARGS -t caravanacloud/ecomarkets-vdn:$VERSION .

info "Building app docker image"
docker build -f app/Containerfile $DOCKER_XARGS -t caravanacloud/ecomarkets-app:$VERSION .

info "Checking docker login"
docker login --username="$DOCKER_USERNAME" --password="$DOCKER_PASSWORD"

# check if login was successful
if [ $? -ne 0 ]; then
  error "Docker login failed"
  exit 1
fi

info "Registry login success. Pushing images..."

info "Pushing core docker images"
docker push caravanacloud/ecomarkets-core:$VERSION
docker push caravanacloud/ecomarkets-core-build:$VERSION


info "Pushing web docker image"
docker push caravanacloud/ecomarkets-web:$VERSION

info "Pushing api docker image"
docker push caravanacloud/ecomarkets-api:$VERSION

info "Pushing vdn docker image"
docker push caravanacloud/ecomarkets-vdn:$VERSION

info "Pushing app docker image"
docker push caravanacloud/ecomarkets-app:$VERSION

info done
