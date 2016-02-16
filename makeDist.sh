#!/bin/bash

set -e

mkdir -p dist

if [ -d dist ]; then
    rm -rf dist/*
fi


mvn clean assembly:assembly
cp target/galen-ide-jar-with-dependencies.jar dist/galen-ide.jar
cp fordist/galen-ide dist/.
cp fordist/install.sh dist/.

