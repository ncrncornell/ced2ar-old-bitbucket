package edu.ncrn.cornell.util;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import javax.xml.xpath.*;
import javax.xml.parsers.*;
import java.io.*;
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
	
	public XMLHandle(String x){
		this.xml = loadXMLFromString(x);
		//TODO: Validate!!
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
			//javax.xml.xpath.XPath xp = XPathFactory.newInstance().newXPath();
			//XPathExpression varExpr = xp.compile(xpath);
			//NodeList nodes = (NodeList) varExpr.evaluate(xml, XPathConstants.NODESET);
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
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			return builder.parse(is);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
