package edu.ncrn.cornell.util;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



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
	
	/**
	 * public constructor
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
		//check validity
		if(!isValid()){
			System.out.println("XML NOT VALID");
		}else{
			System.out.println("XML IS VALID");
		}
		
	}
	
	/**
	 * Returns the evalaution of the input XPath as a string. 
	 * If Xpath points to multiple nodes, concatenates all values into one string,
	 * separated by newlines.
	 * @param expression - the xpath
	 * @return xml value
	 */
	public String getXPathSingleValue(String expression){
		System.out.println("attempting to evaluate expath expression: "+expression);
		try{
			XPath xp = XPathFactory.newInstance().newXPath();
			XPathExpression expr = xp.compile(expression);
			String value = (String) expr.evaluate(xml, XPathConstants.STRING);
			NodeList nodes = (NodeList) expr.evaluate(xml, XPathConstants.NODESET);
			System.out.println("node count: "+nodes.getLength());
			//This conditional is a hack. Not robust at all for different xpaths.
			if(nodes.getLength() > 1){
				String builder = "";
				for(int i = 0; i < nodes.getLength(); i++){
					Node n = nodes.item(i);
					builder = builder + n.getTextContent() + "\n";
				}
				value = builder;
			}
			//System.out.println("node: "+value);
			return value;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
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
			for(int i = 0; i < nodes.getLength(); i++){
				Node node = nodes.item(i);
				items.add(node.getTextContent());
				//System.out.println(node.getTextContent());
			}
			
			return items;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
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
	
	private boolean isValid(){
		Source xmlFile = null;
		try {
			URL schemaFile = new URL(this.schemaURL);
			System.out.println("SCHEMA URL: "+this.schemaURL);
			Document ns_aware_xml = loadXMLFromString(this.xml_string, true);
			xmlFile = new DOMSource(ns_aware_xml.getDocumentElement());
			System.out.println("ROOT ELEMENT: "+this.xml.getDocumentElement().getTagName());
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
}
