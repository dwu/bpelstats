package de.danielluebke.bpelstats.metrics.complexity;

import javax.xml.namespace.QName;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Gatherer for calulating the Nested Depth (NP)
 * 
 * @author dluebke
 * 
 */
public class NDGatherer extends BPELComplexityMetricGatherer {

	private int nd = 0;
	private int currentLevel = 0;
	private BPELConstants bpelConstants;
	
	@Override
	public void startDocument() throws SAXException {
		nd = 0;
		currentLevel = 0;
		bpelConstants = null;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		if(bpelConstants == null) {
			bpelConstants = new BPELConstants(uri);
		}
		
		QName elementName = new QName(uri, localName);

		if(bpelConstants.structuredActivities.contains(elementName)) {
			currentLevel++;
		}
		
		if (bpelConstants.basicActivities.contains(elementName)) {
			nd = Math.max(nd, currentLevel);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		QName elementName = new QName(uri, localName);

		if(bpelConstants.structuredActivities.contains(elementName)) {
			currentLevel--;
		}
	}
	
	@Override
	public String getName() {
		return "ND";
	}

	@Override
	public String getValue() {
		return "" + nd;
	}

}
