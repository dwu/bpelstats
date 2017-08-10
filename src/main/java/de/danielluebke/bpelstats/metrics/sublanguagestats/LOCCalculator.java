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
	
	public static int calculateSLOC(String source) {
		if(source == null) return 0;
		
		String[] lines = source.replaceAll("\r", "\n").split("\n");
		
		boolean inComment = false;
		int slocCount = 0;
		for(String line : lines) {
			String l = line.trim();
			
			if("".equals(l))
				continue;
			
			if(l.contains("/*")) {
				inComment = true;
				if (l.contains("*/")) {
					inComment = false;
				}
				if (!(l.startsWith("/*") && (!l.contains("*/") || l.endsWith("*/")))) {
					slocCount++;
				}
				continue;
			}
			if(l.contains("*/")) {
				if (!l.endsWith("/*") && !inComment) {
					slocCount++;
				}
				inComment = false;
				continue;
			}
			if(inComment || l.startsWith("//"))
				continue;

			// else
			slocCount++;
		}
		
		return slocCount;
	}
	
}
