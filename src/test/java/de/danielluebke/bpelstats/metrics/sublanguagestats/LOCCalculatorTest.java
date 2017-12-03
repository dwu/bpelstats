package de.danielluebke.bpelstats.metrics.sublanguagestats;

import static org.junit.Assert.*;

import org.junit.Test;

public class LOCCalculatorTest {

	@Test
	public void testNoContent() {
		assertEquals(0, LOCCalculator.calculateLOC(""));
		assertEquals(0, LOCCalculator.calculateLOC(null));
	}
	
	@Test
	public void testSingleLine() {
		assertEquals(1, LOCCalculator.calculateLOC("123"));
	}
	
	@Test
	public void testTwoLines_LineBreak() {
		assertEquals(2, LOCCalculator.calculateLOC("123\n456"));
	}
	
	@Test
	public void testTwoLines_LineBreakLineFeed() {
		assertEquals(2, LOCCalculator.calculateLOC("123\r\n456"));
	}
	
	@Test
	public void testSkipEmptyLines() {
		assertEquals(0, LOCCalculator.calculateLOC("\n\n"));
		assertEquals(2, LOCCalculator.calculateLOC("1\n\n2"));
	}
	
	@Test
	public void testSkipEmptyLinesWithWhiteSpaces() {
		assertEquals(0, LOCCalculator.calculateLOC(" \n \n "));
		assertEquals(2, LOCCalculator.calculateLOC("1 \n \n 2 \n "));
	}

	@Test
	public void testSLOCMultilineCommentsAreCalculatedCorrectlyIfTheyBeginAtStartOfLine() {
		String java = "int i = 0; \n/* \n asd \n */";
		
		int sloc = LOCCalculator.calculateJavaSLOC(java);
		assertEquals(1, sloc);
	}
	
	@Test
	public void testSLOCOnlySingleLineComment() {
		String java = "// comment";
		
		int sloc = LOCCalculator.calculateJavaSLOC(java);
		assertEquals(0, sloc);
	}
	@Test
	public void testSLOCOnlyMultiLineComment() {
		String java = "/* Test */";
		
		int sloc = LOCCalculator.calculateJavaSLOC(java);
		assertEquals(0, sloc);
	}
	
	@Test
	public void testSLOCMultilineCommentsAreCalculatedCorrectlyIfTheyDoNotEndAtEndOfLine() {
		String java = "int i = 0; \n/* \n asd \n */ i++;";
		
		int sloc = LOCCalculator.calculateJavaSLOC(java);
		assertEquals(2, sloc);
	}
	
	@Test
	public void testSLOCMultilineCommentsAreCalculatedCorrectlyIfTheyAreNotBeginningAtStartOfLine() {
		String java = "int i = 0; /* \n asd \n */";
		
		int sloc = LOCCalculator.calculateJavaSLOC(java);
		assertEquals(1, sloc);
	}

}
