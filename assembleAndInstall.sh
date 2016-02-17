#!/bin/bash

set -e
./makeDist.sh

cd dist

sudo ./install.sh
