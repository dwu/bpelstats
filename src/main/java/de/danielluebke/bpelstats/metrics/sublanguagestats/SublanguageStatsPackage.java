package de.danielluebke.bpelstats.metrics.sublanguagestats;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import org.xml.sax.SAXException;

import de.danielluebke.bpelstats.metrics.StatisticsPackage;

public class SublanguageStatsPackage extends StatisticsPackage {

	private String reuseDir;

	public void setReuseDir(String reuseDir) {
		this.reuseDir = reuseDir;
	}
	
	@Override
	protected String[] getFieldTitles() {
		return new String[] {
				"XPath/Expression(LOCs)", 
				"XPath/Expression(Occurrences)",
				"XPath/Query(LOCs)", 
				"XPath/Query(Occurrences)",
				"XPath/Total(LOCs)",
				"XPath/Total(Occurrences)",
				"XQuery/Expression/Simple(LOCs)",
				"XQuery/Expression/Simple(Occurrences)",
				"XQuery/Expression/Complex(LOCs)",
				"XQuery/Expression/Complex(Occurrences)",
				"XQuery/Expression/Total(LOCs)",
				"XQuery/Expression/Total(Occurrences)",
				"XQuery/Query/Simple(LOCs)",
				"XQuery/Query/Simple(Occurrences)",
				"XQuery/Query/Complex(LOCs)",
				"XQuery/Query/Complex(Occurrences)",
				"XQuery/Query/Total(LOCs)", 
				"XQuery/Query/Total(Occurrences)",
				"XQuery/External(LOCs)", 
				"XQuery/Reuse(LOCs)",
				"XQuery/Total(LOCs)", 
				"XQuery/Complexity",
				"XQuery/Complexity(#Conditions)",
				"XQuery/Complexity(#Iterations)",
				"XSLT/External", 
				"XSLT/Reuse", 
				"XSLT/Total",
				"XSLT/Complexity",
				"XSLT/Complexity(#Conditions)",
				"XSLT/Complexity(#Iterations)",
				"XSLT/Occurrences",
				"XMLMap/Occurrences",
				"BOMap/LOCs",
				"BOMap/Complexity",
				"BOMap/Complexity(#Conditions)",
				"BOMap/Complexity(#Iterations)",
				"BOMap/Occurrences",
				"Java/Code/LOCs",
				"Java/Code/SLOCs",
				"Java/Code/Complexity",
				"Java/Code/Complexity(#Conditions)",
				"Java/Code/Complexity(#Iterations)",
				"Java/Code/Occurrences",
				"Java/Expression/LOCs",
				"Java/Expression/SLOCs",
				"Java/Expression/Complexity",
				"Java/Expression/Complexity(#Conditions)",
				"Java/Expression/Complexity(#Iterations)",
				"Java/Expression/Occurrences",				
				"Java/Total/LOCs",
				"Java/Total/SLOCs",
				"Java/Total/Complexity",
				"Java/Total/Complexity(#Conditions)",
				"Java/Total/Complexity(#Iterations)",
				"Java/Total/Occurrences",				
				"WPSBuiltIn/Occurrences",
				"XPath/Halstead/Operands",
				"XPath/Halstead/Operators",
				"XPath/Halstead/ProgramLength",
				"XPath/Halstead/VocabularySize",
				"XPath/Halstead/Volume",
				"XPath/Halstead/Difficulty",
				"XPath/Halstead/Effort",
				"XPath/Halstead/Effort(s)",
				"XPath/Halstead/EstimatedBugs(Modern)",
				"XSLT/Halstead/Operands",
				"XSLT/Halstead/Operators",
				"XSLT/Halstead/ProgramLength",
				"XSLT/Halstead/VocabularySize",
				"XSLT/Halstead/Volume",
				"XSLT/Halstead/Difficulty",
				"XSLT/Halstead/Effort",
				"XSLT/Halstead/Effort(s)",
				"XSLT/Halstead/EstimatedBugs(Modern)",
				"XQuery/Halstead/Operands",
				"XQuery/Halstead/Operators",
				"XQuery/Halstead/ProgramLength",
				"XQuery/Halstead/VocabularySize",
				"XQuery/Halstead/Volume",
				"XQuery/Halstead/Difficulty",
				"XQuery/Halstead/Effort",
				"XQuery/Halstead/Effort(s)",
				"XQuery/Halstead/EstimatedBugs(Modern)",
				"BOMap/Halstead/Operands",
				"BOMap/Halstead/Operators",
				"BOMap/Halstead/ProgramLength",
				"BOMap/Halstead/VocabularySize",
				"BOMap/Halstead/Volume",
				"BOMap/Halstead/Difficulty",
				"BOMap/Halstead/Effort",
				"BOMap/Halstead/Effort(s)",
				"BOMap/Halstead/EstimatedBugs(Modern)",
				"Java/Code/Halstead/Operands",
				"Java/Code/Halstead/Operators",
				"Java/Code/Halstead/ProgramLength",
				"Java/Code/Halstead/VocabularySize",
				"Java/Code/Halstead/Volume",
				"Java/Code/Halstead/Difficulty",
				"Java/Code/Halstead/Effort",
				"Java/Code/Halstead/Effort(s)",
				"Java/Code/Halstead/EstimatedBugs(Modern)",
				"Java/Expression/Halstead/Operands",
				"Java/Expression/Halstead/Operators",
				"Java/Expression/Halstead/ProgramLength",
				"Java/Expression/Halstead/VocabularySize",
				"Java/Expression/Halstead/Volume",
				"Java/Expression/Halstead/Difficulty",
				"Java/Expression/Halstead/Effort",
				"Java/Expression/Halstead/Effort(s)",
				"Java/Expression/Halstead/EstimatedBugs(Modern)",
				"Java/Total/Halstead/Operands",
				"Java/Total/Halstead/Operators",
				"Java/Total/Halstead/ProgramLength",
				"Java/Total/Halstead/VocabularySize",
				"Java/Total/Halstead/Volume",
				"Java/Total/Halstead/Difficulty",
				"Java/Total/Halstead/Effort",
				"Java/Total/Halstead/Effort(s)",
				"Java/Total/Halstead/EstimatedBugs(Modern)",				
				"Dependencies"
		};
	}

