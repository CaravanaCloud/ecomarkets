#!/usr/bin/env bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
VERSION_TAG="$(cat "$DIR/VERSION").$(date +%Y%m%d%H%M%S)"
git tag -a $VERSION_TAG -m "Build"
git push origin $VERSION_TAG
