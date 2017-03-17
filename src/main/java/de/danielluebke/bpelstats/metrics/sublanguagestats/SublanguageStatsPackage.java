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
				"XPath/Expression(LOCs)", "XPath/Expression(Occurrences)",
				"XPath/Query(LOCs)", "XPath/Query(Occurrences)",
				"XPath/Total(LOCs)", "XPath/Total(Occurrences)",
				"XSLT/External", "XSLT/Reuse", "XSLT/Total",
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
				"XQuery/Query/Total(LOCs)", "XQuery/Query/Total(Occurrences)",
				"XQuery/External(LOCs)", "XQuery/Reuse(LOCs)",
				"XQuery/Total(LOCs)", "XQuery/Complexity", "XSLT/Complexity",
				"Java/LOCs",
				"Java/Occurrences",
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
			total.xsltExternalLOCs,
			total.xsltReuseLOCs,
			(total.xsltExternalLOCs + total.xsltReuseLOCs),
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
			total.xsltComplexity, 
			total.javaExpressionLOCs,
			total.javaExpressionOccurrences,
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
