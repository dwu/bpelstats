package de.danielluebke.bpelstats.metrics.sublanguagestats;

import java.util.Arrays;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BOMapHalsteadMetricsCalculator extends DefaultHandler implements IHalsteadMetricsCalculator {
 
	private static final List<String> BOMAP_EXCLUDED_ELEMENTS = Arrays.asList(
			"businessObjectMap",
			"inputBusinessObjectVariable",
			"outputBusinessObjectVariable",
			"propertyMap");
	
	private final String BOMAP_NAMESPACE = "http://www.ibm.com/xmlns/prod/websphere/wbiserver/map/6.0.0";

	private HalsteadMetrics metrics = new HalsteadMetrics();
	private JavaWPSHalsteadMetricsCalculator javaHalsteadMetricsCalculator = new JavaWPSHalsteadMetricsCalculator();
	
	private StringBuffer javaCode;
	private boolean inJavaCode;

	@Override
	public void startDocument() throws SAXException {
		metrics.clear();
		javaHalsteadMetricsCalculator.clear();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		if (BOMAP_NAMESPACE.equals(uri)) {
			if(!BOMAP_EXCLUDED_ELEMENTS.contains(localName)) {
				metrics.addOperator("bomap:" + localName);
				if ("javaCode".equals(localName)) {
					inJavaCode = true;
					javaCode = new StringBuffer();
				}
			} else {
				return;
			}
			for(int i = 0; i < attributes.getLength(); i++) {
				String attributeQName = attributes.getQName(i);
				metrics.addOperator("@" + attributeQName);
				metrics.addOperand(attributes.getValue(i));
			}
		} // TODO else?
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (inJavaCode) {
			javaCode.append(new String(ch, start, length));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (BOMAP_NAMESPACE.equals(uri) && "javaCode".equals(localName)) {
			inJavaCode = false;
			javaHalsteadMetricsCalculator.calculateComplexity(javaCode.toString());
		}
	}

	@Override
	public void endDocument() throws SAXException {
		HalsteadMetrics javaMetrics = javaHalsteadMetricsCalculator.getHalsteadMetrics();
		for(String operand : javaMetrics.getUniqueOperands()) {
			metrics.addOperand(operand, javaMetrics.getTotalNumberOfOperand(operand));
		}
		for(String operator : javaMetrics.getUniqueOperators()) {
			metrics.addOperator(operator, javaMetrics.getTotalNumberOfOperator(operator));
		}
	}
	
	@Override
	public HalsteadMetrics getHalsteadMetrics() {
		metrics.finalize();
		return metrics;
	}
}
