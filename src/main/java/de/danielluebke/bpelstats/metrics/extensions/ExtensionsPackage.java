package de.danielluebke.bpelstats.metrics.extensions;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.danielluebke.bpelstats.metrics.StatisticsPackage;
import de.danielluebke.bpelstats.saxutil.SAXExecutor;

public class ExtensionsPackage extends StatisticsPackage {

	@Override
	protected String[] getFieldTitles() {
		return new String[] {
				"MustUnderstandExtensions",
				"OptionalExtensions",
				"ExtensionActivities"
		};
	}

	@Override
	protected Object[] calculateStatistic(String bpelFilename) throws IOException, SAXException, ParserConfigurationException {
		ExtensionHandler extensionHandler = new ExtensionHandler();
		SAXExecutor.execute(bpelFilename, extensionHandler);
		
		StringBuilder mustUnderstandExtensions = new StringBuilder();
		for(String e :  extensionHandler.getExtensionsMustUnderstand()) {
			if(mustUnderstandExtensions.length() != 0) {
				mustUnderstandExtensions.append(getSecondaryDelimiter());
				mustUnderstandExtensions.append(e);
			}
		}
		
		StringBuilder otherExtensions = new StringBuilder();
		for(String e :  extensionHandler.getExtensionsNotMustUnderstand()) {
			if(otherExtensions.length() != 0) {
				otherExtensions.append(getSecondaryDelimiter());
				otherExtensions.append(e);
			}
		}
		
		StringBuilder extensionActivities = new StringBuilder();
		for(String e :  extensionHandler.getExtensionActivities()) {
			if(extensionActivities.length() != 0) {
				extensionActivities.append(getSecondaryDelimiter());
				extensionActivities.append(e);
			}
		}
		
		return new Object[] {
				mustUnderstandExtensions.toString(),
				otherExtensions.toString(),
				extensionActivities.toString()
		};
	}

}
