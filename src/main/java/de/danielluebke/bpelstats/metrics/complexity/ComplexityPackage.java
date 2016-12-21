package de.danielluebke.bpelstats.metrics.complexity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.danielluebke.bpelstats.metrics.StatisticsPackage;
import de.danielluebke.bpelstats.saxutil.HandlerMultiplexer;
import de.danielluebke.bpelstats.saxutil.SAXExecutor;

public class ComplexityPackage extends StatisticsPackage {

	private List<BPELComplexityMetricGatherer> handlers;

	public ComplexityPackage() {
		handlers = new ArrayList<BPELComplexityMetricGatherer>();
		handlers.add(new NOAGatherer());
		handlers.add(new NOACGatherer());
		handlers.add(new CFCGatherer());
		handlers.add(new NDGatherer());
		handlers.add(new CWGatherer());
		handlers.add(new FIGatherer());
		handlers.add(new FOGatherer());
	}
	
	@Override
	protected String[] getFieldTitles() {

		String[] fieldTitles = new String[handlers.size()];
		for(int i = 0; i < fieldTitles.length; i++) {
			fieldTitles[i] = handlers.get(i).getName();
		}
				
		return fieldTitles;
	}

	@Override
	protected Object[] calculateStatistic(String bpelFilename)
			throws IOException, SAXException, ParserConfigurationException {
		
		HandlerMultiplexer multiplexer = new HandlerMultiplexer(handlers);
		List<Object> values = new ArrayList<>();
		SAXExecutor.execute(bpelFilename, multiplexer);

		for (BPELComplexityMetricGatherer m : handlers) {
			values.add("" + m.getValue());
		}

		return values.toArray();
	}

}
