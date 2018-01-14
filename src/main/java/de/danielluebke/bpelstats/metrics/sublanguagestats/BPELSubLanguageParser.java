package de.danielluebke.bpelstats.metrics.sublanguagestats;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import es.uca.webservices.xquery.parser.util.XQueryParsingException;

public class BPELSubLanguageParser extends DefaultHandler {

	private static final List<String> BPEL_URN_XQUERY = Arrays.asList(
			"urn:active-endpoints:expression-language:xquery1.0",		// ActiveVOS
			"urn:oasis:names:tc:wsbpel:2.0:sublang:xquery1.0"			// Apache ODE
		);
	private static final List<String> BPEL_URN_XPATH = Arrays.asList(
			"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0", 			// WS-BPEL 2.0
			"http://www.w3.org/TR/1999/REC-xpath-19991116"				// WPS
		);
	private static final List<String> URN_JAVA = Arrays.asList(
			"urn:bpelstats:java", 																	// Generic
			"http://www.ibm.com/xmlns/prod/websphere/business-process/expression-lang/java/6.0.0/"	// WPS
		);
	private static final List<String> URN_WPSBUILTIN = Arrays.asList(
			"http://www.ibm.com/xmlns/prod/websphere/business-process/expression-lang/built-in/6.0.0/"	// WPS
		);
	
	private static final String NAMESPACE_PROCESSSERVER = "http://www.ibm.com/xmlns/prod/websphere/business-process/6.0.0/";
	
	private File baseDirectory;
	
	public static class LanguageFragment {
		String language;
		String fragment;
		QName elementName;
	}
	
	private static final List<String> XQUERY_IMPORT_TYPES = Arrays.asList(
			"http://modules.active-endpoints.com/2009/07/xquery"
		);
	private static final List<String> XSLT_IMPORT_TYPES = Arrays.asList(
			"http://www.w3.org/1999/XSL/Transform"
		);
	
	private StringBuilder textContent = new StringBuilder();
	private String defaultQueryLanguage;
	private String defaultExpressionLanguage;
	
	private List<LanguageFragment> queryFragments = new ArrayList<BPELSubLanguageParser.LanguageFragment>();
	private List<LanguageFragment> expressionFragments = new ArrayList<BPELSubLanguageParser.LanguageFragment>();
	
	private List<Import> imports = new ArrayList<Import>();
	
	private LanguageFragment currentFragment = null;
	
	private int xquerySimpleExpressionLOCs = 0;
	private int xqueryComplexExpressionLOCs = 0;
	private int xquerySimpleQueryLOCs = 0;
	private int xqueryComplexQueryLOCs = 0;
	private int xpathQueryLOCs = 0;
	private int xpathExpressionLOCs = 0;
	private int xquerySimpleExpressionOccurrences = 0;
	private int xqueryComplexExpressionOccurrences = 0;
	private int xquerySimpleQueryOccurrences = 0;
	private int xqueryComplexQueryOccurrences = 0;
	private int xpathQueryOccurrences = 0;
	private int xpathExpressionOccurrences = 0;
	private int xqueryComplexityQuery = 0;
	private int xqueryComplexityExpression = 0;
	private int javaCodeOccurrences = 0;
	private int javaCodeLOCs = 0;
	private int javaCodeSLOCs = 0;
	private int javaCodeComplexity = 0;
	private int javaCodeNumConditions = 0;
	private int javaCodeNumIterations = 0;
	private int javaExpressionOccurrences = 0;
	private int javaExpressionLOCs = 0;
	private int javaExpressionSLOCs = 0;
	private int javaExpressionComplexity = 0;
	private int javaExpressionNumConditions = 0;
	private int javaExpressionNumIterations = 0;
	private int boMapOccurrences = 0;
	private int xmlMapOccurrences = 0;	
	private int wpsBuiltInOccurrences = 0;
	private String bpelNamespace;

	private static final String XML_MAP_TRANSFORM_PATTERN = "com\\.ibm\\.wbiserver\\.transform\\.util\\.TransformTypeHelper\\.transformTypes\\(.*\"(.*)\"";
	private Pattern xmlMapTransform;
	
