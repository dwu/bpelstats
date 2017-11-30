package de.danielluebke.bpelstats.metrics.sublanguagestats;

import java.util.Arrays;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import es.uca.webservices.xquery.parser.util.XQueryParsingException;

public class XSLTHalsteadMetricsCalculator extends DefaultHandler implements IHalsteadMetricsCalculator {
 
	private static final List<String> XSLT_EXCLUDED_ELEMENTS = Arrays.asList("import", "stylesheet", "output");
	
	private final String XSLT_NAMESPACE = "http://www.w3.org/1999/XSL/Transform";

	private HalsteadMetrics metrics = new HalsteadMetrics();
	private XPathHalsteadMetricsCalculator xpathHalsteadMetricsCalculator = new XPathHalsteadMetricsCalculator();

	@Override
	public void startDocument() throws SAXException {
		metrics.clear();
		xpathHalsteadMetricsCalculator.clear();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		if (XSLT_NAMESPACE.equals(uri)) {
			if(!XSLT_EXCLUDED_ELEMENTS.contains(localName)) {
				metrics.addOperator("xsl:" + localName);
			} else {
				return;
			}
			for(int i = 0; i < attributes.getLength(); i++) {
				String attributeQName = attributes.getQName(i);
				metrics.addOperator(attributeQName);
				if("test".equals(attributeQName) || "match".equals(attributeQName) || "select".equals(attributeQName)) {
					try {
						xpathHalsteadMetricsCalculator.parse(attributes.getValue(i));
					} catch (XQueryParsingException e) {
						throw new SAXException(e);
					}
				} else if(
						"name".equals(attributeQName) && 
						("variable".equals(localName) || "param".equals("localName"))
						) {
					metrics.addOperand("$" + attributes.getValue(i));
				} else {
					metrics.addOperator(attributes.getValue(i));
				}
			}
		} else {
			metrics.addOperand("{" + uri + "}" + localName);
			for(int i = 0; i < attributes.getLength(); i++) {
				metrics.addOperand(attributes.getQName(i));
				metrics.addOperand(attributes.getValue(i));
			}
		}
	}


	@Override
	public void endDocument() throws SAXException {
		HalsteadMetrics xpathMetrics = xpathHalsteadMetricsCalculator.getHalsteadMetrics();
		for(String operand : xpathMetrics.getUniqueOperands()) {
			metrics.addOperand(operand, xpathMetrics.getTotalNumberOfOperand(operand));
		}
		for(String operator : xpathMetrics.getUniqueOperators()) {
			metrics.addOperator(operator, xpathMetrics.getTotalNumberOfOperator(operator));
		}
	}
	
	@Override
	public HalsteadMetrics getHalsteadMetrics() {
		metrics.finalize();
		return metrics;
	}
}
