#!/usr/bin/env bash
# Compile all source into out/ and run one demo, e.g.:
#   scripts/run.sh demo.ArrayStackDemo
# Every class you implement has its own demo, so you can run (and debug)
# the one you are working on while the others are still unfinished.
set -e
cd "$(dirname "$0")/.."
if [ $# -ne 1 ]; then
  echo "usage: scripts/run.sh <demo class>, e.g. scripts/run.sh demo.ArrayStackDemo" >&2
  exit 1
fi
javac -d out $(find src/main -name "*.java")
java -cp out "$1"
