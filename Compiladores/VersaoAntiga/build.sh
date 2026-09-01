#!/usr/bin/env bash
# Compila a interface do compilador e gera dist/interface.jar
set -euo pipefail
cd "$(dirname "$0")"

rm -rf build dist
mkdir -p build dist

javac --release 17 -encoding UTF-8 -d build $(find src -name '*.java')
jar --create --file dist/interface.jar --main-class compilador.Principal -C build .

echo "gerado: dist/interface.jar"
echo "execute com: java -jar dist/interface.jar"
