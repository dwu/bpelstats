package de.danielluebke.bpelstats.metrics.complexity;

import javax.xml.namespace.QName;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Gatherer for calulating the Number of Activities incl Control-Flow (NOAC),
 * i.e. the count of all basic and structured activities in a process
 * 
 * @author dluebke
 * 
 */
public class NOACGatherer extends BPELComplexityMetricGatherer {

	private int noac = 0;

	@Override
	public void startDocument() throws SAXException {
		noac = 0;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		QName elementName = new QName(uri, localName);

		if (BPELConstants.BASIC_ACTIVITIES.contains(elementName)
				|| BPELConstants.STRUCTURED_ACTIVITIES.contains(elementName)) {
			noac++;
		}
	}

	@Override
	public String getName() {
		return "NOAC";
	}

	@Override
	public String getValue() {
		return "" + noac;
	}

}
