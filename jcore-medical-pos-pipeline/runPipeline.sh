#!/bin/bash

java_libs=target/dependency

### Check whether Maven is installed and install dependencies
if !(hash mvn 2>/dev/null); then
	echo ""
	echo "[Maven] Maven is not installed. Aborting.";
	echo "[Maven] Please install it to use the automatic project dependency copying process."
	exit 1;
else
	if [ -d "$java_libs" ]; then
		echo ""
		echo "[Maven] jar library directory \""$java_libs"\" already exists. Skipping Maven dependency copying."
		echo "[Maven] Please execute \"mvn clean\" from the command line and re-run this script if you want a clean installation."
		echo "[Maven] You can safely ignore this if you don't encounter any problems."
		echo ""
	else
		mvn dependency:copy-dependencies
	fi
fi

### compiles the java file
if [ ! -f "target/classes/de/julielab/jcore/pipeline/MedicalPOSPipeline.class" ]; then
	echo "[Maven] Compiling sources for the first time"
	mvn compile
fi

### add dependencies to java classpath
export CLASSPATH=`for i in $java_libs/*.jar; do echo -n "$i:";done;echo -n ""`

### run the pipeline with given arguments
java -cp $CLASSPATH"./target/classes" de.julielab.jcore.pipeline.MedicalPOSPipeline $*
