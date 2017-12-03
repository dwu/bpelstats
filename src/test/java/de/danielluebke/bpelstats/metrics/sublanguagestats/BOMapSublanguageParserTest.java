package de.danielluebke.bpelstats.metrics.sublanguagestats;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class BOMapSublanguageParserTest {

	BOMapSubLanguageParser p;

	@Test
	public void testFullBOMap() throws Exception {
		BOMapSubLanguageParser handler = parseFile(new File("src/test/resources/FullTestProc/FullTestBOMap.map"));

		assertEquals(2, handler.getComplexity());
		assertEquals(1, handler.getNumConditions());
		assertEquals(1, handler.getNumIterations());
	}

	private BOMapSubLanguageParser parseFile(File location)
			throws ParserConfigurationException, SAXException, IOException, FileNotFoundException {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		SAXParser saxParser = spf.newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();
		BOMapSubLanguageParser handler = new BOMapSubLanguageParser(location.getParentFile());
		xmlReader.setContentHandler(handler);
		xmlReader.parse(new InputSource(new FileInputStream(location)));
		return handler;
	}

}
