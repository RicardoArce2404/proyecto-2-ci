#!/usr/bin/env bash
cd "$(dirname "$0")"
java -jar "../lib/jflex-full-1.9.1.jar" \
     -d "../build/" \
     "../src/Lexer.flex"
