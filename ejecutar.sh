#!/bin/sh
set -eu
cd "$(dirname "$0")"
sh configurar-sqlite.sh
mkdir -p out
find src -name '*.java' -print0 | xargs -0 javac --release 11 -encoding UTF-8 -d out
java -cp 'out:lib/*' Main
