package edu.ncrn.cornell.model.util;

import static org.junit.gen5.api.Assertions.assertEquals;
import static org.junit.gen5.api.Assertions.assertFalse;
import static org.junit.gen5.api.Assertions.assertTrue;


import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import edu.ncrn.cornell.util.XMLHandle;
import org.junit.gen5.api.AfterEach;
import org.junit.gen5.api.BeforeAll;
import org.junit.gen5.api.BeforeEach;
import org.junit.gen5.api.Test;
import org.junit.gen5.junit4.runner.JUnit5;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Brandon Barker on 4/14/2016.
 */


@RunWith(JUnit5.class)
public class XMLHandleTest {

    //TODO: Support multiple character sets (e.g. Charsets.UTF_8).

    //TODO: Support multiple schemas
    private String schema = "http://www.ncrn.cornell.edu/docs/ddi/2.5.NCRN.P/schemas/codebook.xsd";

    private final String[] allDdiFiles = {"/SIPP Synthetic Beta v5.1.xml"};
    private List<InputStream> allDdiInputs = new ArrayList<>();


    @Test
    public void createXmlHandles() {
        for(InputStream input: allDdiInputs) {
            String xmlString = "";
            try {
                xmlString = CharStreams.toString(new InputStreamReader(input, Charsets.UTF_8));
            } catch (IOException e) {
                System.out.println("Couldn't read input stream" + input.toString());
            }
            XMLHandle xhandle = new XMLHandle(xmlString, schema);
            assertTrue(xhandle.isValid());
        }
    }

    @Test
    public void emptyDoc() {
        String xmlString = "";
        XMLHandle xhandle = new XMLHandle(xmlString, schema);
        assertFalse(xhandle.isValid());
    }

    @BeforeEach
    void readTestFiles() {
        allDdiInputs = new ArrayList<>(allDdiFiles.length);

        Arrays.stream(allDdiFiles).forEach(f ->
            allDdiInputs.add(this.getClass().getResourceAsStream(f))
        );

    }

    @AfterEach
    void cleanupInputs() {
        allDdiInputs.forEach(f -> {try {
            f.close();
        } catch(IOException e) {
            System.out.println("Couldn't close" + f.toString());
        }});
        allDdiInputs.clear();
    }



}
