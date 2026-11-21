#!/bin/sh
set -eu
cd "$(dirname "$0")"
mkdir -p out
sh configurar-sqlite.sh
find src tests -name '*.java' -print0 | xargs -0 javac --release 11 -encoding UTF-8 -Xlint:all -d out
java -cp 'out:lib/*' RepositorioPedidosTest
java -cp out RepositorioPedidosTest --sin-driver
java -cp 'out:lib/*' IntegracionSwingTest
