#!/bin/sh
set -eu
cd "$(dirname "$0")"
mkdir -p lib
archivo='lib/sqlite-jdbc-3.54.1.0.jar'
esperado='f8dce420a6016278497f4f0560f65602b9db858de141b541cf91a4644ebe4639'
checksum() {
    if command -v sha256sum >/dev/null 2>&1; then
        sha256sum "$1" | cut -d ' ' -f 1
    else
        shasum -a 256 "$1" | cut -d ' ' -f 1
    fi
}
if [ -f "$archivo" ] && [ "$(checksum "$archivo")" = "$esperado" ]; then
    exit 0
fi
temporal=$(mktemp 'lib/sqlite-jdbc.XXXXXX')
trap 'rm -f "$temporal"' 0
curl --fail --location --retry 2 --silent --show-error \
    'https://repo.maven.apache.org/maven2/org/xerial/sqlite-jdbc/3.54.1.0/sqlite-jdbc-3.54.1.0.jar' \
    --output "$temporal"
if [ "$(checksum "$temporal")" != "$esperado" ]; then
    echo 'El checksum del controlador SQLite no coincide.' >&2
    exit 1
fi
mv "$temporal" "$archivo"
