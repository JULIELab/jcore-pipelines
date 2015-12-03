#!/bin/bash

java_libs=target/dependency

export CLASSPATH=`for i in $java_libs/*.jar; do echo -n "$i:";done;echo -n ""`

$UIMA_HOME/bin/runCPE.sh cpe-pipeline.xml
