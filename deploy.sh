#!/usr/bin/env bash

set -x

mvn clean deploy --settings settings.xml -DskipTests
