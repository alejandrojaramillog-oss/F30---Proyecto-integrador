#!/bin/sh
set -eu
cd "$(dirname "$0")"
mkdir -p out
find src tests -name '*.java' -print0 | xargs -0 javac --release 11 -encoding UTF-8 -Xlint:all -d out
java -cp out ServicioDescuentosTest
java -cp out Main
