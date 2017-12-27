package de.danielluebke.bpelstats.metrics.sublanguagestats;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XSLTSubLanguageParser extends DefaultHandler {

	private static final List<String> XSLT_COMPLEXITY_STRUCTURES = Collections.unmodifiableList(Arrays.asList("template", "if", "when", "otherwise", "for-each")); 
	private static final List<String> XSLT_CONDTIONAL_STRUCTURES = Collections.unmodifiableList(Arrays.asList("if", "when", "otherwise")); 
	private static final List<String> XSLT_ITERATION_STRUCTURES = Collections.unmodifiableList(Arrays.asList("for-each")); 
	
	private final String XSLT_NAMESPACE = "http://www.w3.org/1999/XSL/Transform";

	private List<Import> imports = new ArrayList<Import>();
	private int complexity = 0;
	private XSLTHalsteadMetricsCalculator halsteadCalculator = new XSLTHalsteadMetricsCalculator();

	private File baseDirectory;

	private int numConditions;

	private int numIterations;
	
	public XSLTSubLanguageParser(File baseDirectory) {
		this.baseDirectory = baseDirectory;
	}
	
	@Override
	public void startDocument() throws SAXException {
		imports.clear();
		complexity = 0;
		numConditions = 0;
		numIterations = 0;
		halsteadCalculator.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		halsteadCalculator.startElement(uri, localName, qName, attributes);
		
		if (!XSLT_NAMESPACE.equals(uri)) return;
		
		if (localName.equals("import")) {
			Import i = new Import();
			i.importType = "XSLT";
			try {
				i.location = new File(baseDirectory, attributes.getValue("href")).getCanonicalFile();
			} catch (IOException e) {
				throw new SAXException(e);
			}
			imports.add(i);
		}
		
		if (XSLT_COMPLEXITY_STRUCTURES.contains(localName)) {
			complexity++;
		}
		
		if(XSLT_CONDTIONAL_STRUCTURES.contains(localName)) {
			numConditions++;
		}
		
		if(XSLT_ITERATION_STRUCTURES.contains(localName)) {
			numIterations++;
		}
	}
	
	@Override
	public void endDocument() throws SAXException {
		halsteadCalculator.endDocument();
	}

	public List<? extends Import> getImports() {
		return Collections.unmodifiableList(imports);
	}
	
	public int getComplexity() {
		return complexity;
	}

	public HalsteadMetrics getHalsteadMetrics() {
		return halsteadCalculator.getHalsteadMetrics();
	}
	
	public int getNumConditions() {
        return numConditions;
    }

    public int getNumIterations() {
        return numIterations;
    }
}
