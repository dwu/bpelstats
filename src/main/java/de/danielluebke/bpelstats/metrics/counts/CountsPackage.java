package de.danielluebke.bpelstats.metrics.counts;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.danielluebke.bpelstats.metrics.StatisticsPackage;

public class CountsPackage extends StatisticsPackage {

	@Override
	protected String[] getFieldTitles() {
		return new String[] {
				"#Assign",
				"#Catch",
				"#CatchAll",
				"#Compensate",
				"#CompensateScope",
				"#CompensationHandler",
				"#Copy",
				"#Else",
				"#ElseIf",
				"#Empty",
				"#Exit",
				"#Flow",
				"#ForEach",
				"#If",
				"#Invoke",
				"#Link",
				"#OnAlarm",
				"#OnAlarmHandler",
				"#OnMessage",
				"#OnMessageHandler",
				"#PartnerLink",
				"#Pick",
				"#Receive",
				"#RepeatUntil",
				"#Reply",
				"#Rethrow",
				"#Scope",
				"#Sequence",
				"#Throw",
				"#Validate",
				"#Wait",
				"#While",
				"#Variable",
				"#AllActivities",
				"#BasicActivities",
				"#StructuredActivities",
				"#NonlinearActivities",
				"#ExtensionActivities",
				"#Link",
				"#TransitionCondition",
				"#B4PPeopleActivity",
				"#AVBreak",
				"#AVContinue",
				"#AVSuspend",
				"#AVOnSignal",
				"#AVSignalReceive",
				"#AVSignalSend",
				"#WPSFlow",
				"#WPSExitCondition",
				"#WPSJavaGlobals",
				"#WPSImport",
				"#WPSScript",
				"#WPSJavaCode",
				"#WPSCustomProperty",
				"#WPSDescription",
				"#WPSDocumentation",
				"#WPSTrue",
				"#WPSFalse"
		};
	}

	@Override
	protected Object[] calculateStatistic(String bpelFilename)
			throws IOException, SAXException, ParserConfigurationException {
		SAXStatsHandler result = new SAXStatsGatherer().gather(bpelFilename);
		
		return new Object[] {
				result.getCountAssign(),
				result.getCountCatch(),
				result.getCountCatchAll(),
				result.getCountCompensate(),
				result.getCountCompensateScope(),
				result.getCountCompensationHandler(),
				result.getCountCopy(),
				result.getCountElse(),
				result.getCountElseIf(),
				result.getCountEmpty(),
				result.getCountExit(),
				result.getCountFlow(),
				result.getCountForEach(),
				result.getCountIf(),
				result.getCountInvoke(),
				result.getCountLink(),
				result.getCountOnAlarm(),
				result.getCountOnAlarmHandler(),
				result.getCountOnMessage(),
				result.getCountOnMessageHandler(),
				result.getCountPartnerLink(),
				result.getCountPick(),
				result.getCountReceive(),
				result.getCountRepeatUntil(),
				result.getCountReply(),
				result.getCountRethrow(),
				result.getCountScope(),
				result.getCountSequence(),
				result.getCountThrow(),
				result.getCountValidate(),
				result.getCountWait(),
				result.getCountWhile(),
				result.getCountVariable(),
				result.getCountAllActivities(),
				result.getCountBasicActivities(),
				result.getCountStructuredActivities(),
				result.getCountNonLinearActivities(),
				result.getCountExtensionActivities(),
				result.getCountLinks(),
				result.getCountTransitionConditions(),
				result.getCountB4PPeopleActivity(),
				result.getCountActiveVOSBreak(),
				result.getCountActiveVOSContinue(),
				result.getCountActiveVOSSuspend(),
				result.getCountActiveVOSOnSignal(),
				result.getCountActiveVOSSignalReceive(),
				result.getCountActiveVOSSignalSend(),
				result.getCountWPSFlow(),
				result.getCountWPSExitCondition(),
				result.getCountWPSJavaGlobals(),
				result.getCountWPSImport(),
				result.getCountWPSScript(),
				result.getCountWPSJavaCode(),
				result.getCountWPSCustomProperty(),
				result.getCountWPSDescription(),
				result.getCountWPSDocumentation(),
				result.getWPSTrue(),
				result.getWPSFalse()
			};
	}

}
