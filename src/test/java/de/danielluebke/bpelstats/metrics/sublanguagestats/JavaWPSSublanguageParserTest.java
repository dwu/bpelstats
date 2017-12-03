package de.danielluebke.bpelstats.metrics.sublanguagestats;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class JavaWPSSublanguageParserTest {

	JavaWPSSubLanguageParser p;

	@Before
	public void init() {
		p = new JavaWPSSubLanguageParser(null);
	}

	@Test
	public void testNoContent() {
		p.calculateComplexity("");
		assertEquals(0, p.getComplexity());
		assertEquals(0, p.getNumConditions());
		assertEquals(0, p.getNumIterations());
	}

	@Test
	public void testIfDontCount() {
		p.calculateComplexity("i++;\nString foo = \"if\";\ni++;");
		assertEquals(0, p.getComplexity());
		assertEquals(0, p.getNumConditions());
		assertEquals(0, p.getNumIterations());
	}

	@Test
	public void testIf() {
		p.calculateComplexity("i++;\nif (\"foo\".equals(\"bar\")) {\ni++;\n}\ni++;");
		assertEquals(1, p.getComplexity());
		assertEquals(1, p.getNumConditions());
		assertEquals(0, p.getNumIterations());
	}

	@Test
	public void testWhile() {
		p.calculateComplexity("i++;\nwhile (\"foo\".equals(\"bar\")) {\ni++;\n}\ni++;");
		assertEquals(1, p.getComplexity());
		assertEquals(0, p.getNumConditions());
		assertEquals(1, p.getNumIterations());
	}

	@Test
	public void testIfWhile() {
		p.calculateComplexity(
				"i++;\nwhile (\"foo\".equals(\"bar\")) {\nif (\"foo\".equals(\"bar\")) {\ni++;\n}\n}\ni++;");
		assertEquals(2, p.getComplexity());
		assertEquals(1, p.getNumConditions());
		assertEquals(1, p.getNumIterations());
	}

}
