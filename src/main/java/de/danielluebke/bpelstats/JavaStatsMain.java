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
import de.danielluebke.bpelstats.metrics.sublanguagestats.JavaSublanguageStatsPackage;

public final class JavaStatsMain {

	private static final String DEFAULT_SEPARATOR = ",";

	private boolean isPrintHeader;
	private boolean isDoAnonymization;
	private boolean isDoPseudoAnonymization;
	private Writer out;
	private String delimiter;
	private List<String> javaFileList;
	private boolean isModePrintHelp;

	private JavaStatsMain() {
	}

	public static void main(String[] args) throws Exception {
		int returnCode = new JavaStatsMain().run(args);
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

			StatisticsPackage mainStrategy = null;
			mainStrategy = new JavaSublanguageStatsPackage();
			// BPELFiles contains the list of Java files to be processed
			mainStrategy.setBPELFiles(javaFileList.toArray(new String[javaFileList.size()]));
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

		javaFileList = cmd.getArgList();
		isPrintHeader = cmd.hasOption('h');
		isDoAnonymization = cmd.hasOption('a');
		isDoPseudoAnonymization = cmd.hasOption('p');
		isModePrintHelp = cmd.hasOption("help");

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
		formatter.printHelp("javastats file.java... [options]", options);
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
				"Anonymize file names (overrides -p)").create("a"));

		options.addOption(OptionBuilder.withDescription(
				"Uses pseudo anonymization printing both anonymized and non-anonymized file names (overridden by -a)").create("p"));

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
