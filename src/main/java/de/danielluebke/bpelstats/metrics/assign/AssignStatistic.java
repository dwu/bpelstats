package de.danielluebke.bpelstats.metrics.assign;

public class AssignStatistic {

	private String assignPath;
	private int copyCount;

	public AssignStatistic(String assignPath) {
		this.assignPath = assignPath;
	}
	
	public String getAssignPath() {
		return assignPath;
	}

	public void setAssignPath(String assignPath) {
		this.assignPath = assignPath;
	}

	public int getCopyCount() {
		return copyCount;
	}

	public void setCopyCount(int copyCount) {
		this.copyCount = copyCount;
	}

	public int incCopyCount() {
		this.copyCount++;
		return copyCount;
	}
}
