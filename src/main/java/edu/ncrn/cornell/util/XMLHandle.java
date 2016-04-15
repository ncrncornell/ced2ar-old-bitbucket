package edu.ncrn.cornell.util;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentFilter;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


import javax.xml.xpath.*;
import javax.xml.parsers.*;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.*;
import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * Class to handle xml manipulation. Probably will need to be broken into subclasses once more fn'ality is added.
 * TODO: needs to handle retreiving one vs multiple items much better.
 * @author kylebrumsted
 *
 */
public class XMLHandle {

	/**
	 * private attributes
	 */
	private String xml_string;
	private Document xml;
	private String schemaURL;

    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder builder;

    //TODO: replace usage with faster parse functions: https://www.w3.org/TR/xml/#NT-Name
    private static final Pattern elementWildcard = Pattern.compile("\\[\\*\\]");
    private static final Pattern attributeWildcard = Pattern.compile(
        "\\[@(.+)\\s*=\\s*[\"'](\\*)[\"']\\]"
    );
    private static final Pattern wildcard = Pattern.compile("\\*");



    /**
	 * public constructor for String xml
	 * @param x: XML string
	 * @param schemaUrl: URL string for the schema location for validation
	 */
	public XMLHandle(String x, String schemaUrl){
        //save the string for validation
		this.xml_string = x;
		//create doc w/o namespaces for easy xpath
		this.xml = loadXMLFromString(x, false).orElse(builder.newDocument());
		//set the schema url
		this.schemaURL = schemaUrl;
		
	}
	
	
	/**
	 * Returns the evalaution of the input XPath as a string. 
	 * If Xpath points to multiple nodes, concatenates all values into one string,
	 * separated by newlines.
	 *
	 * Unless there is a good reason to use it, we should probably leave as
	 * deprecated in favor of using getValueList or getUniqueValue.
	 *
	 * @param xpath - the xpath
	 * @return xml value
	 */
	@Deprecated
	public String getXPathSingleValue(String xpath){
        List<String> values = getValueList(xpath);
        return Joiner.on("\n").skipNulls().join(values);
	}

    /**
     *
     * @param xpath An xpath without any wildcards (including
     *              implicit wildcards). Throws an exception if
     *
     * @return A value corresponding to the given xpath, or nothing
     * if no value is found.
     */
    public Optional<String> getUniqueValue(String xpath){
        Optional<NodeList> nodes = getXPathList(xpath);

        if (nodes.isPresent()) {
            if (nodes.get().getLength() > 1) {
                throw new IllegalArgumentException("Not a unique xpath in getUniqueValue");
            }
            if (nodes.get().getLength() == 1) {
                Node node = nodes.get().item(0);
                return Optional.ofNullable(node.getTextContent());
            }
        }
        return Optional.empty();
    }
	
	/**
	 * Returns a list of values for xpath that access multiple elements
	 * @param xpath
	 * @return
	 */
	public List<String> getValueList(String xpath){
        Optional<NodeList> nodes = getXPathList(xpath);
        ArrayList<String> items = new ArrayList<String>();

        if (nodes.isPresent()) {
            for (int ii = 0; ii < nodes.get().getLength(); ii++) {
                Node node = nodes.get().item(ii);
                items.add(node.getTextContent());
            }
        }
        return items;
	}
	
	/**
	 * Function to insert a new value into a codebook.
	 * If value already exists at specified XPATH, either do nothing or replace,
	 * depending on value of replace argument.
	 * Returns boolean; whether or not insert was successful.
	 * @param XPath
	 * @param newValue
	 * @param replace
	 * @return
	 */
	public boolean insert(String XPath, String newValue, boolean replace){
		//TODO: implement fn
		return false;
	}
	
	/**
	 * Replaces an already existing value
	 * @param XPath
	 * @param newValue
	 * @return
	 */
	public boolean replace(String XPath, String newValue){
		//TODO: implement fn
		return false;
	}
	
	/**
	 * Deletes the value/contents/children specified by XPath 
	 * @param XPath
	 * @return
	 */
	public boolean delete(String XPath){
		//TODO: implement fn
		return false;
	}
	
	
	
	private Optional<NodeList> getXPathList(String expression){
		
		try{
			javax.xml.xpath.XPath xp = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xp.compile(expression);
			NodeList nodes =  (NodeList) expr.evaluate(xml, XPathConstants.NODESET);
			return Optional.ofNullable(nodes);
		} catch(Exception e){
			e.printStackTrace();
		}
		return Optional.empty();
	}
	
	
	/**
	 * Takes xml in string form and builds a DOM document for parsing/manipulation.
	 * @param xml
	 * @return
	 */
	private Optional<Document> loadXMLFromString(String xml, boolean ns_aware)
	{
		try{
			if(ns_aware)
				factory.setNamespaceAware(true);
			builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			return Optional.ofNullable(builder.parse(is));
		}catch(Exception e){
            //TODO: Log by some means; shouldn't log in testing:
			//e.printStackTrace();
		}
		return Optional.empty();
	}

