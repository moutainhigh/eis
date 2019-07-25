package com.maicard.test;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.maicard.exception.RequiredObjectIsNullException;


public class XpathTest {
	public static void main(String[] argv){
		String fileName = "d:/Mission.xml";
		//System.out.println("Loading file:" + file);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		File file = new File(fileName);
		String content = null;
		try {
			content = FileUtils.readFileToString(file).trim().replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", "");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Document document = null;
		NodeList list = null;
		try{
			DocumentBuilder db = factory.newDocumentBuilder();
			StringReader sr = new StringReader(content);   	
			InputSource is = new InputSource(sr);   

			document = db.parse(is);
			list = document.getChildNodes().item(0).getChildNodes();
			System.out.println("顶级元素:" + list.getLength());
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(list.getLength());
		for(int i = 0; i < list.getLength(); i++){
			Node node = list.item(i);
			NamedNodeMap att = node.getAttributes();
			
			for(int j = 0 ; j < att.getLength(); j++){
			System.out.println(att.item(j).getNodeName() + "=>" + att.item(j).getTextContent());
			}
		}
	}

}
