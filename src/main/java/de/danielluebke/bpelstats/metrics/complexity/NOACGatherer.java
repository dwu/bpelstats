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
	private BPELConstants bpelConstants;

	@Override
	public void startDocument() throws SAXException {
		noac = 0;
		bpelConstants = null;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		QName elementName = new QName(uri, localName);

		if(bpelConstants == null) {
			bpelConstants = new BPELConstants(uri);
		}
		
		if (bpelConstants.basicActivities.contains(elementName)
				|| bpelConstants.structuredActivities.contains(elementName)) {
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
