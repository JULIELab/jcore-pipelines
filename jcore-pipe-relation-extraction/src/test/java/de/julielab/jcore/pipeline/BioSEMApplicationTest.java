package de.julielab.jcore.pipeline;

import org.junit.Test;

import de.julielab.jcore.pipeline.relationextraction.BioSEMApplication;

public class BioSEMApplicationTest {

    @Test
    public void test() {
        BioSEMApplication app = new BioSEMApplication();
        app.main(new String[] {});
    }
}
