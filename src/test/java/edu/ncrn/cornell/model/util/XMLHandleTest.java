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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;
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

    List<String> allDdiFilesList = Arrays.asList(allDdiFiles);

    private List<InputStream> allDdiInputs = new ArrayList<>();

    static private Map<String, String[]> allXpaths = new HashMap<>(5);

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

    @Test
    /**
     * These tests should NOT depend on specific documents or schemas
     */
    public void generateUniqueXpathsDocumentIndependent() {

        for(InputStream input: allDdiInputs) {
            String xmlString = getXmlString(input, Charsets.UTF_8);
            XMLHandle xhandle = new XMLHandle(xmlString, schema);
            //TODO: just one schema for now
            for(String xpath: allXpaths.get("ddi_2.5.1")) {

                Long numUniqXpath = xhandle.getUniqueXPaths("", xpath, Collections.emptyList()).count();
                List<String> xpathValues = xhandle.getValueList(xpath);
                if (numUniqXpath != xpathValues.size()) {
                    System.out.println("Error, discrepancy in xpath mappings and value index sizes:");
                    System.out.println(
                        xpath + ": " +
                        String.valueOf(xpathValues.size()) + " : " + String.valueOf(numUniqXpath)
                    );
                    xhandle.getUniqueXPaths("", xpath, Collections.emptyList()).forEach(System.out::println);
                }
                assertTrue(xpathValues.size() == numUniqXpath);

                //TODO: add test that each unique xpath returns a single value

                //TODO check /some/xpath count = /some/xpath[*] count for both values and unique xpaths

            }
        }
    }

    @Test
    /**
     * These are tests that depend on specific documents or schemas.
     */
    public void generateUniqueXpathsDocumentDependent() {

        int docIdx = allDdiFilesList.indexOf("/SIPP Synthetic Beta v5.1.xml");
        InputStream input = allDdiInputs.get(docIdx);

        String xmlString = getXmlString(input, Charsets.UTF_8);
        XMLHandle xhandle = new XMLHandle(xmlString, schema);

        String xpath = "/codeBook/dataDscr/var[*]/labl";
        Long numUniqXpath = xhandle.getUniqueXPaths("", xpath, Collections.emptyList()).count();
        assertTrue(numUniqXpath > 1);

        xpath = "/codeBook/stdyDscr/othrStdyMat/relMat[*]";
        numUniqXpath = xhandle.getUniqueXPaths("", xpath, Collections.emptyList()).count();
        assertTrue(4 == numUniqXpath);

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

    @BeforeAll
    static void setupDataSources() {
        //TODO: should build this from DB once test db available
        String[] ddi251 = {
            "/codeBook/stdyDscr/stdyInfo/abstract",
            "/codeBook/docDscr/citation/titlStmt/altTitl",
            "/codeBook/docDscr/citation/biblCit",
            "/codeBook/docDscr/citation/distStmt/distrbtr",
            "/codeBook/docDscr/citation/titlStmt/titl",
            "/codeBook/stdyDscr/citation/biblCit",
            "/codeBook/stdyDscr/othrStdyMat/relMat[*]",
            "/codeBook/dataDscr/var[*]/sumStat",
            "/codeBook/dataDscr/var[*]/valrng",
            "/codeBook/dataDscr/var[*]/valrng/range/@max",
            "/codeBook/dataDscr/var[*]/valrng/range/@min",
            "/codeBook/dataDscr/var[*]/txt",
            "/codeBook/fileDscr[*]",
            "/codeBook/dataDscr/var[*]/labl",
            "/codeBook/dataDscr/var[*]/@name",
            "/codeBook/dataDscr/var[*]/varFormat/@type"
        };

        allXpaths.put("ddi_2.5.1", ddi251);
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
