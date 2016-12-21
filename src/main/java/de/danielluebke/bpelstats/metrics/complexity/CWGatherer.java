package de.danielluebke.bpelstats.metrics.complexity;

import javax.xml.namespace.QName;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Gatherer for calulating the Cognitive Weight (CW)
 * 
 * @author dluebke
 * 
 */
public class CWGatherer extends BPELComplexityMetricGatherer {

	private int cw;
	
	@Override
	public void startDocument() throws SAXException {
		cw = 0;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if(BPELConstants.BPEL_NAMESPACE.equals(uri)) {
			if("flow".equals(localName)) {
				cw += 4;
			} else if ("while".equals(localName)) {
				cw += 3;
			} else if ("forEach".equals(localName)) {
				cw += 3;
			} else if ("repeatUntil".equals(localName)) {
				cw += 3;
			} else if ("pick".equals(localName)) {
				cw += 3;
			} else if ("eventHandlers".equals(localName)) {
				cw += 3;
			} else if ("faultHandlers".equals(localName)) {
				cw += 3;
			} else if ("compensationHandler".equals(localName)) {
				cw += 3;
			} else if ("terminationHandler".equals(localName)) {
				cw += 3;
			} else if ("invoke".equals(localName)) {
				cw += 2;
			} else if ("reply".equals(localName)) {
				cw += 2;
			} else if ("if".equals(localName)) {
				cw += 2;
			} else if (BPELConstants.BASIC_ACTIVITIES.contains(new QName(BPELConstants.BPEL_NAMESPACE, localName))) {
				cw ++;
			}
		}
	}

	@Override
	public String getName() {
		return "CW";
	}

	@Override
	public String getValue() {
		return "" + cw;
	}

}
