package de.danielluebke.bpelstats.metrics.sublanguagestats;

import java.io.File;

public class JavaFileStats {

	public File absoluteFileName;

	public String fileType;

	public int javaLOCs = 0;
	public int javaSLOCs = 0;
	public int javaComplexity = 0;
	public int javaNumConditions = 0;
	public int javaNumIterations = 0;
	public HalsteadMetrics javaHalstead = new HalsteadMetrics();

	@Override
	public String toString() {
		return "JavaFileStats [absoluteFileName=" + absoluteFileName + ", fileType=" + fileType + ", javaLOCs="
				+ javaLOCs + ", javaSLOCs=" + javaSLOCs + ", javaComplexity=" + javaComplexity + ", javaNumConditions="
				+ javaNumConditions + ", javaNumIterations=" + javaNumIterations + ", javaHalstead=" + javaHalstead
				+ "]";
	}

	public void add(JavaFileStats f) {
		javaLOCs += f.javaLOCs;
		javaSLOCs += f.javaSLOCs;
		javaComplexity += f.javaComplexity;
		javaNumConditions += f.javaNumConditions;
		javaNumIterations += f.javaNumIterations;
		javaHalstead.add(f.javaHalstead);
	}
}
