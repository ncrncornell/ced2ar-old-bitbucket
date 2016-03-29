package edu.ncrn.cornell.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.ncrn.cornell.model.dao.SchemaDao;

import javax.xml.xpath.*;
import javax.xml.parsers.*;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/**
 * Class to handle xml manipulation. Probably will need to be broken into subclasses once more fn'ality is added.
 * TODO: needs to handle retreiving one vs multiple items much better.
 * @author kylebrumsted
 *
 */
public class XMLHandle {

	private Document xml;
	//TODO:add schema stuff
	private String schemaURL;
	
	public XMLHandle(String x, String schemaUrl){
		this.xml = loadXMLFromString(x);
		this.schemaURL = schemaUrl;
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
			System.out.println("node: "+value);
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
			System.out.println("Number of nodes: "+nodes.getLength());
		
			ArrayList<String> items = new ArrayList<String>();
			for(int i = 0; i < nodes.getLength(); i++){
				Node node = nodes.item(i);
				items.add(node.getTextContent());
				System.out.println(node.getTextContent());
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
		return false;
	}
	
	/**
	 * Replaces an already existing value
	 * @param XPath
	 * @param newValue
	 * @return
	 */
	public boolean replace(String XPath, String newValue){
		return false;
	}
	
	/**
	 * Deletes the value/contents/children specified by XPath 
	 * @param XPath
	 * @return
	 */
	public boolean delete(String XPath){
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
	private Document loadXMLFromString(String xml)
	{
		//System.out.println("[XMLHandle]:: Attempting to build document from string: "+xml.substring(0, 100));
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			//factory.setNamespaceAware(true);
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
			xmlFile = new DOMSource(this.xml.getDocumentElement());
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
