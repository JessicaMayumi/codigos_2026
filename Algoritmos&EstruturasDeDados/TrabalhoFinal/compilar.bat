@echo off
REM Compila todos os fontes Java em src\ para bin\
cd /d "%~dp0"
if exist bin rmdir /s /q bin
mkdir bin
dir /s /b src\*.java > sources.tmp
javac -encoding UTF-8 -d bin @sources.tmp
del sources.tmp
echo Compilacao concluida em %cd%\bin
