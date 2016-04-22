#!/usr/bin/env bash

set -x

if [ -n "$TRAVIS_TAG" ]; then
    mvn clean deploy --settings settings.xml -DskipTests -P release -Drevision=''
    exit $?
elif [ "$TRAVIS_BRANCH" = "master" ]; then
    mvn clean deploy --settings settings.xml -DskipTests
    exit $?
fi
