#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
VERSION=$(cat "VERSION")

# Build API (Quarkus for AWS Lambda)
mvn -fn -B -ntp -f ecomarkets clean package

IMAGE_TAG="caravanacloud/ecomarkets-app:$VERSION"
# Build App (Skeleton)
podman build \
    -f ecomarkets-app/Containerfile \
    --no-cache ecomarkets-app \
    -t "$IMAGE_TAG"

# Build Infrastructure as Code (AWS CloudFormation)
mkdir dist
zip -r dist/ecomarkets-cfn.zip ecomarkets-cfn/
