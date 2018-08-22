package de.julielab.jcore.pipeline.relationextraction;

import java.io.IOException;

import org.apache.commons.cli.*;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;

import de.julielab.jcore.consumer.bionlpformat.main.BioEventConsumer;
import de.julielab.jcore.reader.bionlpformat.main.BioEventReader;

public class BioSEMApplication {
    static final String PATH_DELIM = System.getProperty("file.separator");
    static final String NEWLINE = System.getProperty("line.separator");
    static final String WORKDIR = System.getProperty("user.dir");

    private static final String INPUT_FOLDER = "inFiles/";
    private static final String OUTPUT_FOLDER = "outFiles/";

    private static final String AE_09_DESCRIPTOR = "de.julielab.jcore.ae.biosem.desc.jcore-biosem-ae-bionlp-st09";
    private static final String AE_11_DESCRIPTOR = "de.julielab.jcore.ae.biosem.desc.jcore-biosem-ae-bionlp-st11";
    private static final String AE_13_DESCRIPTOR = "de.julielab.jcore.ae.biosem.desc.jcore-biosem-ae-bionlp-st13";
    private static final String TYPES_DESCRIPTOR = "de.julielab.jcore.types.jcore-all-types";

    CollectionReader reader;
    AnalysisEngine relationExtractor = null;
    AnalysisEngine consumer = null;

    private static String input_root;
    private static String output_root;
    private static String model;

    public void run() {
        initializeComponents(getInput_root(), getOutput_root(), getModel());
        processXMIDocuments();
    }

    public static String getInput_root() {
        return input_root;
    }

    public static String getOutput_root() {
        return output_root;
    }

    public static String getModel() {
        return model;
    }

    private void readCommandLineArgs(String[] args) {
        CommandLineParser cmd = new BasicParser();
        CommandLine paramParser = null;
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        options.addOption("i", "input", true, "input folder");
        options.addOption("o", "output", true, "output folder");
        options.addOption("m", "model", true, "model: {st09, st11, st13}");
        options.addOption("h", "help", false, "help");

        try {
            paramParser = cmd.parse(options, args);
            Object param;

            /** ----- Help ----- **/
            if (paramParser.hasOption("h")) {
                formatter.printHelp(this.getClass().getName(), options);
                System.exit(0);
            }

            /** ----- Model ----- **/
            param = paramParser.getOptionValue("m");
            if (param != null) {
                model = (String) param;
                if (!(getModel().equalsIgnoreCase("st09") || getModel().equalsIgnoreCase("st11")
                        || getModel().equalsIgnoreCase("st13"))) {
                    System.out.println("Model \"" + getModel() + "\" is not known." + NEWLINE +
                            "Choose one of 'st09', 'st11' or 'st13' " + "Aborting.");
                    System.exit(-1);
                }
                System.out.println("Model: " + getModel());
            } else {
                model = "st09";
                System.out.println("No model specified (-m). Defaulting to: 'st09' model.");
            }

            /** ----- Input Folder ----- **/
            param = paramParser.getOptionValue("i");
            if (param != null) {
                input_root = (String) param;
                System.out.println("Input: " + getInput_root());
            } else {
                input_root = WORKDIR + PATH_DELIM + INPUT_FOLDER;
                System.out.println("No input directory (-i) specified. Defaulting to: '" + input_root +"'");
            }

            /** ----- Output Folder ----- **/
            param = paramParser.getOptionValue("o");
            if (param != null) {
                output_root = (String) param;
                System.out.println("Output: " + getOutput_root());
            } else {
                output_root = WORKDIR + PATH_DELIM + OUTPUT_FOLDER;
                System.out.println("No output directory (-o) specified. Defaulting to: '" + output_root +"'");
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(this.getClass().getName(), options);
            System.exit(-1);
        }
    }

    private void initializeComponents(String in, String out, String model) {
        try {
            // init ST Reader
            reader = CollectionReaderFactory.createReader(BioEventReader.class,
            		BioEventReader.DIRECTORY_PARAM, in,
            		BioEventReader.BIOEVENT_SERVICE_MODE_PARAM, false);
            // init BioSEM Event Extractor AE
            switch (model) {
                case "st09":
                    relationExtractor = AnalysisEngineFactory.createEngine(AE_09_DESCRIPTOR);
                    break;
                case "st11":
                    relationExtractor = AnalysisEngineFactory.createEngine(AE_11_DESCRIPTOR);
                    break;
                case "st13":
                    relationExtractor = AnalysisEngineFactory.createEngine(AE_13_DESCRIPTOR);
                    break;
                default:
                    break;
            }
            // init ST Writer
            consumer = AnalysisEngineFactory.createEngine(BioEventConsumer.class,
            		BioEventConsumer.DIRECTORY_PARAM, out,
            		BioEventConsumer.BIOEVENT_SERVICE_MODE_PARAM, false);
        } catch (ResourceInitializationException e) {
            e.printStackTrace();
        } catch (InvalidXMLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processXMIDocuments() {
        try {
            JCas jCas = JCasFactory.createJCas(TYPES_DESCRIPTOR);
            while (reader.hasNext()) {
                reader.getNext(jCas.getCas());
                relationExtractor.process(jCas);
                consumer.process(jCas);
                jCas.reset();
            }

        } catch (UIMAException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BioSEMApplication app = new BioSEMApplication();
        app.readCommandLineArgs(args);
        app.run();
    }
}
