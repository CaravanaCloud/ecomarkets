#!/bin/bash

mvn clean verify -Dquarkus.profile=prod --fail-never
