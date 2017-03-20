package de.danielluebke.bpelstats.metrics.cyclomaticcomplexity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.danielluebke.bpelstats.saxutil.SAXExecutor;

public class BPELCyclomaticComplexityGatherer {

	private static final class BPELCyclomaticComplexityHandler extends DefaultHandler {

		private String bpelNamespace;
		
		private static final List<String> BPEL_CONTROLFLOW_CONSTRUCTS_ADD = 
				Collections.unmodifiableList(Arrays.asList(
						"case", "exit", "if", "ifElse", "forEach", "repeatUntil", "terminate", "while", "onAlarm", "onMessage", "onEvent"
				));
		private static final List<String> BPEL_CONTROLFLOW_CONSTRUCTS_SUBTRACT = 
				Collections.unmodifiableList(Arrays.asList(
						"pick"
						));

		private static final List<String> BPEL_ACTIVITIES =
				Collections.unmodifiableList(Arrays.asList(
						"assign", "compensate", "compensateScope", "empty", "exit", "terminate", "flow", "forEach", "if", 
						"switch", "invoke", "pick", "receive", "repeatUntil", "reply", "rethrow", "scope", "sequence",
						"throw", "validate", "wait", "while", "extensionActivity"
						));
		
		private CyclomaticComplexityFileStats result;
		private CyclomaticComplexityFunctionStats currentFunctionStats;
		private QName flowQName;
		private QName linksQName;
		private QName linkQName;
		
		private Stack<QName> elementStack;
		
		public BPELCyclomaticComplexityHandler(CyclomaticComplexityFileStats result) {
			this.result = result;
		}
		
		@Override
		public void startDocument() throws SAXException {
			elementStack = new Stack<>();
			currentFunctionStats = new CyclomaticComplexityFunctionStats("process");
			result.getFunctionStats().add(currentFunctionStats);
		}
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			
			if(bpelNamespace == null) {
				bpelNamespace = uri;
				flowQName = new QName(bpelNamespace, "flow");
				linksQName = new QName(bpelNamespace, "links"); 
				linkQName = new QName(bpelNamespace, "link"); 
			}
			
			if(bpelNamespace.equals(uri)) {
				System.out.println("Start BPEL: " + localName + " " + currentFunctionStats.getCyclomaticComplexity());
				if(localName.equals(flowQName.getLocalPart())) {
					currentFunctionStats.incCyclomaticComplexity();
					System.out.println("Detected flow");
				} else if(BPEL_CONTROLFLOW_CONSTRUCTS_ADD.contains(localName)) {
					currentFunctionStats.incCyclomaticComplexity();
					System.out.println("Detected Control-Flow Construct");
				} else if(BPEL_CONTROLFLOW_CONSTRUCTS_SUBTRACT.contains(localName)) {
					currentFunctionStats.decCyclomaticComplexity();
					System.out.println("Detected Dec");
				} else if(!elementStack.isEmpty() && elementStack.peek().equals(flowQName) && BPEL_ACTIVITIES.contains(localName)) {
					currentFunctionStats.decCyclomaticComplexity();
					System.out.println("Detected Activity in Flow");
				} else if(localName.equals(linkQName.getLocalPart())) {
					currentFunctionStats.incCyclomaticComplexity();
					System.out.println("Detected link");
				}
				System.out.println("End BPEL: " + localName + " " + currentFunctionStats.getCyclomaticComplexity());
			}
			
			elementStack.push(new QName(uri, localName));
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			elementStack.pop();
		}
	}
	
	public CyclomaticComplexityFileStats gather(File bpelFile) throws IOException, SAXException, ParserConfigurationException {
		return gather(new FileInputStream(bpelFile), bpelFile.getAbsolutePath());
	}
	

	public CyclomaticComplexityFileStats gather(InputStream inputstream, String bpelFileName) throws IOException, SAXException, ParserConfigurationException {
		CyclomaticComplexityFileStats result = new CyclomaticComplexityFileStats(bpelFileName, FileType.BPEL);
		
		BPELCyclomaticComplexityHandler handler = new BPELCyclomaticComplexityHandler(result);
		SAXExecutor.execute(inputstream, handler);
		
		return result;
	}
	
}
