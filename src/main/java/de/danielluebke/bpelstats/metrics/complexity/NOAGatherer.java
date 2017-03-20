package de.danielluebke.bpelstats.metrics.complexity;

import javax.xml.namespace.QName;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Gatherer for calulating the Number of Activities (NOA), i.e. the count of all
 * basic activities in a process
 * 
 * @author dluebke
 * 
 */
public class NOAGatherer extends BPELComplexityMetricGatherer {

	private int noa = 0;
	private BPELConstants bpelConstants;

	@Override
	public void startDocument() throws SAXException {
		noa = 0;
		bpelConstants = null;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		if(bpelConstants == null) {
			bpelConstants = new BPELConstants(uri);
		}
		
		QName elementName = new QName(uri, localName);

		if (bpelConstants.basicActivities.contains(elementName)) {
			noa++;
		}
	}

	@Override
	public String getName() {
		return "NOA";
	}

	@Override
	public String getValue() {
		return "" + noa;
	}

}
