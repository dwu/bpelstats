package de.danielluebke.bpelstats.metrics.sublanguagestats;

import java.io.File;

import de.danielluebke.bpelstats.metrics.StatisticsPackage;

public class JavaSublanguageStatsPackage extends StatisticsPackage {

	@Override
	protected String[] getFieldTitles() {
		return new String[] {
				"Java/LOCs",
				"Java/SLOCs",
				"Java/Complexity",
				"Java/Complexity(#Conditions)",
				"Java/Complexity(#Iterations)",
				"Java/Halstead/Operands",
				"Java/Halstead/Operators",
				"Java/Halstead/ProgramLength",
				"Java/Halstead/VocabularySize",
				"Java/Halstead/Volume",
				"Java/Halstead/Difficulty",
				"Java/Halstead/Effort",
				"Java/Halstead/Effort(s)",
				"Java/Halstead/EstimatedBugs(Modern)"
		};
	}

	@Override
	protected Object[] calculateStatistic(String javaFilename) throws Exception {
		JavaSubLanguageStatsGatherer gatherer = new JavaSubLanguageStatsGatherer();

		File javaFile = new File(javaFilename);

		JavaFileStats sublanguageResults = gatherer.gather(javaFile);

		return new Object[] {
			sublanguageResults.javaLOCs,
			sublanguageResults.javaSLOCs,
			sublanguageResults.javaComplexity,
			sublanguageResults.javaNumConditions,
			sublanguageResults.javaNumIterations,
			sublanguageResults.javaHalstead.getUniqueOperands().size(),
			sublanguageResults.javaHalstead.getUniqueOperators().size(),
			sublanguageResults.javaHalstead.getProgramLength(),
			sublanguageResults.javaHalstead.getVocabularySize(),
			sublanguageResults.javaHalstead.getVolume(),
			sublanguageResults.javaHalstead.getDifficulty(),
			sublanguageResults.javaHalstead.getEffort(),
			sublanguageResults.javaHalstead.getEstimatedEffortInSeconds(),
			sublanguageResults.javaHalstead.getModernEstimatedBugs()
		};
	}

}
