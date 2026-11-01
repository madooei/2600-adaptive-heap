#!/usr/bin/env bash
# Compile all source and tests into out/, then run the JUnit tests.
# With no argument, run every test JUnit finds. With one argument, run only
# that test class, e.g.:
#   scripts/test.sh adaptiveheap.IndexedArrayHeapTest
# This script is the single place the classpath lives so you never have to
# type it by hand.
set -e
cd "$(dirname "$0")/.."
javac -d out -cp "lib/*" $(find src/main src/test -name "*.java")
if [ $# -eq 0 ]; then
  java -jar lib/junit-platform-console-standalone-6.1.0.jar execute \
    -cp out --scan-classpath
else
  java -jar lib/junit-platform-console-standalone-6.1.0.jar execute \
    -cp out --select-class "$1"
fi
