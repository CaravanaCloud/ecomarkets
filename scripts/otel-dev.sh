#!/bin/bash

docker compose -f docker-compose.otel.yaml down
docker compose -f docker-compose.otel.yaml up

