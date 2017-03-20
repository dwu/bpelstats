package de.danielluebke.bpelstats.metrics.assign;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CopyPerAssignGatherer extends DefaultHandler {

	private String bpelNamespace;
	private AssignStatistic currentAssign;
	private List<AssignStatistic> assigns = new ArrayList<>();
	private List<String> elementTree = new ArrayList<>();
	
	@Override
	public void startDocument() throws SAXException {
		bpelNamespace = null;
		assigns.clear();
		elementTree.clear();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(bpelNamespace == null) {
			bpelNamespace = uri;
		}
		
		
		if(bpelNamespace.equals(uri) && "assign".equals(localName)) {
			currentAssign = new AssignStatistic(convertListToPath(elementTree) + "/assign");
			assigns.add(currentAssign);
		}
		
		if("copy".equals(localName)) {
			if(elementTree.get(elementTree.size() - 1).equals("assign") && bpelNamespace.equals(uri) && "copy".equals(localName)) {
				currentAssign.incCopyCount();
			}
		}
		
		elementTree.add(localName);
	}
	
	private String convertListToPath(List<String> elementTree) {
		StringBuilder result = new StringBuilder();
		
		for(String element : elementTree) {
			result.append("/").append(element);
		}
		return result.toString();
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		elementTree.remove(elementTree.size() - 1);
	}
	
	public List<AssignStatistic> getAssigns() {
		return assigns;
	}
}
