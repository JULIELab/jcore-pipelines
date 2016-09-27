package de.julielab.jcore.pipeline.relationextraction;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;

import de.julielab.jcore.consumer.bionlp09event.main.EventConsumer;
import de.julielab.jcore.reader.bionlp09event.main.EventReader;

public class BioSEMApplication {
    private static final String INPUT_FOLDER = "inFiles/";
    private static final String OUTPUT_FOLDER = "outFiles/";

    private static final String AE_DESCRIPTOR = "de.julielab.jcore.ae.biosem.desc.jcore-biosem-ae-bionlp-st11";
    private static final String TYPES_DESCRIPTOR = "de.julielab.jcore.types.jcore-all-types";

    CollectionReader reader;
    AnalysisEngine relationExtractor = null;
    AnalysisEngine consumer = null;

    public void run(String in, String out) {
        initializeComponents(in, out);
        processXMIDocuments();
    }

    private void initializeComponents(String in, String out) {
        try {
            // init ST Reader
            reader = CollectionReaderFactory.createReader(EventReader.class,
            		EventReader.DIRECTORY_PARAM, in,
                    EventReader.BIOEVENT_SERVICE_MODE_PARAM, false);
            // init BioSEM Event Extractor AE
            relationExtractor = AnalysisEngineFactory.createEngine(AE_DESCRIPTOR);
            // init ST Writer
            consumer = AnalysisEngineFactory.createEngine(EventConsumer.class,
            		EventConsumer.DIRECTORY_PARAM, out,
                    EventConsumer.BIOEVENT_SERVICE_MODE_PARAM, false);
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
        if (args.length > 2) {
            System.out.println("Too many arguments...");
        } else if (args.length < 2) {
            System.out.println("No in- and outputfolder declared; use default.");
            app.run(INPUT_FOLDER, OUTPUT_FOLDER);
        } else if (args.length == 2) {
            String in = args[0];
            String out = args[1];
            app.run(in, out);
        }
    }
}
