package de.danielluebke.bpelstats;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.IOUtils;

import de.danielluebke.bpelstats.metrics.StatisticsPackage;
import de.danielluebke.bpelstats.metrics.assign.AssignStatsPackage;
import de.danielluebke.bpelstats.metrics.complexity.ComplexityPackage;
import de.danielluebke.bpelstats.metrics.counts.CountsPackage;
import de.danielluebke.bpelstats.metrics.extensions.ExtensionsPackage;
import de.danielluebke.bpelstats.metrics.sublanguagestats.SublanguageStatsPackage;

public final class BpelStatsMain {

	private static final String DEFAULT_SEPARATOR = ",";
	
	private boolean isPrintHeader;
	private boolean isDoAnonymization;
	private boolean isDoPseudoAnonymization;
	private boolean isModeSublanguageStatistics;
	private boolean isModeComplexityMetrics;
	private boolean isModeExtensions;
	private Writer out;
	private String delimiter;
	private List<String> bpelFileList;
	private String reuseDirectories;
	private boolean isModePrintHelp;

	private boolean isModeAssign;

	private BpelStatsMain() {
	}

	public static void main(String[] args) throws Exception {
		int returnCode = new BpelStatsMain().run(args);
		System.exit(returnCode);
	}

	public int run(String[] args) throws ParserConfigurationException, IOException {
		Options options = createOptions();
		try {
			parseParameters(args, options);
			
			if(isModePrintHelp) {
				showHelp(options);
				return -1;
			}
			
			// Validate Parameters
			int i = 0;
			if(isModeExtensions) i++;
			if(isModeComplexityMetrics) i++;
			if(isModeSublanguageStatistics) i++;
			if(i > 1) {
				showHelp(options);
				return 1;
			}
			
			StatisticsPackage mainStrategy = null;
			if(isModeExtensions) {
				mainStrategy = new ExtensionsPackage();
			} else if(isModeComplexityMetrics) {
				mainStrategy = new ComplexityPackage();
			} else if(isModeSublanguageStatistics) {
				mainStrategy = new SublanguageStatsPackage();
				((SublanguageStatsPackage)mainStrategy).setReuseDir(reuseDirectories);
			} else if(isModeAssign) {
				mainStrategy = new AssignStatsPackage();
			} else {
				mainStrategy = new CountsPackage();
			}
			mainStrategy.setBPELFiles(bpelFileList.toArray(new String[bpelFileList.size()]));
			mainStrategy.setDelimiter(delimiter);
			mainStrategy.setDoAnonymization(isDoAnonymization);
			mainStrategy.setDoPseudoAnonymization(isDoPseudoAnonymization);
			mainStrategy.setPrintHeader(isPrintHeader);
			mainStrategy.setWriter(out);
			
			mainStrategy.runStatistics();
			
		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			showHelp(options);
			return 2;
		} catch (FileNotFoundException e) {
			System.err.println("Problem while writing file: " + e.getMessage());
			return 3;
		} finally {
			IOUtils.closeQuietly(out);
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	private void parseParameters(String[] args, Options options) throws ParseException, IOException {
		CommandLineParser parser = new PosixParser();
		CommandLine cmd = parser.parse(options, args);
		
		bpelFileList = cmd.getArgList();
		isPrintHeader = cmd.hasOption('h');
		isDoAnonymization = cmd.hasOption('a');
		isDoPseudoAnonymization = cmd.hasOption('p');
		reuseDirectories = cmd.getOptionValue("reusedir");
		
		isModeSublanguageStatistics = cmd.hasOption('s');
		isModeComplexityMetrics = cmd.hasOption('c');
		isModeExtensions = cmd.hasOption('e');
		isModePrintHelp = cmd.hasOption("help");
		isModeAssign = cmd.hasOption("assign");

		if (cmd.hasOption('f')) {
			out = new FileWriter(cmd.getOptionValue('f'));
		} else {
			out = new OutputStreamWriter(System.out);		
		}

		if (cmd.hasOption('d')) {
			delimiter = cmd.getOptionValue('d');
		} else {
			delimiter = DEFAULT_SEPARATOR;
		}
	}

	private void showHelp(final Options options) {
		final HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("bpelstats file.bpel... [options]", options);
	}

	@SuppressWarnings("static-access")
	private Options createOptions() {
		final Options options = new Options();

		options.addOption(OptionBuilder
				.withDescription("Writes statistics into a file.").hasArg()
				.withArgName("FILE").create("f"));
		options.addOption(OptionBuilder.withDescription(
				"Writes out the table header").create("h"));

		options.addOption(OptionBuilder.withDescription(
				"Gathers sub-language statistics (excludes -c and -e)").create("s"));

		options.addOption(OptionBuilder.withDescription(
				"Gather BPEL Complexity Metrics as used in Juric & Hertis 2014 (excludes -e and -s)").create("c"));

		options.addOption(OptionBuilder.withDescription(
				"Gather BPEL Extensions and ExtensionActivities (excludes -c and -s)").create("e"));

		options.addOption(OptionBuilder.withDescription(
				"Anonymize BPEL file names (overrides -p)").create("a"));
		
		options.addOption(OptionBuilder.withDescription(
				"Uses pseudo anonymization printing both anonymized and non-anonymized file names (overridden by -a)").create("p"));

		options.addOption(OptionBuilder
				.withDescription(
						"Assign Statistics")
				.withLongOpt("assign")
				.create());
		
		options.addOption(OptionBuilder
				.withDescription(
						"Directories for re-usable assets (XSLT/XQuery). Only applicable for -s option")
				.withArgName("directory").withLongOpt("reusedir").hasArg(true)
				.create());
		options.addOption(OptionBuilder
				.withDescription("Prints this help")
				.withLongOpt("help").hasArg(false)
				.create());

		options.addOption(OptionBuilder
				.withDescription("Use a custom delimiter (default=" + DEFAULT_SEPARATOR + ")")
				.withArgName("delimiter").hasArg().create("d"));

		return options;
	}

}
