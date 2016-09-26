package de.julielab.jcore.pipelines;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

import de.julielab.jcore.reader.file.main.FileReader;
import de.julielab.jcore.consumer.xmi.CasToXmiConsumer;

/* To successfully run the pipeline, the POM file needs to declare the imports
 * This is important to remember as Readers/AEs that are not explicitly configured
 * here (i.e. all that are initialized by a descriptor */

public class MedicalPOSPipeline {
	public static void main(String[] args) throws Exception {
		// this UIMAfit method starts a simple UIMA Pipeline with the modules
		// that are described/initialized further on
		runPipeline(
	    	// Since there is not pre-configured File Reader Project, it has to
	    	// be initialized with specific parameters; e.g. the input directory
	        createReaderDescription(FileReader.class,
	            FileReader.DIRECTORY_INPUT, "tmp/wiki-bsp.txt"),
	        // For the Analysis Engines there are pre-configured projects that
	        // load the respective model; so all there needs to be done, is to
	        // look up the descriptor path in their READMEs
	        // (as you can see, the path' are to some extend self-explaining and
	        // can be inferred
	        createEngineDescription("de.julielab.jcore.ae.jsbd.desc.jcore-jsbd-ae-medical-german"),
	        createEngineDescription("de.julielab.jcore.ae.jtbd.desc.jcore-jtbd-ae-medical-german"),
	        createEngineDescription("de.julielab.jcore.ae.jpos.desc.jcore-jpos-ae-medical-german"),
	        // As with the reader, the writer is not pre-configured and you need
	        // to initialize it with some parameters
	        createEngineDescription(CasToXmiConsumer.class,
	            CasToXmiConsumer.PARAM_OUTPUTDIR, "tmp/")
		);
	}
}