	@Override
	protected Object[] calculateStatistic(String bpelFilename) throws Exception {
		BpelSubLanguageStatsGatherer gatherer = new BpelSubLanguageStatsGatherer();
		File bpelFile = new File(bpelFilename);

		String[] reuseDirs;
		if (reuseDir  == null || "".equals(reuseDir)) {
			reuseDirs = new String[0];
		} else {
			reuseDirs = reuseDir.split(",");
		}
		List<FileStats> sublanguageResults = gatherer.gather(bpelFile,
				reuseDirs);
		StringBuilder importFiles = new StringBuilder();

		FileStats total = new FileStats();
		for (FileStats fs : sublanguageResults) {
			total.add(fs);
			if (importFiles.length() > 0) {
				importFiles.append(getSecondaryDelimiter());
			}

			String path = getRelativePath(
					fs.absoluteFileName.getCanonicalPath(), bpelFile
							.getParentFile().getCanonicalPath());
			if (path.indexOf("..") >= 0) {
				path = path.substring(path.indexOf(".."));
			}
			importFiles.append(anonymizeFilenameIfNecessary(path));
		}

		return new Object[] {
			total.xpathExpressionsLOCs,
			total.xpathExpressionOccurrences,
			total.xpathQueriesLOCs,
			total.xpathQueryOccurrences,
			(total.xpathExpressionsLOCs + total.xpathQueriesLOCs),
			(total.xpathExpressionOccurrences + total.xpathQueryOccurrences),
			total.xquerySimpleExpressionLOCs,
			total.xquerySimpleExpressionOccurrences,
			total.xqueryComplexExpressionLOCs,
			total.xqueryComplexExpressionOccurrences,
			(total.xqueryComplexExpressionLOCs + total.xquerySimpleExpressionLOCs),
			(total.xqueryComplexExpressionOccurrences + total.xquerySimpleExpressionOccurrences),
			total.xquerySimpleQueryLOCs,
			total.xquerySimpleQueryOccurrences,
			total.xqueryComplexQueryLOCs,
			total.xqueryComplexQueryOccurrences,
			(total.xqueryComplexQueryLOCs + total.xquerySimpleQueryLOCs),
			(total.xqueryComplexQueryOccurrences + total.xquerySimpleExpressionOccurrences),
			total.xqueryExternalLOCs,
			total.xqueryReuseLOCs,
			(total.xqueryExternalLOCs + total.xquerySimpleQueryLOCs + total.xqueryComplexQueryLOCs + total.xquerySimpleExpressionLOCs + total.xqueryComplexExpressionLOCs + total.xqueryReuseLOCs),
			total.xqueryComplexity,
			total.xqueryNumConditions,
			total.xqueryNumIterations,
			total.xsltExternalLOCs,
			total.xsltReuseLOCs,
			(total.xsltExternalLOCs + total.xsltReuseLOCs),
			total.xsltComplexity,
			total.xsltNumConditions,
			total.xsltNumIterations,
			total.xsltOccurrences,
			total.xmlMapOccurrences,
			total.boMapLOCs,
			total.boMapComplexity,
			total.boMapNumConditions,
			total.boMapNumIterations,
			total.boMapOccurrences,
			total.javaCodeLOCs,
			total.javaCodeSLOCs,
			total.javaCodeComplexity,
			total.javaCodeNumConditions,
			total.javaCodeNumIterations,
			total.javaCodeOccurrences,
			total.javaExpressionLOCs,
			total.javaExpressionSLOCs,
			total.javaExpressionComplexity,
			total.javaExpressionNumConditions,
			total.javaExpressionNumIterations,
			total.javaExpressionOccurrences,
			(total.javaCodeLOCs + total.javaExpressionLOCs),
			(total.javaCodeSLOCs + total.javaExpressionSLOCs),
			(total.javaCodeComplexity + total.javaExpressionComplexity),
			(total.javaCodeNumConditions + total.javaExpressionNumConditions),
			(total.javaCodeNumIterations + total.javaExpressionNumIterations),
			(total.javaCodeOccurrences + total.javaExpressionOccurrences),
			total.wpsBuiltInOccurrences,
			total.xpathHalstead.getUniqueOperands().size(),
			total.xpathHalstead.getUniqueOperators().size(),
			total.xpathHalstead.getProgramLength(),
			total.xpathHalstead.getVocabularySize(),
			total.xpathHalstead.getVolume(),
			total.xpathHalstead.getDifficulty(),
			total.xpathHalstead.getEffort(),
			total.xpathHalstead.getEstimatedEffortInSeconds(),
			total.xpathHalstead.getModernEstimatedBugs(),
			total.xsltHalstead.getUniqueOperands().size(),
			total.xsltHalstead.getUniqueOperators().size(),
			total.xsltHalstead.getProgramLength(),
			total.xsltHalstead.getVocabularySize(),
			total.xsltHalstead.getVolume(),
			total.xsltHalstead.getDifficulty(),
			total.xsltHalstead.getEffort(),
			total.xsltHalstead.getEstimatedEffortInSeconds(),
			total.xsltHalstead.getModernEstimatedBugs(),
			total.xqueryHalstead.getUniqueOperands().size(),
			total.xqueryHalstead.getUniqueOperators().size(),
			total.xqueryHalstead.getProgramLength(),
			total.xqueryHalstead.getVocabularySize(),
			total.xqueryHalstead.getVolume(),
			total.xqueryHalstead.getDifficulty(),
			total.xqueryHalstead.getEffort(),
			total.xqueryHalstead.getEstimatedEffortInSeconds(),
			total.xqueryHalstead.getModernEstimatedBugs(),
			total.bomapHalstead.getUniqueOperands().size(),
			total.bomapHalstead.getUniqueOperators().size(),
			total.bomapHalstead.getProgramLength(),
			total.bomapHalstead.getVocabularySize(),
			total.bomapHalstead.getVolume(),
			total.bomapHalstead.getDifficulty(),
			total.bomapHalstead.getEffort(),
			total.bomapHalstead.getEstimatedEffortInSeconds(),
			total.bomapHalstead.getModernEstimatedBugs(),
			total.javaCodeHalstead.getUniqueOperands().size(),
			total.javaCodeHalstead.getUniqueOperators().size(),
			total.javaCodeHalstead.getProgramLength(),
			total.javaCodeHalstead.getVocabularySize(),
			total.javaCodeHalstead.getVolume(),
			total.javaCodeHalstead.getDifficulty(),
			total.javaCodeHalstead.getEffort(),
			total.javaCodeHalstead.getEstimatedEffortInSeconds(),
			total.javaCodeHalstead.getModernEstimatedBugs(),
			total.javaExpressionHalstead.getUniqueOperands().size(),
			total.javaExpressionHalstead.getUniqueOperators().size(),
			total.javaExpressionHalstead.getProgramLength(),
			total.javaExpressionHalstead.getVocabularySize(),
			total.javaExpressionHalstead.getVolume(),
			total.javaExpressionHalstead.getDifficulty(),
			total.javaExpressionHalstead.getEffort(),
			total.javaExpressionHalstead.getEstimatedEffortInSeconds(),
			total.javaExpressionHalstead.getModernEstimatedBugs(),
			(total.javaCodeHalstead.getUniqueOperands().size() + total.javaExpressionHalstead.getUniqueOperands().size()),
			(total.javaCodeHalstead.getUniqueOperators().size() + total.javaExpressionHalstead.getUniqueOperators().size()),
			(total.javaCodeHalstead.getProgramLength() + total.javaExpressionHalstead.getProgramLength()),
			(total.javaCodeHalstead.getVocabularySize() + total.javaExpressionHalstead.getVocabularySize()),
			(total.javaCodeHalstead.getVolume() + total.javaExpressionHalstead.getVolume()),
			(total.javaCodeHalstead.getDifficulty() + total.javaExpressionHalstead.getDifficulty()),
			(total.javaCodeHalstead.getEffort() + total.javaExpressionHalstead.getEffort()),
			(total.javaCodeHalstead.getEstimatedEffortInSeconds() + total.javaExpressionHalstead.getEstimatedEffortInSeconds()),
			(total.javaCodeHalstead.getModernEstimatedBugs() + total.javaExpressionHalstead.getModernEstimatedBugs()),
			importFiles
		};
	}

