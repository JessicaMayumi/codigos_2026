@echo off
REM Compila e executa o buscador num passo so.
REM   run.bat             -> GUI
REM   run.bat --console   -> forca modo console
cd /d "%~dp0"

if not exist bin (
    echo Compilando...
    mkdir bin
    dir /s /b src\*.java > sources.tmp
    javac -encoding UTF-8 -d bin @sources.tmp
    del sources.tmp
)

java -Dfile.encoding=UTF-8 -cp bin Main %*
