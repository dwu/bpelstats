package de.danielluebke.bpelstats.metrics;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Template method class for controlling a metric execution
 * 
 *  Metrics shall subclass this class and override {@link #getFieldTitles()} and {@link #calculateStatistic(String)}
 * 
 * @author dluebke
 */
public abstract class StatisticsPackage {

	protected String[] bpelFilenames;
	private boolean isPrintHeader;
	protected String delimiter;
	private boolean doAnonymization;
	private boolean doPseudoAnonymization;
	protected boolean isProcessIndirectImports;
	protected Writer writer;

	public void setPrintHeader(boolean printHeader) {
		this.isPrintHeader = printHeader;
	}
	
	public void setBPELFiles(String[] bpelFiles) {
		this.bpelFilenames = bpelFiles;
	}
	
	public void setDelimiter(String d) {
		this.delimiter = d;
	}
	
	public void setWriter(Writer out) {
		this.writer = out;
	}
	
	public void setDoAnonymization(boolean doAnonymization) {
		this.doAnonymization = doAnonymization;
	}
	
	public void setDoPseudoAnonymization(boolean doPseudoAnonymization) {
		this.doPseudoAnonymization = doPseudoAnonymization;
	}
	
	public void setImportAll(boolean isImportAll) {
		this.isProcessIndirectImports = isImportAll;
	}

	protected String getSecondaryDelimiter() {
		if(!delimiter.equals(",")) {
			return ",";
		} else {
			return ";";
		}
	}
	
	public void runStatistics() throws IOException {
		printHeader();
		
		printStatistics();
	}

	protected void printStatistics() throws IOException {
		for(String bpelFilename : bpelFilenames) {
			writer.write(anonymizeFilenameIfNecessary(bpelFilename));
			
			try {
				Object[] metricValues = calculateStatistic(bpelFilename);
				for(Object metricValue : metricValues) {
					writer.write(delimiter);
					writer.write(metricValue.toString());
				}
			} catch(Exception e) {
				writer.write("Error calculating metrics: " + e.getMessage().replaceAll("\r\n", " ").replaceAll("\n", " ").replaceAll("\r", " ").replaceAll("\t", " "));
				e.printStackTrace();
			}
			writer.write("\n");
		}
	}

	protected void printHeader() throws IOException {
		if(isPrintHeader) {
			String[] headers = getFieldTitles();
			writer.write("Filename");
			for(String header : headers) {
				writer.write(delimiter);
				writer.write(header);
			}
			writer.write("\n");
		}
	}

	protected String anonymizeFilenameIfNecessary(String bpelFilename) {
		if(doAnonymization) {
			return getHashedFilename(bpelFilename);
		}
		
		if(doPseudoAnonymization) {
			return bpelFilename + "_" + getHashedFilename(bpelFilename);
		}
		
		return bpelFilename;
	}

	private String getHashedFilename(String fileName) {
		try {
		File bpelFile = new File(fileName);
			return getSHA512Hash(bpelFile.getName());
		} catch (Exception e) {
			// if no SHA512 is available, which should not be the case, then we just use Java hashing...
			return "" + fileName.hashCode();
		}
	}

	private String getSHA512Hash(String valueToHash)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String hash = null;
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		byte[] bytes = md.digest(valueToHash.getBytes("UTF-8"));
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		hash = sb.toString();
		return hash;
	}
	
	protected abstract String[] getFieldTitles();

	protected abstract Object[] calculateStatistic(String bpelFilename) throws Exception;
}
