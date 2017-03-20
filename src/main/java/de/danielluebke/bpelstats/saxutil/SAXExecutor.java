package de.danielluebke.bpelstats.saxutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class SAXExecutor {

	private SAXExecutor() {
	}
	
	public static <H extends DefaultHandler> H execute(InputStream inputstream, H handler) throws IOException, SAXException, ParserConfigurationException {
		SAXParserFactory spf = SAXParserFactory.newInstance();
	    spf.setNamespaceAware(true);
	    SAXParser saxParser = spf.newSAXParser();
	    XMLReader xmlReader = saxParser.getXMLReader();
		xmlReader.setContentHandler(handler);
	    xmlReader.parse(new InputSource(inputstream));
	    return handler;	
	}
	
	public static <H extends DefaultHandler> H execute(String xmlFileName, H handler) throws IOException, SAXException, ParserConfigurationException {
		return execute(new FileInputStream(new File(xmlFileName)), handler);
	}
}
