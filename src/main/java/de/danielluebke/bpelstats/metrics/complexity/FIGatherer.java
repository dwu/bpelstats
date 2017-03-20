package de.danielluebke.bpelstats.metrics.complexity;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Gatherer for calulating the Fan-In (FI)
 * 
 * @author dluebke
 * 
 */
public class FIGatherer extends BPELComplexityMetricGatherer {

	private int fi = 0;
	private int pickLevel = 0;
	private boolean searchForOnMessageInCreateInstancePick = false;
	private String bpelNamespace;
	
	@Override
	public void startDocument() throws SAXException {
		fi = 0;
		pickLevel = 0;
		searchForOnMessageInCreateInstancePick = false;
		bpelNamespace = null;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		if(bpelNamespace == null) {
			bpelNamespace = uri;
		}
		
		if(searchForOnMessageInCreateInstancePick) {
			pickLevel++;
		}
		
		if(bpelNamespace.equals(uri)) {
			if("receive".equals(localName) && "yes".equals(attributes.getValue("createInstance"))) {
				fi = 1;
			}
			
			if("pick".equals(localName) && "yes".equals(attributes.getValue("createInstance"))) {
				searchForOnMessageInCreateInstancePick = true;
				pickLevel = 1;
				fi = 0;
			}
			
			if(searchForOnMessageInCreateInstancePick && pickLevel == 2 && "onMessage".equals(localName)) {
				fi++;
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(searchForOnMessageInCreateInstancePick) {
			pickLevel--;
		}
		
		if(pickLevel == 0) {
			searchForOnMessageInCreateInstancePick = false;
		}
	}
	
	@Override
	public String getName() {
		return "FI";
	}

	@Override
	public String getValue() {
		return "" + fi;
	}

}
