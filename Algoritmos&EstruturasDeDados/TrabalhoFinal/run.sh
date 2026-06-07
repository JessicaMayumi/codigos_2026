#!/usr/bin/env bash
# Compila (apenas se precisar) e executa o buscador num passo so.
#   ./run.sh             -> GUI (ou console se for headless)
#   ./run.sh --console   -> forca modo console
set -euo pipefail
cd "$(dirname "$0")"

# Se o java/javac nao estiver no PATH, tenta achar um JDK conhecido.
if ! command -v javac >/dev/null 2>&1; then
    for candidato in "$HOME"/.jdks/* /usr/lib/jvm/*; do
        if [ -x "$candidato/bin/javac" ]; then
            export PATH="$candidato/bin:$PATH"
            break
        fi
    done
fi

# Recompila so se algum .java for mais novo que o bin/ (ou se bin/ nao existir).
precisa_compilar=false
if [ ! -d bin ]; then
    precisa_compilar=true
elif [ -n "$(find src -name '*.java' -newer bin -print -quit)" ]; then
    precisa_compilar=true
fi

if [ "$precisa_compilar" = true ]; then
    echo "Compilando..."
    rm -rf bin
    mkdir -p bin
    find src -name "*.java" -print0 | xargs -0 javac -encoding UTF-8 -d bin
fi

java -Dfile.encoding=UTF-8 -cp bin Main "$@"
