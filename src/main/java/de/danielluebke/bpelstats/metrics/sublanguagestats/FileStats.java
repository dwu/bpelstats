package de.danielluebke.bpelstats.metrics.sublanguagestats;

import java.io.File;

public class FileStats {

	public File absoluteFileName;
	
	public String fileType;
	
	public int xquerySimpleExpressionLOCs;
	public int xquerySimpleQueryLOCs;
	public int xqueryExternalLOCs;
	public int xqueryReuseLOCs;
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
	public int xqueryNumIterations;
	public int xqueryNumConditions;
	public int xsltExternalLOCs;
	public int xsltReuseLOCs;
	public int xsltComplexity = 0;
	public int xsltNumIterations;
	public int xsltNumConditions;
	public int xsltOccurrences;
	public int javaCodeLOCs = 0;
	public int javaCodeSLOCs = 0;
	public int javaCodeComplexity = 0;
	public int javaCodeNumConditions = 0;
	public int javaCodeNumIterations = 0;	
	public int javaCodeOccurrences = 0;
	public int javaExpressionLOCs = 0;
	public int javaExpressionSLOCs = 0;
	public int javaExpressionComplexity = 0;
	public int javaExpressionNumConditions = 0;
	public int javaExpressionNumIterations = 0;	
	public int javaExpressionOccurrences = 0;
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
	public HalsteadMetrics javaCodeHalstead = new HalsteadMetrics();
	public HalsteadMetrics javaExpressionHalstead = new HalsteadMetrics();

	@Override
	public String toString() {
		return absoluteFileName.getName() + ": XQ=" + (xquerySimpleExpressionLOCs+xquerySimpleQueryLOCs+xqueryExternalLOCs+xqueryReuseLOCs) + ", XSLT=" + (xsltReuseLOCs+xsltExternalLOCs) + ", XPath:" + (xpathExpressionsLOCs+xpathQueriesLOCs);
	}
	
	public void add(FileStats f) {
		xquerySimpleExpressionLOCs += f.xquerySimpleExpressionLOCs;
		xquerySimpleQueryLOCs += f.xquerySimpleQueryLOCs;
		xqueryExternalLOCs += f.xqueryExternalLOCs;
		xqueryReuseLOCs += f.xqueryReuseLOCs;
		
		xpathExpressionsLOCs += f.xpathExpressionsLOCs;
		xpathQueriesLOCs += f.xpathQueriesLOCs;
		xqueryComplexExpressionLOCs += f.xqueryComplexExpressionLOCs;
		xqueryComplexQueryLOCs += f.xqueryComplexQueryLOCs;
		xquerySimpleExpressionOccurrences += f.xquerySimpleExpressionOccurrences;
		xqueryComplexExpressionOccurrences += f.xqueryComplexExpressionOccurrences;
		xquerySimpleQueryOccurrences += f.xquerySimpleQueryOccurrences;
		xqueryComplexQueryOccurrences += f.xqueryComplexQueryOccurrences;
		xqueryNumConditions+= f.xqueryNumConditions;
		xqueryNumIterations += f.xqueryNumIterations;
		xpathQueryOccurrences += f.xpathQueryOccurrences;
		xpathExpressionOccurrences += f.xpathExpressionOccurrences;
		xqueryComplexity += f.xqueryComplexity;
		xsltExternalLOCs += f.xsltExternalLOCs;
		xsltReuseLOCs += f.xsltReuseLOCs;
		xsltComplexity += f.xsltComplexity;
		xsltNumConditions+= f.xsltNumConditions;
		xsltNumIterations += f.xsltNumIterations;
		xsltOccurrences = f.xsltOccurrences;
		javaCodeLOCs += f.javaCodeLOCs;
		javaCodeSLOCs += f.javaCodeSLOCs;
		javaCodeComplexity += f.javaCodeComplexity;
		javaCodeNumConditions += f.javaCodeNumConditions;
		javaCodeNumIterations += f.javaCodeNumIterations;		
		javaCodeOccurrences += f.javaCodeOccurrences;
		javaExpressionLOCs += f.javaExpressionLOCs;
		javaExpressionSLOCs += f.javaExpressionSLOCs;
		javaExpressionComplexity += f.javaExpressionComplexity;
		javaExpressionNumConditions += f.javaExpressionNumConditions;
		javaExpressionNumIterations += f.javaExpressionNumIterations;		
		javaExpressionOccurrences += f.javaExpressionOccurrences;
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
		javaCodeHalstead.add(f.javaCodeHalstead);
		javaExpressionHalstead.add(f.javaExpressionHalstead);
	}
}
