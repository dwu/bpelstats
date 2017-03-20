package de.danielluebke.bpelstats.metrics.complexity;

import org.xml.sax.helpers.DefaultHandler;

public abstract class BPELComplexityMetricGatherer extends DefaultHandler {

	public abstract String getName();
	public abstract String getValue();
	
}
