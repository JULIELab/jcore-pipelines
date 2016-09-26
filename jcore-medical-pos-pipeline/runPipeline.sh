#!/bin/bash

java_libs=target/dependency

export CLASSPATH=`for i in $java_libs/*.jar; do echo -n "$i:";done;echo -n ""`

java -cp $CLASSPATH de.julielab.jcore.pipelines.MedicalPosPipeline $*
