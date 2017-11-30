package de.danielluebke.bpelstats.metrics.sublanguagestats;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.danielluebke.bpelstats.util.Counters;

public class HalsteadMetrics {

	private Set<String> uniqueOperators = new HashSet<>();
	private Counters<String> totalNumberOfOperator = new Counters<String>();
	private Set<String> uniqueOperands = new HashSet<>();
	private Counters<String> totalNumberOfOperand = new Counters<String>();
	private int totalNumberOfOperators = -1;
	private int totalNumberOfOperands = -1;

	public void clear() {
		uniqueOperators.clear();
		totalNumberOfOperator.clear();
		uniqueOperands.clear();
		totalNumberOfOperand.clear();
		
		totalNumberOfOperators = -1;
		totalNumberOfOperands = -1;
	}

	public void addOperator(String operator) {
		uniqueOperators.add(operator);
		totalNumberOfOperator.inc(operator);
	}

	public void addOperand(String operand) {
		uniqueOperands.add(operand);
		totalNumberOfOperand.inc(operand);
	}

	public Set<String> getUniqueOperands() {
		return uniqueOperands;
	}
	
	public Set<String> getUniqueOperators() {
		return uniqueOperators;
	}
	
	public Counters<String> getTotalNumberOfOperand() {
		return totalNumberOfOperand;
	}
	
	public Counters<String> getTotalNumberOfOperator() {
		return totalNumberOfOperator;
	}

	public int getTotalNumberOfOperand(String operand) {
		return totalNumberOfOperand.get(operand);
	}
	
	public int getTotalNumberOfOperator(String operator) {
		return totalNumberOfOperator.get(operator);
	}

	public void addOperand(String operand, int n) {
		uniqueOperands.add(operand);
		totalNumberOfOperand.inc(operand, n);
	}

	public void addOperator(String operator, int n) {
		uniqueOperators.add(operator);
		totalNumberOfOperator.inc(operator, n);
	}
	
	public void finalize() {
		List<String> toRemove = new ArrayList<>();
		
		toRemove.clear();
		totalNumberOfOperators = 0;
		for(String operator : uniqueOperators) {
			int n = totalNumberOfOperator.get(operator);
			if(n == 0) {
				toRemove.add(operator);
			} else {
				totalNumberOfOperators += n;
			}
		}
		uniqueOperators.removeAll(toRemove);
		
		toRemove.clear();
		totalNumberOfOperands = 0;
		for(String operand : uniqueOperands) {
			int n = totalNumberOfOperand.get(operand);
			if(n == 0) {
				toRemove.add(operand);
			} else {
				totalNumberOfOperands += n;
			}
		}
		uniqueOperands.removeAll(toRemove);
	}
	
	public int getVocabularySize() {
		return uniqueOperators.size() + uniqueOperands.size();
	}
	
	public int getProgramLength() {
		return totalNumberOfOperators + totalNumberOfOperands;
	}
	
	public double getDifficulty() {
		return (double)uniqueOperators.size() / 2 * totalNumberOfOperands / uniqueOperands.size();
	}
	
	public double getVolume() {
		return getProgramLength() * Math.log(getVocabularySize()) / Math.log(2);
	}
	
	public double getEffort() {
		return getDifficulty() * getVolume();
	}
	
	public double getEstimatedEffortInSeconds() {
		return getEffort() / 18;
	}
	
	public double getModernEstimatedBugs() {
		return getVolume() / 3000;
	}
	
	public String getLongTextRepresentation() {
		String formatString = "%26s: %15.4f\n";
		return
				String.format(formatString, "Unique Operators", (double)uniqueOperators.size()) + 
				String.format(formatString, "Unique Operands", (double)uniqueOperands.size()) + 
				String.format(formatString, "Total Number of Operators", (double)totalNumberOfOperators) + 
				String.format(formatString, "Total Number of Operands", (double)totalNumberOfOperands) + 
				String.format(formatString, "Program Vocabulary", (double)getVocabularySize()) + 
				String.format(formatString, "Program Length", (double)getProgramLength()) + 
				String.format(formatString, "Volume", getVolume()) + 
				String.format(formatString, "Difficulty", getDifficulty()) + 
				String.format(formatString,"Effort(sec)", getEstimatedEffortInSeconds()) + 
				String.format(formatString,"Effort(min)", getEstimatedEffortInSeconds() / 60) + 
				String.format(formatString,"Effort(hrs)", getEstimatedEffortInSeconds() / 60 / 60) + 
				String.format(formatString, "Estimated Bugs", getModernEstimatedBugs()) + 
				"";
	}
	
	public String getCsvString() {
		return String.format("%d;%d;%d;%d;%d;%d;%.4f;%.4f;%.4f;%.4f;%.4f;%.4f",
				uniqueOperators.size(),
				uniqueOperands.size(),
				totalNumberOfOperators,
				totalNumberOfOperands,
				getVocabularySize(),
				getProgramLength(),
				getVolume(),
				getDifficulty(),
				getEstimatedEffortInSeconds(),
				getEstimatedEffortInSeconds() / 60,
				getEstimatedEffortInSeconds() / 60 / 60,
				getModernEstimatedBugs()
				); 
	}
	
	public static String getCsvHeader() {
		return String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s",
				"Distinct Operators",
				"Distinct Operands",
				"Total Number of Operators",
				"Total Number of Operands",
				"Vocabulary Size",
				"Program Length",
				"Volume",
				"Difficulty",
				"Estimated Effort (s)",
				"Estimated Effort (m)",
				"Estimated Effort (h)",
				"Estimated Bugs"
				);
	}

	public void add(HalsteadMetrics halstead) {
		if(halstead == null) {
			return;
		}
		
		if(halstead.uniqueOperands != null)
		for(String o : halstead.uniqueOperands) {
			this.addOperand(o, halstead.getTotalNumberOfOperand(o));
		}
		
		if(halstead.uniqueOperators != null)
		for(String o : halstead.uniqueOperators) {
			this.addOperator(o, halstead.getTotalNumberOfOperator(o));
		}
		
		finalize();
	}
}
