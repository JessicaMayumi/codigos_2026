@echo off
cd /d "%~dp0"
java -Dfile.encoding=UTF-8 -cp bin br.furb.buscador.Main %*
