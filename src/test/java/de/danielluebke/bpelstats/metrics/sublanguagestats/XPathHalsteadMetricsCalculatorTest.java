package de.danielluebke.bpelstats.metrics.sublanguagestats;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class XPathHalsteadMetricsCalculatorTest {

	private XPathHalsteadMetricsCalculator calculator = new XPathHalsteadMetricsCalculator();
	
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
		System.out.println(metrics.getTotalNumberOfOperator());
		System.out.println(metrics.getTotalNumberOfOperand());
		
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

}
