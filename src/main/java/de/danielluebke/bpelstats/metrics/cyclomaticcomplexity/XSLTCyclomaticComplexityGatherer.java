package de.danielluebke.bpelstats.metrics.cyclomaticcomplexity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.danielluebke.bpelstats.saxutil.SAXExecutor;

public class XSLTCyclomaticComplexityGatherer {

	private static final class XSLTCyclomaticComplexityHandler extends DefaultHandler {

		private static final String NS_XSLT = "http://www.w3.org/1999/XSL/Transform";
		
		private static final List<String> XSLT_CONTROLFLOW_CONSTRUCTS = Collections.unmodifiableList(Arrays.asList("if", "for-each", "when"));
		
		private CyclomaticComplexityFileStats result;
		private CyclomaticComplexityFunctionStats currentFunctionStats;

		public XSLTCyclomaticComplexityHandler(CyclomaticComplexityFileStats result) {
			this.result = result;
		}
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {
			if(NS_XSLT.equals(uri)) {
				if(localName.equals("template")) {
					String templateName = "";
					if(attributes.getValue("name") != null) {
						templateName = "name=\"" + attributes.getValue("name") + "\"";
					} else {
						templateName = "match=\"" + attributes.getValue("match") + "\"";
					}
					currentFunctionStats = new CyclomaticComplexityFunctionStats(templateName);
					result.getFunctionStats().add(currentFunctionStats);
				}
				
				if(XSLT_CONTROLFLOW_CONSTRUCTS.contains(localName)) {
					currentFunctionStats.incCyclomaticComplexity();
				}
			}
		}
	}
	
	public CyclomaticComplexityFileStats gather(File xsltFile) throws IOException, SAXException, ParserConfigurationException {
		return gather(new FileInputStream(xsltFile), xsltFile.getAbsolutePath());
	}
	

	public CyclomaticComplexityFileStats gather(InputStream inputstream, String xsltFileName) throws IOException, SAXException, ParserConfigurationException {
		CyclomaticComplexityFileStats result = new CyclomaticComplexityFileStats(xsltFileName, FileType.XSLT);
		
		XSLTCyclomaticComplexityHandler handler = new XSLTCyclomaticComplexityHandler(result);
		SAXExecutor.execute(inputstream, handler);
		
		return result;
	}
	
}
