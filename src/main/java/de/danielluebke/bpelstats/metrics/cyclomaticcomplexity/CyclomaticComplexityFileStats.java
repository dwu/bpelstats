package de.danielluebke.bpelstats.metrics.cyclomaticcomplexity;

import java.util.ArrayList;
import java.util.List;

public class CyclomaticComplexityFileStats {

	private String fileName;
	private FileType fileType;
	private List<CyclomaticComplexityFunctionStats> functionStats = new ArrayList<>();

	public CyclomaticComplexityFileStats(String absolutePath, FileType fileType) {
		this.fileName = absolutePath;
		this.fileType = fileType;
	}

	public String getFileName() {
		return fileName;
	}
	
	public FileType getFileType() {
		return fileType;
	}
	
	public List<CyclomaticComplexityFunctionStats> getFunctionStats() {
		return functionStats;
	}
	
	public int getCyclomaticComplexity() {
		int cyclomaticComplexity = 0;
		
		for(CyclomaticComplexityFunctionStats functionStat : functionStats) {
			cyclomaticComplexity += functionStat.getCyclomaticComplexity();
		}
		
		return cyclomaticComplexity;
	}
}