	private static final String BO_MAP_TRANSFORM_PATTERN = "_serv.transform\\(\".*\", \"(.*)\", inputMap, outputMap, .*\\)\\);";
	private Pattern boMapTransform;	
	
	private XQueryHalsteadMetricsCalculator xpathHalsteadCalculator = null;
	private HalsteadMetrics xqueryHalsteadMetrics;
	private HalsteadMetrics javaCodeHalsteadMetrics;
	private HalsteadMetrics javaExpressionHalsteadMetrics;
	private int xqueryNumConditions;
	private int xqueryNumIterations;
	
	public BPELSubLanguageParser(File baseDirectory) {
		this.baseDirectory = baseDirectory;
		
		xmlMapTransform = Pattern.compile(XML_MAP_TRANSFORM_PATTERN);
		boMapTransform = Pattern.compile(BO_MAP_TRANSFORM_PATTERN);
	}
	
	@Override
	public void startDocument() throws SAXException {
		textContent = new StringBuilder();
		defaultQueryLanguage = BPEL_URN_XPATH.get(0);
		defaultExpressionLanguage = defaultQueryLanguage;
		imports.clear();
		queryFragments.clear();
		expressionFragments.clear();
		xquerySimpleExpressionLOCs = 0;
		xpathQueryLOCs = 0;
		xquerySimpleQueryLOCs = 0;
		xpathExpressionLOCs = 0;
		xqueryComplexityQuery = 0;
		xqueryComplexityExpression = 0;
		javaCodeOccurrences = 0;
		javaCodeLOCs = 0;
		javaCodeSLOCs = 0;
		javaCodeComplexity = 0;
		javaCodeNumConditions = 0;
		javaCodeNumIterations = 0;
		javaExpressionOccurrences = 0;
		javaExpressionLOCs = 0;
		javaExpressionSLOCs = 0;
		javaExpressionComplexity = 0;
		javaExpressionNumConditions = 0;
		javaExpressionNumIterations = 0;
		wpsBuiltInOccurrences = 0;
		boMapOccurrences = 0;
		xmlMapOccurrences = 0;
		
		xpathHalsteadCalculator = new XQueryHalsteadMetricsCalculator();
		xqueryHalsteadMetrics = new HalsteadMetrics();
		javaCodeHalsteadMetrics = new HalsteadMetrics();
		javaExpressionHalsteadMetrics = new HalsteadMetrics();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if(bpelNamespace == null) {
			bpelNamespace = uri;
		}
		
		textContent.delete(0, textContent.length());
		
		if(bpelNamespace.equals(uri) && localName.equals("process")) {
			String expressionLanguage = attributes.getValue("expressionLanguage");
			if(expressionLanguage != null) {
				defaultExpressionLanguage = expressionLanguage;
			}
			String queryLanguage = attributes.getValue("queryLanguage");
			if(queryLanguage != null) {
				defaultQueryLanguage = queryLanguage;
			}
		}
		
		if(bpelNamespace.equals(uri) && localName.equals("import")) {
			String type = attributes.getValue("importType");
			if(XQUERY_IMPORT_TYPES.contains(type)) {
				Import i = new Import();
				i.importType = "XQUERY";
				try {
					i.location = new File(baseDirectory, attributes.getValue("location")).getCanonicalFile();
				} catch (IOException e) {
					throw new SAXException(e);
				}
				imports.add(i);
			}
			
			if(XSLT_IMPORT_TYPES.contains(type)) {
				Import i = new Import();
				i.importType = "XSLT";
				i.location = new File(baseDirectory, attributes.getValue("location"));
				imports.add(i);
			}
		}
		
		if(
				(bpelNamespace.equals(uri) && (
						localName.equals("condition") || 
						localName.equals("for") || 
						localName.equals("until") || 
						localName.equals("repeatEvery") || 
						localName.equals("from") || 
						localName.equals("startCounterValue") || 
						localName.equals("finalCounterValue") || 
						localName.equals("completionCondition") || 
						localName.equals("branches") || 
						localName.equals("joinCondition") || 
						localName.equals("transitionCondition") || 
						localName.equals("to")
				)) ||
				( NAMESPACE_PROCESSSERVER.equals(uri) && (
						localName.equals("exitCondition")
				))
			) {
			String expressionLanguage = attributes.getValue("expressionLanguage");
			currentFragment = new LanguageFragment();
			currentFragment.language = expressionLanguage;
			currentFragment.elementName = new QName(uri, localName);
			if(expressionLanguage == null) {
				currentFragment.language = defaultExpressionLanguage;
			}
			expressionFragments.add(currentFragment);
		}
		
		if(uri.equals(NAMESPACE_PROCESSSERVER) && localName.equals("javaCode")) {
			currentFragment = new LanguageFragment();
			currentFragment.language = URN_JAVA.get(0);
			currentFragment.elementName = new QName(uri, localName);
			expressionFragments.add(currentFragment);
		}
		
		if(
				(bpelNamespace.equals(uri) && localName.equals("literal"))
			) {
			// assign/from/literal must not be counted
			expressionFragments.remove(currentFragment);
			queryFragments.remove(currentFragment);
			currentFragment = null;
		}
		
		if(localName.equals("query")) {
			String queryLanguage = attributes.getValue("queryLanguage");
			currentFragment = new LanguageFragment();
			currentFragment.language = queryLanguage;
			currentFragment.elementName = new QName(uri, localName);
			if(queryLanguage == null) {
				currentFragment.language = defaultQueryLanguage;
			}
			queryFragments.add(currentFragment);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		
		if(currentFragment != null) {
			currentFragment.fragment = textContent.toString();
		
			if(
				(currentFragment.fragment == null || "".trim().equals(currentFragment.fragment)) &&
				!URN_WPSBUILTIN.contains(currentFragment.language)
				) {
				expressionFragments.remove(currentFragment);
				queryFragments.remove(currentFragment);
			}
		
			currentFragment = null;
		}
		textContent.delete(0, textContent.length());
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		textContent.append(ch, start, length);
	}
	
	@Override
	public void endDocument() throws SAXException {
		removeEmptyFragments();
		resetLOCCounts();
		XQuerySubLanguageParser xqueryParser = new XQuerySubLanguageParser();
		
		for(LanguageFragment lf : queryFragments) {
			if(BPEL_URN_XPATH.contains(lf.language)) {
				xpathQueryOccurrences++;
				xpathQueryLOCs += calculateLOC(lf);
				try {
					xpathHalsteadCalculator.parse(lf.fragment);
				} catch (XQueryParsingException e) {
					
				}
				
			} else if(BPEL_URN_XQUERY.contains(lf.language)) {
				try {
					xqueryParser.parse(lf.fragment);
				} catch (XQueryParsingException e) {
					throw new SAXException(e);
				}
				boolean isComplex = xqueryParser.isComplex();
				int loc = calculateLOC(lf);
				xqueryComplexityQuery +=  xqueryParser.getComplexity();
				if(isComplex) {
					xqueryComplexQueryOccurrences++;
					xqueryComplexQueryLOCs += loc; 
				} else {
					xquerySimpleQueryOccurrences++;
					xquerySimpleQueryLOCs += loc;
				}
				xqueryHalsteadMetrics.add(xqueryParser.getHalsteadMetrics());
			} else {
				System.err.println(lf.language + " not found!");
			}
		}
		
		for(LanguageFragment lf : expressionFragments) {
			if(BPEL_URN_XPATH.contains(lf.language)) {
				xpathExpressionOccurrences++;
				xpathExpressionLOCs += calculateLOC(lf);
			} else if(BPEL_URN_XQUERY.contains(lf.language)) {
				try {
					xqueryParser.parse(lf.fragment);
				} catch (XQueryParsingException e) {
					throw new SAXException(e);
				}
				boolean isComplex = xqueryParser.isComplex();
				int loc = xqueryParser.getLoc();
				xqueryComplexityExpression  += xqueryParser.getComplexity();
				xqueryNumConditions += xqueryParser.getNumConditions();
				xqueryNumIterations += xqueryParser.getNumIterations();
				if(isComplex) {
					xqueryComplexExpressionOccurrences++;
					xqueryComplexExpressionLOCs += loc; 
				} else {
					xquerySimpleExpressionOccurrences++;
					xquerySimpleExpressionLOCs += loc;
				}
			} else if(URN_JAVA.contains(lf.language)) {
				if (lf.language.equals(URN_JAVA.get(1))) { 
					// WPS JavaExpression
					javaExpressionOccurrences++;
					javaExpressionLOCs += calculateLOC(lf);
					javaExpressionSLOCs += calculateSLOC(lf);

					JavaWPSSubLanguageParser javaParser = new JavaWPSSubLanguageParser(baseDirectory);
					javaParser.calculateComplexity(lf.fragment);
					javaExpressionComplexity += javaParser.getComplexity();
					javaExpressionNumConditions += javaParser.getNumConditions();
					javaExpressionNumIterations += javaParser.getNumIterations();
					javaExpressionHalsteadMetrics.add(javaParser.getHalsteadMetrics());
					
				} else {
					// WPS JavaCode
					Matcher xmlMap = xmlMapTransform.matcher(lf.fragment);
					Matcher boMap = boMapTransform.matcher(lf.fragment);
					if (xmlMap.find()) {
						xmlMapOccurrences++;
	
						// Process XSLT representation of XML maps
						Import i = new Import();
						i.importType = "XSLT";
						i.location = new File(baseDirectory, xmlMap.group(1));
						imports.add(i);				
					} else if (boMap.find()) {
						// Process BOMaps
						boMapOccurrences++;
	
						Import i = new Import();
						i.importType = "BOMAP";
						i.location = new File(baseDirectory, boMap.group(1) + ".map");
						imports.add(i);
					} else {
						// Process as pure java code
						javaCodeOccurrences++;
						javaCodeLOCs += calculateLOC(lf);
						javaCodeSLOCs += calculateSLOC(lf);
	
						JavaWPSSubLanguageParser javaParser = new JavaWPSSubLanguageParser(baseDirectory);
						javaParser.calculateComplexity(lf.fragment);
						javaCodeComplexity += javaParser.getComplexity();
						javaCodeNumConditions += javaParser.getNumConditions();
						javaCodeNumIterations += javaParser.getNumIterations();
						javaCodeHalsteadMetrics.add(javaParser.getHalsteadMetrics());
					}
				}
			} else if(URN_WPSBUILTIN.contains(lf.language)) {
				this.wpsBuiltInOccurrences++;
			} else {
				System.err.println(lf.language + " not found!");
			}
		}
	}

	private void resetLOCCounts() {
		xqueryComplexExpressionLOCs = 0;
		xqueryComplexQueryLOCs = 0;
		xquerySimpleExpressionLOCs = 0;
		xquerySimpleQueryLOCs = 0;
		xpathExpressionLOCs = 0;
		xpathQueryLOCs = 0;
		javaCodeLOCs = 0;
		javaCodeSLOCs = 0;
		javaExpressionLOCs = 0;
		javaExpressionSLOCs = 0;
	}

	private void removeEmptyFragments() {
		List<LanguageFragment> fragmentsToDelete = new ArrayList<LanguageFragment>();
		for(LanguageFragment f : queryFragments) {
			if((f.fragment == null || "".equals(f.fragment.trim())) && 
					!(URN_WPSBUILTIN.contains(f.language))
				) {
				fragmentsToDelete.add(f);
			}
		}
		for(LanguageFragment f : expressionFragments) {
			if((f.fragment == null || "".equals(f.fragment.trim())) && 
					!(URN_WPSBUILTIN.contains(f.language))
				) {
				fragmentsToDelete.add(f);
			}
		}
		
		for(LanguageFragment f : fragmentsToDelete) {
			expressionFragments.remove(f);
			queryFragments.remove(f);
		}
	}
	
	private int calculateLOC(LanguageFragment f) {
		return LOCCalculator.calculateLOC(f.fragment);
	}
	
	private int calculateSLOC(LanguageFragment f) {
		return LOCCalculator.calculateJavaSLOC(f.fragment);
	}

	public List<Import> getImports() {
		return Collections.unmodifiableList(imports);
	}

	public int getXPathQueryLOCs() {
		return xpathQueryLOCs;
	}
	public int getXPathExpressionLOCs() {
		return xpathExpressionLOCs;
	}

	public int getXQuerySimpleExpressionLOCs() {
		return xquerySimpleExpressionLOCs;
	}

	public int getXQueryComplexExpressionLOCs() {
		return xqueryComplexExpressionLOCs;
	}

	public int getXQuerySimpleQueryLOCs() {
		return xquerySimpleQueryLOCs;
	}

	public int getXQueryComplexQueryLOCs() {
		return xqueryComplexQueryLOCs;
	}
	
	public int getXQuerySimpleExpressionOccurrences() {
		return xquerySimpleExpressionOccurrences;
	}

	public int getXQueryComplexExpressionOccurrences() {
		return xqueryComplexExpressionOccurrences;
	}

	public int getXQuerySimpleQueryOccurrences() {
		return xquerySimpleQueryOccurrences;
	}

	public int getXQueryComplexQueryOccurrences() {
		return xqueryComplexQueryOccurrences;
	}

	public int getXPathQueryOccurrences() {
		return xpathQueryOccurrences;
	}

	public int getXPathExpressionOccurrences() {
		return xpathExpressionOccurrences;
	}
	
	public int getXQueryComplexityExpression() {
		return xqueryComplexityExpression;
	}
	
	public int getXQueryComplexityQuery() {
		return xqueryComplexityQuery;
	}

	public int getJavaCodeOccurrences() {
		return javaCodeOccurrences;
	}

	public int getJavaCodeLOCs() {
		return javaCodeLOCs;
	}
	
	public int getJavaCodeSLOCs() {
		return javaCodeSLOCs;
	}
	
	public int getJavaExpressionOccurrences() {
		return javaExpressionOccurrences;
	}

	public int getJavaExpressionLOCs() {
		return javaExpressionLOCs;
	}

	public int getJavaExpressionSLOCs() {
		return javaExpressionSLOCs;
	}

	public int getJavaExpressionComplexity() {
		return javaExpressionComplexity;
	}

	public int getJavaExpressionNumConditions() {
		return javaExpressionNumConditions;
	}

	public int getJavaExpressionNumIterations() {
		return javaExpressionNumIterations;
	}

	public int getWpsBuiltInOccurrences() {
		return wpsBuiltInOccurrences;
	}
	
	public int getBoMapOccurrences() {
		return boMapOccurrences;
	}

	public int getXmlMapOccurrences() {
		return xmlMapOccurrences;
	}

	public int getJavaCodeNumConditions() {
		return javaCodeNumConditions;
	}

	public int getJavaCodeNumIterations() {
		return javaCodeNumIterations;
	}

	public int getJavaCodeComplexity() {
		return javaCodeComplexity;
	}
	
	public HalsteadMetrics getXPathHalsteadMetrics() {
		return xpathHalsteadCalculator.getHalsteadMetrics();
	}
	
	public HalsteadMetrics getXQueryHalsteadMetrics() {
		return xqueryHalsteadMetrics;
	}

	public HalsteadMetrics getJavaCodeHalsteadMetrics() {
		return javaCodeHalsteadMetrics;
	}

	public HalsteadMetrics getJavaExpressionHalsteadMetrics() {
		return javaExpressionHalsteadMetrics;
	}

	public int getXQueryNumConditions() {
		return xqueryNumConditions;
	}

	public int getXQueryNumIterations() {
		return xqueryNumIterations;
	}
	
}