	private String getRelativePath(String targetPath, String basePath) throws SAXException {

		String[] base = basePath.split(Pattern.quote(File.separator));
		String[] target = targetPath.split(Pattern.quote(File.separator));

		// First get all the common elements. Store them as a string,
		// and also count how many of them there are.
		StringBuffer common = new StringBuffer();

		int commonIndex = 0;
		while (commonIndex < target.length && commonIndex < base.length
				&& target[commonIndex].equals(base[commonIndex])) {
			common.append(target[commonIndex] + File.separator);
			commonIndex++;
		}

		if (commonIndex == 0) {
			// No single common path element. This most
			// likely indicates differing drive letters, like C: and D:.
			// These paths cannot be relativized.
			throw new SAXException("No common path element found for '"
					+ targetPath + "' and '" + basePath + "'");
		}

		boolean baseIsFile = true;

		File baseResource = new File(basePath);

		if (baseResource.exists()) {
			baseIsFile = baseResource.isFile();

		} else if (basePath.endsWith(File.separator)) {
			baseIsFile = false;
		}

		StringBuffer relative = new StringBuffer();

		if (base.length != commonIndex) {
			int numDirsUp = baseIsFile ? base.length - commonIndex - 1
					: base.length - commonIndex;

			for (int i = 0; i < numDirsUp; i++) {
				relative.append(".." + File.separator);
			}
		}
		relative.append(targetPath.substring(common.length()));
		return relative.toString();
	}
	
}
