package edu.ncrn.cornell.model.util;


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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.gen5.api.Assertions.*;

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
        //TODO: Eventually need to switch to parameterized tests, or devise a means
        //TODO: to run the same test for multiple input data to avoid either recomputing
        //TODO: multiple stages or storing output from multiple stages in memory.
        for(InputStream input: allDdiInputs) {
            String xmlString = getXmlString(input, Charsets.UTF_8);
            XMLHandle xhandle = new XMLHandle(xmlString, schema);
            assertTrue(xhandle.isValid());
        }
    }

    //TODO: should be restructured to be document-dependent; not every
    //TODO: document has the same data
    @Test
    public void generateUniqueXpaths() {
        //Some possibilities from the DB:
        //        "/codeBook/stdyDscr/stdyInfo/abstract"
        //        "/codeBook/docDscr/citation/titlStmt/altTitl"
        //        "/codeBook/docDscr/citation/biblCit"
        //        "/codeBook/docDscr/citation/distStmt/distrbtr"
        //        "/codeBook/docDscr/citation/titlStmt/titl"
        //        "/codeBook/stdyDscr/citation/biblCit"
        //        "/codeBook/stdyDscr/otherStdMat/relMat[*]"
        //        "/codeBook/dataDscr/var[*]/sumStat"
        //        "/codeBook/dataDscr/var[*]/valrng"
        //        "/codeBook/dataDscr/var[*]/valrng/range/@max"
        //        "/codeBook/dataDscr/var[*]/valrng/range/@min"
        //        "/codeBook/dataDscr/var[*]/txt"
        //        "/codeBook/fileDscr[*]"
        //        "/codeBook/dataDscr/var[*]/labl"
        //        "/codeBook/dataDscr/var[*]/@name"
        //        "/codeBook/dataDscr/var[*]/varFormat/@type"

        for(InputStream input: allDdiInputs) {
            String xmlString = getXmlString(input, Charsets.UTF_8);
            XMLHandle xhandle = new XMLHandle(xmlString, schema);
            Long numXpath = xhandle.getUniqueXPaths("", "/codeBook/dataDscr/var[*]/labl").count();
            assertTrue(numXpath > 1);
            //DEBUG
            //xhandle.getUniqueXPaths("", "/codeBook/dataDscr/var[*]/labl").forEach(x -> System.out.println(x));

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

    public static String getXmlString(InputStream input, Charset charset) {
        String xmlString = "";
        try {
            xmlString = CharStreams.toString(new InputStreamReader(input, charset));
        } catch (IOException e) {
            System.out.println("Couldn't read input stream" + input.toString());
        }
        return xmlString;
    }



}
