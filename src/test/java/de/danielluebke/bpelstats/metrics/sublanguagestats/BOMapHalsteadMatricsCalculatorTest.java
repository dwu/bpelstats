package de.danielluebke.bpelstats.metrics.sublanguagestats;

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

public class BOMapHalsteadMatricsCalculatorTest {

	@Test
	public void testFullBOMap() throws Exception {
		BOMapHalsteadMetricsCalculator calculator = parseFile(new File("src/test/resources/FullTestProc/FullTestBOMap.map"));

		HalsteadMetrics halsteadMetrics = calculator.getHalsteadMetrics();
		System.out.println("TotalNumberOfOperands: " + halsteadMetrics.getTotalNumberOfOperand());
		System.out.println("UniqueOperands: " + halsteadMetrics.getUniqueOperands());
		
		System.out.println("TotalNumberOfOperators: " +halsteadMetrics.getTotalNumberOfOperator());
		System.out.println("UniqueOperators: " + halsteadMetrics.getUniqueOperators());
	}

	private BOMapHalsteadMetricsCalculator parseFile(File location)
			throws ParserConfigurationException, SAXException, IOException, FileNotFoundException {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		SAXParser saxParser = spf.newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();
		BOMapHalsteadMetricsCalculator handler = new BOMapHalsteadMetricsCalculator();
		xmlReader.setContentHandler(handler);
		xmlReader.parse(new InputSource(new FileInputStream(location)));
		return handler;
	}

}
