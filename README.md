# JCoRe Pipelines

These are pre-built pipelines for various NLP aspects.

### Objective


### Requirements & Dependencies
In order to use these pipelines you need at least [JDK 7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)(Java SE Development Kit 7), [UIMA 2.6](https://uima.apache.org/index.html) & [Maven 3.0](https://maven.apache.org/). We also recommend using [Eclipse IDE for Java Developers](http://www.eclipse.org/downloads/) as your Java IDE if you're planning on using/building the pipelines programmatically(?) with Java, since it comes with the Maven Plugin. However, you're free to try it with different versions than those mentioned, but we can't make promises for a flawless functioning of our components in these cases.

### Java method
The two Pipelines `jcore-medical-pos-pipeline` & `jcore-relation-extraction-pipeline` use the `runPipeline.sh` script to download all necessary maven artifacts and compile the Java files for starting the pipeline. That is, you will only need to install Maven and Java.  
They differ in the method the Java files utilize UIMA:
* Whereas the first mentioned pipeline just creates and runs a Pipeline with UIMAfit (so you basically have only the option to modify the Pipeline like you would with a CPE approach) ...
* the second implementation would you allow to further augment your pipeline and its intermediate results with everything Java & UIMA hast to offer.  
Please see the respective Java files in `src/main/java` for the examples. If you change any of the Java files, be sure to run `mvn clean` and after that `./runPipeline.sh` again (if you didn't change any dependencies in the file and/or the `pom.xml` it should also be possible to just delete the `target/classes` folder and run `mvn compile`).

### CPE method - Plug'n'Play
The `jcore-named-entity-pipeline` employs the `cpe-pipeline.xml` to configure and run a Pipeline. You will need everything installed and properly configured that is mentioned under `Requirements & Dependencies`.  
To check if your system is correctly configured and download all dependencies run `./installComponents.sh` if no errors are encountered you're good to go to `./runPipeline.sh` after you either put your files into `data/inFiles` or configure the `cpe-pipeline.xml` file to use another input folder.
