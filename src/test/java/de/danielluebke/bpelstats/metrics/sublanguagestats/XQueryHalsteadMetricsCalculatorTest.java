package de.danielluebke.bpelstats.metrics.sublanguagestats;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class XQueryHalsteadMetricsCalculatorTest {

	private XQueryHalsteadMetricsCalculator calculator = new XQueryHalsteadMetricsCalculator();
	
	@Before
	public void setup() {
		calculator.clear();
	}
	
	@Test
	public void testSimpleExpression() throws Exception {
		calculator.parse("/");
		
		HalsteadMetrics metrics = calculator.getHalsteadMetrics();
		
		assertEquals(1, metrics.getUniqueOperators().size());
		assertTrue(metrics.getUniqueOperators().contains("/"));
		assertEquals(1, metrics.getTotalNumberOfOperator("/"));
		assertEquals(0, metrics.getUniqueOperands().size());
	}
	
	@Test
	public void testExpressionWithTextCondition() throws Exception {
		calculator.parse("$x/*[text()='ABC']");
		
		HalsteadMetrics metrics = calculator.getHalsteadMetrics();
		
		assertTrue(metrics.getUniqueOperators().contains("/"));
		assertTrue(metrics.getUniqueOperators().contains("[]"));
		assertTrue(metrics.getUniqueOperators().contains("="));
		assertTrue(metrics.getUniqueOperators().contains("text"));
		assertTrue(metrics.getUniqueOperators().contains("*"));
		assertEquals(1, metrics.getTotalNumberOfOperator("/"));
		assertEquals(1, metrics.getTotalNumberOfOperator("[]"));
		assertEquals(1, metrics.getTotalNumberOfOperator("="));
		assertEquals(1, metrics.getTotalNumberOfOperator("text"));
		assertEquals(1, metrics.getTotalNumberOfOperator("*"));
		assertEquals(5, metrics.getUniqueOperators().size());
		
		assertEquals(2, metrics.getUniqueOperands().size());
		assertEquals(1, metrics.getTotalNumberOfOperand("$x"));
		assertEquals(1, metrics.getTotalNumberOfOperand("'ABC'"));
	}
	
	@Test
	public void testCountAll() throws Exception {
		calculator.parse("count(//*)");
		
		HalsteadMetrics metrics = calculator.getHalsteadMetrics();
		
		assertTrue(metrics.getUniqueOperators().contains("//"));
		assertTrue(metrics.getUniqueOperators().contains("*"));
		assertTrue(metrics.getUniqueOperators().contains("count"));
		assertEquals(1, metrics.getTotalNumberOfOperator("//"));
		assertEquals(1, metrics.getTotalNumberOfOperator("*"));
		assertEquals(1, metrics.getTotalNumberOfOperator("count"));
		assertEquals(3, metrics.getUniqueOperators().size());
		
		assertEquals(0, metrics.getUniqueOperands().size());
	}
	
	@Test
	public void testCountComplex() throws Exception {
		calculator.parse("count(//*[fn:count()=1])");
		
		HalsteadMetrics metrics = calculator.getHalsteadMetrics();
		
		assertTrue(metrics.getUniqueOperators().contains("count"));
		assertTrue(metrics.getUniqueOperators().contains("//"));
		assertTrue(metrics.getUniqueOperators().contains("*"));
		assertTrue(metrics.getUniqueOperators().contains("[]"));
		assertTrue(metrics.getUniqueOperators().contains("fn:count"));
		assertTrue(metrics.getUniqueOperators().contains("="));
		assertEquals(1, metrics.getTotalNumberOfOperator("count"));
		assertEquals(1, metrics.getTotalNumberOfOperator("//"));
		assertEquals(1, metrics.getTotalNumberOfOperator("*"));
		assertEquals(1, metrics.getTotalNumberOfOperator("[]"));
		assertEquals(1, metrics.getTotalNumberOfOperator("fn:count"));
		assertEquals(1, metrics.getTotalNumberOfOperator("="));
		assertEquals(6, metrics.getUniqueOperators().size());
		
		assertEquals(1, metrics.getTotalNumberOfOperand("1"));
		assertEquals(1, metrics.getUniqueOperands().size());
	}
	
	@Test
	public void testQNameElement() throws Exception {
		calculator.parse("$x/ns:a/text()");
		
		HalsteadMetrics metrics = calculator.getHalsteadMetrics();
		
		assertTrue(metrics.getUniqueOperators().contains("/"));
		assertTrue(metrics.getUniqueOperators().contains("text"));
		assertEquals(2, metrics.getTotalNumberOfOperator("/"));
		assertEquals(1, metrics.getTotalNumberOfOperator("text"));
		assertEquals(2, metrics.getUniqueOperators().size());
		
		assertEquals(1, metrics.getTotalNumberOfOperand("$x"));
		assertEquals(1, metrics.getTotalNumberOfOperand("ns:a"));
		assertEquals(2, metrics.getUniqueOperands().size());
	}
	
	@Test
	public void testConcat() throws Exception {
		calculator.parse("concat(a, b)");
		
		HalsteadMetrics metrics = calculator.getHalsteadMetrics();
		
		assertTrue(metrics.getUniqueOperators().contains("concat"));
		assertEquals(1, metrics.getTotalNumberOfOperator("concat"));
		assertEquals(1, metrics.getUniqueOperators().size());
		
		assertEquals(1, metrics.getTotalNumberOfOperand("a"));
		assertEquals(1, metrics.getTotalNumberOfOperand("b"));
		assertEquals(2, metrics.getUniqueOperands().size());
	}
	
	@Test
	public void testDoubleDotParent() throws Exception {
		calculator.parse("//concreteChild/..");
		
		HalsteadMetrics metrics = calculator.getHalsteadMetrics();
		
		assertTrue(metrics.getUniqueOperators().contains("//"));
		assertTrue(metrics.getUniqueOperators().contains("/"));
		assertTrue(metrics.getUniqueOperators().contains(".."));
		assertEquals(1, metrics.getTotalNumberOfOperator("//"));
		assertEquals(1, metrics.getTotalNumberOfOperator("/"));
		assertEquals(1, metrics.getTotalNumberOfOperator(".."));
		assertEquals(3, metrics.getUniqueOperators().size());
		
		assertEquals(1, metrics.getTotalNumberOfOperand("concreteChild"));
		assertEquals(1, metrics.getUniqueOperands().size());
	}

	@Test
	public void testLet() throws Exception {
		calculator.parse("let $x := 1 return $x");
		
		HalsteadMetrics metrics = calculator.getHalsteadMetrics();
		
		assertTrue(metrics.getUniqueOperators().contains(":="));
		assertTrue(metrics.getUniqueOperators().contains("let"));
		assertTrue(metrics.getUniqueOperators().contains("return"));
		assertEquals(1, metrics.getTotalNumberOfOperator(":="));
		assertEquals(1, metrics.getTotalNumberOfOperator("let"));
		assertEquals(1, metrics.getTotalNumberOfOperator("return"));
		assertEquals(3, metrics.getUniqueOperators().size());
		
		assertEquals(2, metrics.getTotalNumberOfOperand("$x"));
		assertEquals(1, metrics.getTotalNumberOfOperand("1"));
		assertEquals(2, metrics.getUniqueOperands().size());
	}
	
	@Test
	public void testLet2() throws Exception {
		calculator.parse("let $x := 1, $y := 2 return $x + $y");
		
		HalsteadMetrics metrics = calculator.getHalsteadMetrics();
		
		assertTrue(metrics.getUniqueOperators().contains(":="));
		assertTrue(metrics.getUniqueOperators().contains("let"));
		assertTrue(metrics.getUniqueOperators().contains("return"));
		assertTrue(metrics.getUniqueOperators().contains("+"));
		assertEquals(2, metrics.getTotalNumberOfOperator(":="));
		assertEquals(1, metrics.getTotalNumberOfOperator("+"));
		assertEquals(1, metrics.getTotalNumberOfOperator("let"));
		assertEquals(1, metrics.getTotalNumberOfOperator("return"));
		assertEquals(4, metrics.getUniqueOperators().size());
		
		assertEquals(2, metrics.getTotalNumberOfOperand("$x"));
		assertEquals(2, metrics.getTotalNumberOfOperand("$y"));
		assertEquals(1, metrics.getTotalNumberOfOperand("1"));
		assertEquals(1, metrics.getTotalNumberOfOperand("2"));
		assertEquals(4, metrics.getUniqueOperands().size());
	}
	
	@Test
	public void testLet3() throws Exception {
		calculator.parse("let $x := 1, $y := 2 return $x - $y");
		
		HalsteadMetrics metrics = calculator.getHalsteadMetrics();
		
		assertTrue(metrics.getUniqueOperators().contains(":="));
		assertTrue(metrics.getUniqueOperators().contains("let"));
		assertTrue(metrics.getUniqueOperators().contains("return"));
		assertTrue(metrics.getUniqueOperators().contains("-"));
		assertEquals(2, metrics.getTotalNumberOfOperator(":="));
		assertEquals(1, metrics.getTotalNumberOfOperator("-"));
		assertEquals(1, metrics.getTotalNumberOfOperator("let"));
		assertEquals(1, metrics.getTotalNumberOfOperator("return"));
		assertEquals(4, metrics.getUniqueOperators().size());
		
		assertEquals(2, metrics.getTotalNumberOfOperand("$x"));
		assertEquals(2, metrics.getTotalNumberOfOperand("$y"));
		assertEquals(1, metrics.getTotalNumberOfOperand("1"));
		assertEquals(1, metrics.getTotalNumberOfOperand("2"));
		assertEquals(4, metrics.getUniqueOperands().size());
	}
	
	@Test
	public void testLet4() throws Exception {
		calculator.parse("let $x := 1, $y := 2 return $x * $y");
		
		HalsteadMetrics metrics = calculator.getHalsteadMetrics();
		
		System.out.println(metrics.getTotalNumberOfOperator());
		System.out.println(metrics.getTotalNumberOfOperand());
		
		assertTrue(metrics.getUniqueOperators().contains(":="));
		assertTrue(metrics.getUniqueOperators().contains("let"));
		assertTrue(metrics.getUniqueOperators().contains("return"));
		assertTrue(metrics.getUniqueOperators().contains("*"));
		assertEquals(2, metrics.getTotalNumberOfOperator(":="));
		assertEquals(1, metrics.getTotalNumberOfOperator("*"));
		assertEquals(1, metrics.getTotalNumberOfOperator("let"));
		assertEquals(1, metrics.getTotalNumberOfOperator("return"));
		assertEquals(4, metrics.getUniqueOperators().size());
		
		assertEquals(2, metrics.getTotalNumberOfOperand("$x"));
		assertEquals(2, metrics.getTotalNumberOfOperand("$y"));
		assertEquals(1, metrics.getTotalNumberOfOperand("1"));
		assertEquals(1, metrics.getTotalNumberOfOperand("2"));
		assertEquals(4, metrics.getUniqueOperands().size());
	}
	
	@Test
	public void testFor() throws Exception {
		calculator.parse("for $i in (1, 2, 4) return $i;");
		
		HalsteadMetrics metrics = calculator.getHalsteadMetrics();
		
		System.out.println(metrics.getTotalNumberOfOperator());
		System.out.println(metrics.getTotalNumberOfOperand());
		
		assertEquals(1, metrics.getTotalNumberOfOperator("for"));
		assertEquals(1, metrics.getTotalNumberOfOperator("in"));
		assertEquals(1, metrics.getTotalNumberOfOperator("()"));
		assertEquals(1, metrics.getTotalNumberOfOperator("return"));
		assertEquals(4, metrics.getUniqueOperators().size());
		
		assertEquals(2, metrics.getTotalNumberOfOperand("$i"));
		assertEquals(1, metrics.getTotalNumberOfOperand("1"));
		assertEquals(1, metrics.getTotalNumberOfOperand("2"));
		assertEquals(1, metrics.getTotalNumberOfOperand("4"));
		assertEquals(4, metrics.getUniqueOperands().size());
	}
	
	@Test
	public void testIf() throws Exception {
		calculator.parse("if(1 = 1) then 'a' else 'b';");
		
		HalsteadMetrics metrics = calculator.getHalsteadMetrics();
		
		assertEquals(1, metrics.getTotalNumberOfOperator("if"));
		assertEquals(1, metrics.getTotalNumberOfOperator("then"));
		assertEquals(1, metrics.getTotalNumberOfOperator("else"));
		assertEquals(1, metrics.getTotalNumberOfOperator("="));
		assertEquals(4, metrics.getUniqueOperators().size());
		
		assertEquals(2, metrics.getTotalNumberOfOperand("1"));
		assertEquals(1, metrics.getTotalNumberOfOperand("'a'"));
		assertEquals(1, metrics.getTotalNumberOfOperand("'b'"));
		assertEquals(3, metrics.getUniqueOperands().size());
	}
	
	@Test
	public void testFunction() throws Exception {
		calculator.parse("declare function local:myfunction($x as xs:string, $y as xs:string) as xs:string { $x }; local:myfunction('a');");
		
		HalsteadMetrics metrics = calculator.getHalsteadMetrics();
		
		System.out.println(metrics.getTotalNumberOfOperator());
		System.out.println(metrics.getTotalNumberOfOperand());
		
		assertEquals(1, metrics.getTotalNumberOfOperator("declare function"));
		assertEquals(2, metrics.getTotalNumberOfOperator("local:myfunction"));
		assertEquals(2, metrics.getUniqueOperators().size());
		
		assertEquals(2, metrics.getTotalNumberOfOperand("$x"));
		assertEquals(1, metrics.getTotalNumberOfOperand("$y"));
		assertEquals(3, metrics.getTotalNumberOfOperand("xs:string"));
		assertEquals(1, metrics.getTotalNumberOfOperand("'a'"));
		assertEquals(4, metrics.getUniqueOperands().size());
	}

	@Test
	public void testFunctionWithLists() throws Exception {
		calculator.parse("declare function local:myfunction($x as xs:string?, $y as xs:string*, $z as xs:string+) as xs:string { $x }; local:myfunction('a');");
		
		HalsteadMetrics metrics = calculator.getHalsteadMetrics();
		
		assertEquals(1, metrics.getTotalNumberOfOperator("declare function"));
		assertEquals(2, metrics.getTotalNumberOfOperator("local:myfunction"));
		assertEquals(1, metrics.getTotalNumberOfOperator("?"));
		assertEquals(1, metrics.getTotalNumberOfOperator("*"));
		assertEquals(1, metrics.getTotalNumberOfOperator("+"));
		assertEquals(5, metrics.getUniqueOperators().size());
		
		assertEquals(2, metrics.getTotalNumberOfOperand("$x"));
		assertEquals(1, metrics.getTotalNumberOfOperand("$y"));
		assertEquals(1, metrics.getTotalNumberOfOperand("$z"));
		assertEquals(4, metrics.getTotalNumberOfOperand("xs:string"));
		assertEquals(1, metrics.getTotalNumberOfOperand("'a'"));
		assertEquals(5, metrics.getUniqueOperands().size());
	}

}
