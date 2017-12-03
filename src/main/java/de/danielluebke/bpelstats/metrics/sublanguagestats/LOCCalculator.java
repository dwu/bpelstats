package de.danielluebke.bpelstats.metrics.sublanguagestats;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import de.danielluebke.bpelstats.metrics.sublanguagestats.java.parser.JavaLexer;

public class LOCCalculator {

	public static int calculateLOC(String source) {
		if(source == null) return 0;
		
		String[] lines = source.replaceAll("\r", "\n").split("\n");
		int locCount = 0;
		for(String line : lines) {
			if(!"".equals(line.trim())) {
				locCount++;
			}
		}
		return locCount;
	}
	
	public static int calculateJavaSLOC(String source) {
		if(source == null) return 0;
		
		JavaLexer lexer = new JavaLexer(CharStreams.fromString(source));

		int lastTokenLine = 0;
		int slocCount = 0;
		
		for (Token t : lexer.getAllTokens()) {
			// not COMMENT or LINE_COMMENT
			if (t.getType() != 107 && t.getType() != 108) {
				if (t.getLine() > lastTokenLine) {
					slocCount++;
					lastTokenLine = t.getLine();
				}
			}
		}
		
		return slocCount;
	}
	
}
