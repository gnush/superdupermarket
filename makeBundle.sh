#!/bin/sh
##############################################################
# Create a bundle zip containing the jar file and run.sh      #
#                                                             #
# USAGE                                                       #
# ./makeBundle.sh NAME VERSION PROJECT_DIR                    #
# To create a bundle from PROJECT_DIR/target/NAME-VERSION.jar #
###############################################################

if [ ! $# -eq 3 ]; then
  echo Not enough arguments supplied
  echo USAGE
  echo ./makeBundle.sh NAME VERSION PROJECT_DIR
  echo To create a bundle from PROJECT_DIR/target/NAME-VERSION.jar
  exit 1
fi

if [ ! -d "$3" ]; then
  echo no target dir specified
  exit 2
fi

BASENAME="$1"
VERSION="$2"
PROJECT_DIR="$3"

JAR="$PROJECT_DIR/target/$BASENAME-$VERSION.jar"

if [ ! -f "$JAR" ]; then
  echo "$JAR" does not exist
  exit 3
fi

TEMP_DIR=$(mktemp -d)

# Copy jar
cp "$JAR" "$TEMP_DIR"

# Copy Data
DATA_DIR=data
mkdir -p "$TEMP_DIR/$DATA_DIR"
cp "$PROJECT_DIR/$DATA_DIR"/*.csv "$TEMP_DIR/$DATA_DIR"
cp "$PROJECT_DIR/$DATA_DIR"/*.sqlite3 "$TEMP_DIR/$DATA_DIR"

# Instantiate run template
RUN="run"
RUN_SH="$TEMP_DIR/$RUN.sh"
cp "$PROJECT_DIR/$RUN-template.sh" "$RUN_SH"
chmod +x "$RUN_SH"
sed -i "s/DIR/./" "$RUN_SH"
sed -i "s/BASENAME/$BASENAME/" "$RUN_SH"
sed -i "s/VERSION/$VERSION/" "$RUN_SH"

# Instantiate populate database template
POP="populateDatabase"
POP_SH="$TEMP_DIR/$POP.sh"
cp "$PROJECT_DIR/$POP-template.sh" "$POP_SH"
chmod +x "$POP_SH"
sed -i "s/DIR/./" "$POP_SH"
sed -i "s/BASENAME/$BASENAME/" "$POP_SH"
sed -i "s/VERSION/$VERSION/" "$POP_SH"

CALL_SITE=$(pwd)
(
  if ! cd "$TEMP_DIR"; then
    echo "Couldn't change into $TEMP_DIR"
    exit 4
  fi
  zip -q -r "$CALL_SITE/$BASENAME-$VERSION.zip" .
)

# Clean
rm -r "$TEMP_DIR"