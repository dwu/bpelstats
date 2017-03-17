package de.danielluebke.bpelstats.metrics.cyclomaticcomplexity;

public class CyclomaticComplexityFunctionStats {

	private String functionName;
	private int cyclomaticComplexity;
	
	public CyclomaticComplexityFunctionStats(String functionName) {
		this.functionName = functionName;
		this.cyclomaticComplexity = 1;
	}

	public int getCyclomaticComplexity() {
		return cyclomaticComplexity;
	}
	
	public void incCyclomaticComplexity() {
		cyclomaticComplexity++;
	}

	public void decCyclomaticComplexity() {
		cyclomaticComplexity--;
	}
	
	public void setCyclomaticComplexity(int cyclomaticComplexity) {
		this.cyclomaticComplexity = cyclomaticComplexity;
	}
	
	public String getFunctionName() {
		return functionName;
	}
}