    /**
     * Validates the instance's schema document against the instance's
     * schema.
     *
     * @return
     */
	public boolean isValid(){
		Source xmlFile = null;
		try {
			URL schemaFile = new URL(schemaURL);
			System.out.println("SCHEMA URL: " + schemaURL);
			Optional<Document> ns_aware_xml = loadXMLFromString(xml_string, true);
            if (!ns_aware_xml.isPresent()) {
                return false;
            }
            xmlFile = new DOMSource(ns_aware_xml.get().getDocumentElement());
            System.out.println("ROOT ELEMENT: " + xml.getDocumentElement().getTagName());
			//System.out.println(xmlFile.toString());
			SchemaFactory schemaFactory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(schemaFile);
			Validator validator = schema.newValidator();
			validator.validate(xmlFile);
		}catch (SAXException e){
			System.out.println(
                    (xmlFile != null ? xmlFile.getSystemId() :
                            "file with null id") + " is NOT valid"
            );
			System.out.println("Reason: " + e.getLocalizedMessage());
			return false;
		}catch (Exception e){
			System.out.println("Validation exception; malformed URL or IO");
			e.printStackTrace();
			return false;
		}
		return true;
	}

    /**
     * For the given xpath, iterates recursively over each wildcard or element list
     * and constructs a list of unique xpaths. This implementation is currently not
     * tail-recursive (may be possible to change this, but Java 8 doesn't have TCO
     * anyway).
     *
     * @param xpathBuilt - pass in empty string initially
     * @param xpathRest - pass in initial, potentially non-unique xpath
     * @return
     */
    public Stream<String> getUniqueXPaths(String xpathBuilt, String xpathRest) {
        if (xpathRest.equals("")) {
            List<String> singleton = new ArrayList<>(1);
            singleton.add(xpathBuilt);
            return singleton.stream();
        }

        List<String> xpathValues = new ArrayList<>(200);

        Iterator<String> xpathParts = Splitter.on("/").omitEmptyStrings()
			.split(xpathRest).iterator();

        if (!xpathParts.hasNext()){
            System.out.println("Logic error; xpathRest is " + xpathRest + " but no split paths");
            System.out.println("xpathBuilt is " + xpathBuilt);
        }
        String currentPathPart = xpathParts.next();

        final String nextXpath = xpathBuilt + "/" + currentPathPart;

        Matcher wildElemMatcher = elementWildcard.matcher(currentPathPart);
        Matcher wildAttribMatcher = attributeWildcard.matcher(currentPathPart);
        Boolean ambiguousEndName = false;
        if (!xpathParts.hasNext()) {
            //We have traversed to the end of the input xpath, but it may
            // represent multiple element values
            xpathValues.addAll(getValueList(nextXpath));
            if (xpathValues.size() == 0) {
                List<String> singleton = new ArrayList<>(0);
                return  singleton.stream();
            }
            else if (xpathValues.size() == 1) {
                List<String> singleton = new ArrayList<>(1);
                singleton.add(nextXpath);
                return singleton.stream();
            }
            else {
                ambiguousEndName = true;
            }
        }

        List<String> uniqueXpaths = new ArrayList<>(200);
        String remainingXpath = xpathRest.replace("/" + currentPathPart, "");
        // Now we must deal with one of the following types of multiplicities
        //TODO: need good tests for each of the following
        if (wildElemMatcher.find(0) && !ambiguousEndName) {
            xpathValues.addAll(getValueList(nextXpath));
            uniqueXpaths.addAll(IntStream.rangeClosed(1, xpathValues.size()).mapToObj(ii ->
                xpathBuilt + "/" +
                wildElemMatcher.replaceFirst("[" + Integer.toString(ii) + "]")
            ).collect(Collectors.toList()));
            return uniqueXpaths.stream().flatMap(xpath ->
                getUniqueXPaths(xpath, remainingXpath)
            );
        }
        else if (wildAttribMatcher.find(0)) {
            Matcher attrValueMatcher = wildcard.matcher(currentPathPart);
            String attribXpath = xpathBuilt + "/@" + wildAttribMatcher.group(0);
            List<String> attribValues = getValueList(attribXpath);
            uniqueXpaths.addAll(attribValues.stream().map(ii ->
                xpathBuilt + "/" + attrValueMatcher.replaceFirst(ii)
            ).collect(Collectors.toList()));
            return uniqueXpaths.stream().flatMap(xpath ->
                getUniqueXPaths(xpath, remainingXpath)
            );
        }
        else if (ambiguousEndName) {
            //xpathValues already obtained
            uniqueXpaths.addAll(IntStream.rangeClosed(1, xpathValues.size()).mapToObj(ii ->
                nextXpath + "[" + Integer.toString(ii) + "]"
            ).collect(Collectors.toList()));
            return uniqueXpaths.stream().flatMap(xpath ->
                getUniqueXPaths(xpath, remainingXpath)
            );
        }
        else {
            //Nothing to do at this level, moving on:
            return getUniqueXPaths(nextXpath, remainingXpath);
        }
        // Never reached
    }
}
