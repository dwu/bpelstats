package de.danielluebke.bpelstats.metrics.sublanguagestats;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;

import de.danielluebke.bpelstats.metrics.sublanguagestats.java.parser.JavaLexer;

public class JavaWPSHalsteadMetricsCalculator implements IHalsteadMetricsCalculator {

	private HalsteadMetrics metrics = new HalsteadMetrics();
	
	private JavaLexer lexer;
	private Vocabulary voc;
	
	public void calculateComplexity(String javaSource) {
		lexer = new JavaLexer(CharStreams.fromString(javaSource));
		voc = lexer.getVocabulary();
		
		for (Token t : lexer.getAllTokens()) {
			int type = t.getType();
			
			if (type >= 1 && type <= 50 || type >= 66 && type <= 102) {
				// Keywords & Operators
				metrics.addOperator("java:" + voc.getSymbolicName(t.getType()).toLowerCase() + ":" + t.getText());
			} else if (type >= 51 && type <= 56 || type == 103) {
				// Identifiers & Literals
				metrics.addOperand("java:" + voc.getSymbolicName(t.getType()).toLowerCase() + ":" + t.getText());
			}
//			else {
//				System.err.println(String.format("Undefined token: %s, %s", voc.getSymbolicName(t.getType()).toLowerCase(), t.getText()));
//			}
		}
	}

	@Override
	public HalsteadMetrics getHalsteadMetrics() {
		return metrics;
	}

	public void clear() {
		metrics.clear();
	}
}
