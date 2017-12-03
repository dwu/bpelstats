package de.danielluebke.bpelstats.metrics.sublanguagestats;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BOMapSubLanguageParser extends DefaultHandler {

	private final String BOMAP_NAMESPACE = "http://www.ibm.com/xmlns/prod/websphere/wbiserver/map/6.0.0";

	private List<Import> imports = new ArrayList<Import>();

	private int complexity = 0;
	private int numConditions = 0;
	private int numIterations = 0;

	private JavaWPSSubLanguageParser javaParser;
	private StringBuffer javaCode;
	private boolean inJavaCode = false;

	private File baseDirectory;

	private BOMapHalsteadMetricsCalculator halsteadCalculator = new BOMapHalsteadMetricsCalculator();

	public BOMapSubLanguageParser(File baseDirectory) {
		this.baseDirectory = baseDirectory;
		this.javaParser = new JavaWPSSubLanguageParser(baseDirectory);
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
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		halsteadCalculator.startElement(uri, localName, qName, attributes);
		
		if (!BOMAP_NAMESPACE.equals(uri))
			return;

		if (localName.equals("submap")) {
			Import i = new Import();
			i.importType = "BOMAP";
			try {
				i.location = new File(baseDirectory, attributes.getValue("submapName").replaceAll(".*:", "") + ".map")
						.getCanonicalFile();
			} catch (IOException e) {
				throw new SAXException(e);
			}
			imports.add(i);
		} else if (localName.equals("javaCode")) {
			inJavaCode = true;
			javaCode = new StringBuffer();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		halsteadCalculator.characters(ch, start, length);
		
		if (inJavaCode)
			javaCode.append(new String(ch, start, length));
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		halsteadCalculator.endElement(uri, localName, qName);
		
		if (!BOMAP_NAMESPACE.equals(uri))
			return;

		if (localName.equals("javaCode")) {
			inJavaCode = false;
			javaParser.calculateComplexity(javaCode.toString());
			complexity += javaParser.getComplexity();
			numConditions += javaParser.getNumConditions();
			numIterations += javaParser.getNumIterations();
		}

		// Note: Imported packages are ignored
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

	public int getNumConditions() {
		return numConditions;
	}

	public int getNumIterations() {
		return numIterations;
	}

	public HalsteadMetrics getHalsteadMetrics() {
		return halsteadCalculator.getHalsteadMetrics();
	}
}
