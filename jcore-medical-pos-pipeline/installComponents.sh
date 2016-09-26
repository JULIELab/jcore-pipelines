#!/bin/bash

lib_dir=target/dependency

### Check whether Maven is installed
if !(hash mvn 2>/dev/null); then
	echo ""
	echo "[Maven] Maven is not installed. Aborting.";
	echo "[Maven] Please install it to use the automatic project dependency copying process."
	exit 1;
else
	if [ -d "$lib_dir" ]; then
		echo ""
		echo "[Maven] jar library directory \""$lib_dir"\" already exists. Skipping Maven dependency copying."
		echo "[Maven] Please execute \"mvn clean\" from the command line and re-run this script if you want a clean installation."
	else
		mvn dependency:copy-dependencies
	fi
fi

### Check whether UIMA is installed and the environment variable is set
if [ -z "$UIMA_HOME" ]; then
	echo ""
	echo "[UIMA] UIMA is either not installed or the environment variable is not set. Aborting.";
	echo "[UIMA] Please make sure you have UIMA installed if you want to use the prebuilt CPEs."
	echo ""
	exit 1;
else
	echo "[UIMA] Your UIMA environment variable is \""$UIMA_HOME"\""
	if [ ! -d "$UIMA_HOME" ]; then
		echo ""
		echo "[UIMA] \""$UIMA_HOME"\" seems to be no valid directory. Aborting."
		echo ""
		exit 1;
	fi
	echo ""
fi
