#!/usr/bin/env bash
# Executa o buscador. Repassa todos os argumentos para o Main:
#   ./executar.sh             → GUI (ou console se for headless)
#   ./executar.sh --console   → força modo console
#   ./executar.sh --gui       → força modo gráfico
set -euo pipefail
cd "$(dirname "$0")"
java -Dfile.encoding=UTF-8 -cp bin br.furb.buscador.Main "$@"
