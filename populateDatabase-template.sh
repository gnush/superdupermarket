#!/bin/sh

LOCATION=DIR
NAME=BASENAME
SUFFIX=VERSION

java --enable-native-access=ALL-UNNAMED -cp $LOCATION/$NAME-$SUFFIX.jar io.github.gnush.PopulateDatabase "$@"