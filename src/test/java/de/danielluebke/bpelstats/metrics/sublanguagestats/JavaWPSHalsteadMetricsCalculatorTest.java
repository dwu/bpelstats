package de.danielluebke.bpelstats.metrics.sublanguagestats;

import org.junit.Test;

public class JavaWPSHalsteadMetricsCalculatorTest {

	@Test
	public void test() throws Exception {
		String java = "if (\"foo\" == 123) { int x = 10; String y = \"foo\"; int x = 11;}";
		JavaWPSHalsteadMetricsCalculator calculator = new JavaWPSHalsteadMetricsCalculator();
		
		calculator.calculateComplexity(java);
		
		HalsteadMetrics halsteadMetrics = calculator.getHalsteadMetrics();
		System.out.println(halsteadMetrics.getTotalNumberOfOperand());
		System.out.println(halsteadMetrics.getUniqueOperands());

		System.out.println(halsteadMetrics.getTotalNumberOfOperator());
		System.out.println(halsteadMetrics.getUniqueOperators());
	}
	
}
