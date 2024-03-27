#!/bin/bash

mvn clean
quarkus build -Dquarkus.profile=prod -DskipTests
