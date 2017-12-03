package de.danielluebke.bpelstats.metrics.sublanguagestats;

import java.io.File;

public class FileStats {

	public File absoluteFileName;
	
	public String fileType;
	
	public int xquerySimpleExpressionLOCs;
	public int xquerySimpleQueryLOCs;
	public int xqueryExternalLOCs;
	public int xqueryReuseLOCs;
	public int xsltExternalLOCs;
	public int xsltReuseLOCs;
	public int xpathExpressionsLOCs;
	public int xpathQueriesLOCs;
	public int xqueryComplexExpressionLOCs;
	public int xqueryComplexQueryLOCs;
	public int xquerySimpleExpressionOccurrences = 0;
	public int xqueryComplexExpressionOccurrences = 0;
	public int xquerySimpleQueryOccurrences = 0;
	public int xqueryComplexQueryOccurrences = 0;
	public int xpathQueryOccurrences = 0;
	public int xpathExpressionOccurrences = 0;
	public int xqueryComplexity = 0;
	public int xsltComplexity = 0;
	public int javaLOCs = 0;
	public int javaSLOCs = 0;
	public int javaComplexity = 0;
	public int javaNumConditions = 0;
	public int javaNumIterations = 0;	
	public int javaOccurrences = 0;
	public int wpsBuiltInOccurrences = 0;
	public int xmlMapOccurrences = 0;
	public int boMapLOCs = 0;
	public int boMapComplexity = 0;
	public int boMapNumConditions = 0;
	public int boMapNumIterations = 0;
	public int boMapOccurrences = 0;
	public HalsteadMetrics xpathHalstead = new HalsteadMetrics();
	public HalsteadMetrics xsltHalstead = new HalsteadMetrics();
	public HalsteadMetrics xqueryHalstead = new HalsteadMetrics();
	public HalsteadMetrics xmlmapHalstead = new HalsteadMetrics();
	public HalsteadMetrics bomapHalstead = new HalsteadMetrics();
	public HalsteadMetrics javaHalstead = new HalsteadMetrics();

	@Override
	public String toString() {
		return absoluteFileName.getName() + ": XQ=" + (xquerySimpleExpressionLOCs+xquerySimpleQueryLOCs+xqueryExternalLOCs+xqueryReuseLOCs) + ", XSLT=" + (xsltReuseLOCs+xsltExternalLOCs) + ", XPath:" + (xpathExpressionsLOCs+xpathQueriesLOCs);
	}
	
	public void add(FileStats f) {
		xquerySimpleExpressionLOCs += f.xquerySimpleExpressionLOCs;
		xquerySimpleQueryLOCs += f.xquerySimpleQueryLOCs;
		xqueryExternalLOCs += f.xqueryExternalLOCs;
		xqueryReuseLOCs += f.xqueryReuseLOCs;
		xsltExternalLOCs += f.xsltExternalLOCs;
		xsltReuseLOCs += f.xsltReuseLOCs;
		xpathExpressionsLOCs += f.xpathExpressionsLOCs;
		xpathQueriesLOCs += f.xpathQueriesLOCs;
		xqueryComplexExpressionLOCs += f.xqueryComplexExpressionLOCs;
		xqueryComplexQueryLOCs += f.xqueryComplexQueryLOCs;
		xquerySimpleExpressionOccurrences += f.xquerySimpleExpressionOccurrences;
		xqueryComplexExpressionOccurrences += f.xqueryComplexExpressionOccurrences;
		xquerySimpleQueryOccurrences += f.xquerySimpleQueryOccurrences;
		xqueryComplexQueryOccurrences += f.xqueryComplexQueryOccurrences;
		xpathQueryOccurrences += f.xpathQueryOccurrences;
		xpathExpressionOccurrences += f.xpathExpressionOccurrences;
		xqueryComplexity += f.xqueryComplexity;
		xsltComplexity += f.xsltComplexity;
		javaLOCs += f.javaLOCs;
		javaSLOCs += f.javaSLOCs;
		javaComplexity += f.javaComplexity;
		javaNumConditions += f.javaNumConditions;
		javaNumIterations += f.javaNumIterations;		
		javaOccurrences += f.javaOccurrences;
		wpsBuiltInOccurrences += f.wpsBuiltInOccurrences;
		xmlMapOccurrences += f.xmlMapOccurrences;
		boMapLOCs += f.boMapLOCs;
		boMapComplexity += f.boMapComplexity;
		boMapNumConditions += f.boMapNumConditions;
		boMapNumIterations += f.boMapNumIterations;
		boMapOccurrences += f.boMapOccurrences;
		xpathHalstead.add(f.xpathHalstead);
		xsltHalstead.add(f.xsltHalstead);
		xqueryHalstead.add(f.xqueryHalstead);
		xmlmapHalstead.add(f.xmlmapHalstead);
		bomapHalstead.add(f.bomapHalstead);
		javaHalstead.add(f.javaHalstead);
	}
}
