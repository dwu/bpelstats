package de.danielluebke.bpelstats.metrics.assign;

import java.io.FileInputStream;
import java.io.IOException;

import de.danielluebke.bpelstats.metrics.StatisticsPackage;
import de.danielluebke.bpelstats.saxutil.SAXExecutor;

public class AssignStatsPackage extends StatisticsPackage {

	@Override
	protected String[] getFieldTitles() {
		return new String[]{ "Assign", "Copy" };
	}

	@Override
	protected void printStatistics() throws IOException {
		for(String bpelFilename : bpelFilenames) {
			
			try {
				CopyPerAssignGatherer gatherer = new CopyPerAssignGatherer();
				SAXExecutor.execute(new FileInputStream(bpelFilename), gatherer);
			
				for(AssignStatistic assign : gatherer.getAssigns()) {
					writer.write(anonymizeFilenameIfNecessary(bpelFilename));
					writer.write(delimiter);
					writer.write(anonymizeFilenameIfNecessary(assign.getAssignPath()));
					writer.write(delimiter);
					writer.write("" + assign.getCopyCount());
					writer.write("\n");
				}
			} catch(Exception e) {
				throw new IOException(e);
			}
		}
	}
	
	@Override
	protected Object[] calculateStatistic(String bpelFilename) throws Exception {
		return null;
	}

}
