package de.danielluebke.bpelstats.metrics.sublanguagestats;


public class LOCCalculator {

	public static int calculateLOC(String source) {
		if(source == null) return 0;
		
		String[] lines = source.replaceAll("\r", "\n").split("\n");
		int locCount = 0;
		for(String line : lines) {
			if(!"".equals(line.trim())) {
				locCount++;
			}
		}
		return locCount;
	}
	
}
