#!/usr/bin/env bash
cd "$(dirname "$0")"
java -jar "../lib/java-cup-11b.jar" \
     -parser parser \
     -symbols sym \
     -destdir "../build/" \
     "../src/Parser.cup"
