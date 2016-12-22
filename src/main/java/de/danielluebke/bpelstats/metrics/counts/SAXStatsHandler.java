package de.danielluebke.bpelstats.metrics.counts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SAXStatsHandler extends DefaultHandler {

	private static final String NAMESPACE_WPC = "http://www.ibm.com/xmlns/prod/websphere/business-process/6.0.0/";
	private static final List<String> WPC_EXTENSIONS = Arrays.asList("exitCondition", "javaGlobals", "import", "script", "javaCode", "customProperty", "value", "description", "documentation");
	
	private String bpelNamespace;
	private Map<String, Integer> bpelCounts;
	private Map<String, Integer> extensionCounts;
	private List<String> elementPath = new ArrayList<String>(); 
	
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		
		bpelCounts = new HashMap<String, Integer>();
		extensionCounts = new HashMap<String, Integer>();
		bpelNamespace = null;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		
		if(bpelNamespace == null) {
			bpelNamespace = uri;
		}
		
		if(bpelNamespace.equals(uri)) {
			String bpelElement = localName;
			
			if(elementPath.size() > 0 && elementPath.get(elementPath.size()-1).equals("eventHandlers")) {
				bpelElement += "Handler";
			}
		
			Integer count = bpelCounts.get(bpelElement);
			if(count == null) {
				count = 0;
			}
			count++;
			bpelCounts.put(bpelElement, count);
		}
		
		if(
			(elementPath.size() > 0 && elementPath.get(elementPath.size()-1).equals("extensionActivity")) ||
			(localName.equals("onSignal") && uri.equals("http://www.activebpel.org/2006/09/bpel/extension/activity")) ||
			(uri.equals(NAMESPACE_WPC) && WPC_EXTENSIONS.contains(localName))
			) {
			String normalizedName = "{" + uri + "}" + localName;
			Integer count = extensionCounts.get(normalizedName);
			if(count == null) {
				count = 0;
			}
			count++;
			extensionCounts.put(normalizedName, count);
		}
		
		elementPath.add(localName);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		
		elementPath.remove(elementPath.size()-1);
	}
	
	private int getBpelCount(String bpelActivityName) {
		Integer count = bpelCounts.get(bpelActivityName);
		return count != null ? count : 0;
	}
	
	private int getExtensionCount(String normalizedQNameExtensionActivity) {
		Integer count = extensionCounts.get(normalizedQNameExtensionActivity);
		return count != null ? count : 0;
	}
	
	public int getCountAssign() {
		return getBpelCount("assign");
	}

	public int getCountCatch() {
		return getBpelCount("catch");
	}

	public int getCountCatchAll() {
		return getBpelCount("catchAll");
	}

	public int getCountCompensate() {
		return getBpelCount("compensate");
	}

	public int getCountCompensateScope() {
		return getBpelCount("compensateScope");
	}

	public int getCountCompensationHandler() {
		return getBpelCount("compensationHandler");
	}

	public int getCountCopy() {
		return getBpelCount("copy");
	}

	public int getCountElse() {
		return getBpelCount("else") + getBpelCount("otherwise"); // BPEL 2.0 + BPEL 1.1
	}

	public int getCountElseIf() {
		// BPEL 2.0 has elseif, BPEL 1.1 only cases. If a switch has more than one case, it counts as an elseif here
		return getBpelCount("elseif") + (getBpelCount("case") - getBpelCount("switch"));
	}

	public int getCountEmpty() {
		return getBpelCount("empty");
	}

	public int getCountExit() {
		return getBpelCount("exit") + getBpelCount("terminate"); // BPEL 2.0 + BPEL 1.1
	}

	public int getCountFlow() {
		return getBpelCount("flow");
	}

	public int getCountForEach() {
		return getBpelCount("forEach");
	}

	public int getCountIf() {
		return getBpelCount("if") + getBpelCount("switch"); // BPEL 2.0 + BPEL 1.1
	}

	public int getCountInvoke() {
		return getBpelCount("invoke");
	}

	public int getCountLink() {
		return getBpelCount("link");
	}

	public int getCountOnAlarm() {
		return getBpelCount("onAlarm");
	}

	public int getCountOnAlarmHandler() {
		return getBpelCount("onAlarmHandler");
	}

	public int getCountOnMessage() {
		return getBpelCount("onMessage");
	}

	public int getCountOnMessageHandler() {
		return getBpelCount("onEventHandler");
	}

	public int getCountPartnerLink() {
		return getBpelCount("partnerLink");
	}

	public int getCountPick() {
		return getBpelCount("pick");
	}

	public int getCountReceive() {
		return getBpelCount("receive");
	}

	public int getCountRepeatUntil() {
		return getBpelCount("repeatUntil");
	}

	public int getCountReply() {
		return getBpelCount("reply");
	}

	public int getCountRethrow() {
		return getBpelCount("rethrow");
	}

	public int getCountScope() {
		return getBpelCount("scope");
	}

	public int getCountSequence() {
		return getBpelCount("sequence");
	}

	public int getCountThrow() {
		return getBpelCount("throw");
	}

	public int getCountValidate() {
		return getBpelCount("validate");
	}

	public int getCountVariable() {
		return getBpelCount("variable");
	}

	public int getCountWait() {
		return getBpelCount("wait");
	}

	public int getCountWhile() {
		return getBpelCount("while");
	}
	

	public int getCountExtensionActivities() {
		return getBpelCount("extensionActivity");
	}

	public int getCountActiveVOSSuspend() {
		return getExtensionCount("{http://www.activebpel.org/2006/09/bpel/extension/activity}suspend");
	}
	
	public int getCountActiveVOSContinue() {
		return getExtensionCount("{http://www.activebpel.org/2006/09/bpel/extension/activity}continue");
	}
	
	public int getCountActiveVOSBreak() {
		return getExtensionCount("{http://www.activebpel.org/2006/09/bpel/extension/activity}break");
	}
	
	public int getCountActiveVOSSignalReceive() {
		return getExtensionCount("{http://www.activebpel.org/2006/09/bpel/extension/activity}signalReceive");
	}
	
	public int getCountActiveVOSSignalSend() {
		return getExtensionCount("{http://www.activebpel.org/2006/09/bpel/extension/activity}signalSend");
	}
	
	public int getCountActiveVOSOnSignal() {
		return getExtensionCount("{http://www.activebpel.org/2006/09/bpel/extension/activity}onSignal");
	}
	
	public int getCountB4PPeopleActivity() {
		return getExtensionCount("{http://www.example.org/BPEL4People}peopleActivity");
	}
	
	public int getCountWPSFlow() {
		return getExtensionCount("{http://www.ibm.com/xmlns/prod/websphere/business-process/6.0.0/}flow");
	}
	
	public int getCountWPSExitCondition() {
		return getExtensionCount("{http://www.ibm.com/xmlns/prod/websphere/business-process/6.0.0/}exitCondition");
	}
	
	public int getCountWPSJavaGlobals() {
		return getExtensionCount("{http://www.ibm.com/xmlns/prod/websphere/business-process/6.0.0/}javaGlobals");
	}
	
	public int getCountWPSImport() {
		return getExtensionCount("{http://www.ibm.com/xmlns/prod/websphere/business-process/6.0.0/}import");
	}
	
	public int getCountWPSScript() {
		return getExtensionCount("{http://www.ibm.com/xmlns/prod/websphere/business-process/6.0.0/}script");
	}
	
	public int getCountWPSJavaCode() {
		return getExtensionCount("{http://www.ibm.com/xmlns/prod/websphere/business-process/6.0.0/}javaCode");
	}
	
	public int getCountWPSCustomProperty() {
		return getExtensionCount("{http://www.ibm.com/xmlns/prod/websphere/business-process/6.0.0/}customProperty");
	}
	
	public int getCountWPSDescription() {
		return getExtensionCount("{http://www.ibm.com/xmlns/prod/websphere/business-process/6.0.0/}description");
	}
	
	public int getCountWPSDocumentation() {
		return getExtensionCount("{http://www.ibm.com/xmlns/prod/websphere/business-process/6.0.0/}documentation");
	}
	
	public int getCountAllActivities() {
		return getCountAssign() + getCountCatch() + getCountCatchAll() + getCountCompensate()
				+ getCountCompensateScope() + getCountCompensationHandler() + getCountCopy()
				+ getCountElse() + getCountElseIf() + getCountEmpty() + getCountExit() + getCountFlow()
				+ getCountForEach() + getCountIf() + getCountInvoke() + getCountOnAlarm()
				+ getCountOnMessage() + getCountOnMessageHandler() + getCountPick()
				+ getCountReceive() + getCountRepeatUntil() + getCountReply() + getCountRethrow()
				+ getCountScope() + getCountSequence() + getCountThrow() + getCountValidate()
				+ getCountExtensionActivities();
	}

	public int getCountBasicActivities() {
		return getCountAssign() + getCountCompensate() + getCountCompensateScope()
				+ getCountEmpty() + getCountExit() + getCountInvoke() + getCountReceive()
				+ getCountReply() + getCountRethrow() + getCountThrow() + getCountValidate();
	}

	public int getCountStructuredActivities() {
		return getCountFlow() + getCountForEach() + getCountIf() + getCountPick() + getCountRepeatUntil() + getCountScope() + getCountSequence() + getCountWhile();
	}

	public int getCountNonLinearActivities() {
		return getCountCatch() + getCountCatchAll() + getCountCompensate()
				+ getCountCompensateScope() + getCountCompensationHandler ()
				+ getCountElse() + getCountElseIf() + getCountExit() + getCountFlow()
				+ getCountForEach() + getCountIf() + getCountOnAlarm()
				+ getCountOnMessage() + getCountOnMessageHandler() + getCountPick()
				+ getCountRepeatUntil() + getCountRethrow() + getCountThrow();
	}

	public Map<String, Integer> getUsedExtensionActivities() {
		return new HashMap<String, Integer>(extensionCounts);
	}

}
