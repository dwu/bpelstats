package de.danielluebke.bpelstats.metrics.counts;


public interface BpelStatsGatherer {

	public abstract BpelStatsFileResult gather(String bpelFileName) throws Exception;
	
}
