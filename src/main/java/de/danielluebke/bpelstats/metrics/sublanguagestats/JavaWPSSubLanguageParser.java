package de.danielluebke.bpelstats.metrics.sublanguagestats;

import de.danielluebke.bpelstats.metrics.sublanguagestats.java.parser.JavaLexer;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;

public class JavaWPSSubLanguageParser {

    private static final List<String> JAVA_COMPLEXITY_STRUCTURES_CONDITION = Collections.unmodifiableList(Arrays.asList("IF", "SWITCH"));
    private static final List<String> JAVA_COMPLEXITY_STRUCTURES_ITERATION = Collections.unmodifiableList(Arrays.asList("WHILE", "FOR"));

    private List<Import> imports = new ArrayList<Import>();

    private File baseDirectory;

    private int complexity = 0;
    private int numConditions = 0;
    private int numIterations = 0;

    private JavaWPSHalsteadMetricsCalculator halsteadCalculator = new JavaWPSHalsteadMetricsCalculator();
    
    public JavaWPSSubLanguageParser(File baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    public void calculateComplexity(String java) {
    	halsteadCalculator.calculateComplexity(java);
    	
        complexity = 0;
        numConditions = 0;
        numIterations = 0;

        JavaLexer lexer = new JavaLexer(CharStreams.fromString(java));
        Vocabulary voc = lexer.getVocabulary();

        for (Token t : lexer.getAllTokens()) {
            if (JAVA_COMPLEXITY_STRUCTURES_CONDITION.contains(voc.getSymbolicName(t.getType()))) {
                numConditions++;
                complexity++;
            } else if (JAVA_COMPLEXITY_STRUCTURES_ITERATION.contains(voc.getSymbolicName(t.getType()))) {
                numIterations++;
                complexity++;
            }
        }
    }

    public List<? extends Import> getImports() {
        return Collections.unmodifiableList(imports);
    }

    public int getComplexity() {
        return complexity;
    }

    public int getNumConditions() {
        return numConditions;
    }

    public int getNumIterations() {
        return numIterations;
    }

	public HalsteadMetrics getHalsteadMetrics() {
		return halsteadCalculator.getHalsteadMetrics();
	}
}
