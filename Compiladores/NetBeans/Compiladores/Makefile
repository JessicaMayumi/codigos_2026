MVN ?= mvn
APP_NAME := compilador
MAIN_CLASS := furb.compiladores.Compiladores
JAR := target/$(APP_NAME).jar

.DEFAULT_GOAL := run

.PHONY: all build run clean help

all: run

help:
	@echo "Comandos disponíveis:"
	@echo "  make build   -> compila o projeto"
	@echo "  make run     -> compila e executa o projeto"
	@echo "  make clean   -> remove artefatos gerados"

build: $(JAR)

$(JAR):
	$(MVN) clean package -DskipTests

run: build
	java -jar $(JAR)

clean:
	$(MVN) clean
