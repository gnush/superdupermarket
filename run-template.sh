#!/bin/sh

LOCATION=DIR
NAME=BASENAME
SUFFIX=VERSION

java --enable-native-access=ALL-UNNAMED -jar $LOCATION/$NAME-$SUFFIX.jar "$@"