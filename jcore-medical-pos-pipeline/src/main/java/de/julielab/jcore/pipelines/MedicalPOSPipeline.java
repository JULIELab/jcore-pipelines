package de.julielab.jcore.pipelines;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;

import de.julielab.jcore.reader.file.main.FileReader;
import de.julielab.jcore.consumer.xmi.CasToXmiConsumer;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/* To successfully run the pipeline, the POM file needs to declare the imports
 * This is important to remember as for Readers/AEs that are not explicitly configured
 * here (i.e. all that are initialized by a descriptor) Eclipse (or some other IDE)
 * won't throw a warning/error message */

public class MedicalPOSPipeline {
	public static void main(String[] args) throws Exception {

		Namespace ns = initArgParser(args);
		startPipeline(ns.getString("input"), ns.getString("output"));
		
	}

	private static Namespace initArgParser(String[] args) {
		ArgumentParser parser = ArgumentParsers.newArgumentParser("Medical POS Tagging, German")
				.defaultHelp(true)
				.description("Starts a UIMA pipeline for POS tagging in the medical context."
						+ "\nIncluded processes are Sentence Splitting, Tokenizing and POS Tagging.");
		parser.addArgument("input")
			.dest("input")
			.metavar("Input-Folder");
		parser.addArgument("output")
			.dest("output")
			.metavar("Output-Folder");
		
		Namespace ns = null;
		try {
			ns = parser.parseArgs(args);
		} catch (ArgumentParserException e) {
			System.out.println("[ERROR] "+e+"\n");
			parser.printHelp();
//			parser.handleError(e);
			System.exit(1);
		}
		
		return ns;
	}

	private static void startPipeline(String infolder, String outfolder) throws ResourceInitializationException,
		InvalidXMLException, UIMAException, IOException {
		// this UIMAfit method starts a simple UIMA Pipeline with the modules
		// that are described/initialized further on
		runPipeline(
	    	// Since there is not pre-configured File Reader Project, it has to
	    	// be initialized with specific parameters; e.g. the input directory
	        createReaderDescription(FileReader.class,
	            FileReader.DIRECTORY_INPUT, infolder,
	            FileReader.FILENAME_AS_DOC_ID, true),
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
	            CasToXmiConsumer.PARAM_OUTPUTDIR, outfolder,
	            CasToXmiConsumer.PARAM_FILE_NAME_TYPE, "de.julielab.jcore.types.Header",
	            CasToXmiConsumer.PARAM_FILE_NAME_FEATURE, "docId")
		);
		
	}
}
