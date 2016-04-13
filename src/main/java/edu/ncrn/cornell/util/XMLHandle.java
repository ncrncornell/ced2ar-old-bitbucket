package edu.ncrn.cornell.util;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


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

    //TODO: replace usage with faster parse functions: https://www.w3.org/TR/xml/#NT-Name
    private static final Pattern elementWildcard = Pattern.compile("\\[\\*\\]");
    private static final Pattern attributeWildcard = Pattern.compile("\\[@(.+)=\"\\*\"\\]");

	
	/**
	 * public constructor for String xml
	 * @param x: XML string
	 * @param schemaUrl: URL string for the schema location for validation
	 */
	public XMLHandle(String x, String schemaUrl){
		//save the string for validation
		this.xml_string = x;
		//create doc w/o namespaces for easy xpath
		this.xml = loadXMLFromString(x, false);
		//set the schema url
		this.schemaURL = schemaUrl;
		
	}
	
	
	/**
	 * Returns the evalaution of the input XPath as a string. 
	 * If Xpath points to multiple nodes, concatenates all values into one string,
	 * separated by newlines.
	 *
	 * Unless there is a good reason to use it, we should probably leave as
	 * deprecated in favor of using getValueList which has a better type.
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
	 * Returns a list of values for xpath that access multiple elements
	 * @param xpath
	 * @return
	 */
	public List<String> getValueList(String xpath){
		
		try{
			NodeList nodes = getXPathList(xpath);
			//System.out.println("Number of nodes: "+nodes.getLength());
		
			ArrayList<String> items = new ArrayList<String>();
			for(int ii = 0; ii < nodes.getLength(); ii++){
				Node node = nodes.item(ii);
				items.add(node.getTextContent());
				//System.out.println(node.getTextContent());
			}
			
			return items;
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ArrayList<>();
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
	
	
	
	private NodeList getXPathList(String expression){
		
		try{
			javax.xml.xpath.XPath xp = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xp.compile(expression);
			NodeList nodes = (NodeList) expr.evaluate(xml, XPathConstants.NODESET);
			return nodes;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	
	/**
	 * Takes xml in string form and builds a DOM document for parsing/manipulation.
	 * @param xml
	 * @return
	 */
	private Document loadXMLFromString(String xml, boolean ns_aware)
	{
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			if(ns_aware)
				factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			return builder.parse(is);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean isValid(){
		Source xmlFile = null;
		try {
			URL schemaFile = new URL(schemaURL);
			System.out.println("SCHEMA URL: " + schemaURL);
			Document ns_aware_xml = loadXMLFromString(xml_string, true);
			xmlFile = new DOMSource(ns_aware_xml.getDocumentElement());
			System.out.println("ROOT ELEMENT: " + xml.getDocumentElement().getTagName());
			//System.out.println(xmlFile.toString());
			SchemaFactory schemaFactory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(schemaFile);
			Validator validator = schema.newValidator();
			validator.validate(xmlFile);
		}catch (SAXException e){
			System.out.println(xmlFile.getSystemId() + " is NOT valid");
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
     * and constructs a list of unique xpaths.
     *
     * @param xpathBuilt - pass in empty string initially
     * @param xpathRest - pass in initial, potentially non-unique xpath
     * @return
     */
    private List<String> getUniqueXPaths(String xpathBuilt, String xpathRest) {
        if (xpathRest.equals("")) {
            List<String> singleton = new ArrayList<>(1);
            singleton.add(xpathBuilt);
            return singleton;
        }

        //TODO: do we need this at this outer-scope?
        List<String> xpathValues = new ArrayList<>(200);

        Iterator<String> xpathParts = Splitter.on("/").split(xpathRest).iterator();
        String currentPathPart = xpathParts.next();
        String nextXpath = xpathBuilt + "/" + currentPathPart;

        if (xpathBuilt.equals("")) {
            // We are at the root node
            nextXpath = "/" + nextXpath;
        }

        Matcher wildElemMatcher = elementWildcard.matcher(currentPathPart);
        Matcher wildAttribMatcher = attributeWildcard.matcher(currentPathPart);
        Boolean ambiguousEndName = false;
        if (!xpathParts.hasNext()) {
            //We have traversed to the end of the input xpath, but it may
            // represent multiple element values
            xpathValues.addAll(getValueList(nextXpath));
            if (xpathValues.size() == 0) {
                return new ArrayList<>(0);
            } else if (xpathValues.size() == 1) {
                List<String> singleton = new ArrayList<>(1);
                singleton.add(nextXpath);
                return singleton;
            } else {
                ambiguousEndName = true;
            }
        }

        List<String> uniqueXpaths = new ArrayList<>(200);
        String remainingXpath = xpathRest.replace(currentPathPart, "");
        // Now we must deal with one of the following types of multiplicities
        if (wildElemMatcher.matches()) {
            //TODO: process substitutions using utility method
            return uniqueXpaths.stream().flatMap(xpath ->
                getUniqueXPaths(xpath, remainingXpath).stream()
            ).collect(Collectors.toList());
        }
        else if (wildAttribMatcher.matches()) {
            //TODO ...

        }
        else if (ambiguousEndName) {
            //TODO ...
        }
        else {
            throw new UnknownError("Logic error in getUniqueXPaths");
        }
        // Never reached
        return uniqueXpaths;
    }
}
