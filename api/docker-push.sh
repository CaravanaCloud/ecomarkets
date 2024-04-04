#!/bin/bash
set -x 

DOCKER_USERNAME="caravanacloud"
DOCKER_TAG="$DOCKER_USERNAME/$(basename $(pwd)):$(cat ../VERSION)"

docker login --username=$DOCKER_USERNAME --password=$DOCKER_PASSWORD

docker build -f src/main/docker/Containerfile --no-cache --progress=plain -t $DOCKER_TAG .

docker push $DOCKER_TAG 

echo done
