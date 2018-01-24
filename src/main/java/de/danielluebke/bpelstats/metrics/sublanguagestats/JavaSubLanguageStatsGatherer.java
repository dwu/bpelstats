package de.danielluebke.bpelstats.metrics.sublanguagestats;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

import es.uca.webservices.xquery.parser.util.XQueryParsingException;

public class JavaSubLanguageStatsGatherer {

	public JavaFileStats gather(File javaFile) throws IOException, SAXException, ParserConfigurationException, XQueryParsingException {
		String javaFileContents = FileUtils.readFileToString(javaFile);

		JavaWPSSubLanguageParser javaParser = new JavaWPSSubLanguageParser(javaFile.getParentFile());

		javaParser.calculateComplexity(javaFileContents);

		JavaFileStats javaFileStats = new JavaFileStats();
		javaFileStats.fileType = "JAVA";
		javaFileStats.absoluteFileName = javaFile.getAbsoluteFile();
		javaFileStats.javaLOCs = LOCCalculator.calculateLOC(javaFileContents);
		javaFileStats.javaSLOCs = LOCCalculator.calculateJavaSLOC(javaFileContents);
		javaFileStats.javaComplexity = javaParser.getComplexity();
		javaFileStats.javaNumConditions = javaParser.getNumConditions();
		javaFileStats.javaNumIterations = javaParser.getNumIterations();

		javaParser.getHalsteadMetrics().finalize();
		javaFileStats.javaHalstead = javaParser.getHalsteadMetrics();

		return javaFileStats;
	}


}
