package de.danielluebke.bpelstats.metrics.sublanguagestats;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class XSLTHalsteadMetricsCalculatorTest {

	@Test
	public void test() throws Exception {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		SAXParser saxParser = spf.newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();
		XSLTHalsteadMetricsCalculator calculator = new XSLTHalsteadMetricsCalculator();
		xmlReader.setContentHandler(calculator);
		xmlReader.parse(new InputSource(new FileInputStream("C:/data/workspaces/terravis-av92/ch.terravis.egvt.process.egvt2.1/src/xsl/createCession.xsl")));
		
		HalsteadMetrics halsteadMetrics = calculator.getHalsteadMetrics();
		System.out.println(halsteadMetrics.getTotalNumberOfOperand());
		System.out.println(halsteadMetrics.getUniqueOperands());
		
		System.out.println(halsteadMetrics.getTotalNumberOfOperator());
		System.out.println(halsteadMetrics.getUniqueOperators());
	}

	@Test
	public void testPrintAll() throws Exception {
		File directory = new File("C:/temp/terravis-xslt");
		
		System.out.println("Filename;" + HalsteadMetrics.getCsvHeader());
		
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		SAXParser saxParser = spf.newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();
		XSLTHalsteadMetricsCalculator calculator = new XSLTHalsteadMetricsCalculator();
		xmlReader.setContentHandler(calculator);
		for(File f : directory.listFiles()) {
			xmlReader.parse(new InputSource(new FileInputStream(f)));
			
			HalsteadMetrics halsteadMetrics = calculator.getHalsteadMetrics();
			System.out.println(f + ";" + halsteadMetrics.getCsvString());
		}
	}
}
