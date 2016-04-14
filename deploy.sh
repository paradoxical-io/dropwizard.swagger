#!/usr/bin/env bash

set -x

mvn deploy --settings settings.xml -DskipTests
