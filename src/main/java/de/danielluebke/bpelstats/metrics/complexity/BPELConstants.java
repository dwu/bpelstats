package de.danielluebke.bpelstats.metrics.complexity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

public class BPELConstants {

	public final String NAMESPACE_WPC = "http://www.ibm.com/xmlns/prod/websphere/business-process/6.0.0/";
	
	public final String bpelNamespace; // = "http://docs.oasis-open.org/wsbpel/2.0/process/executable";
	public final List<QName> basicActivities;
	public final List<QName> structuredActivities;
	public final List<QName> handlers;
	public final QName link;
	public final QName sequence;
	public final List<QName> _if;
	public final QName _while;
	public final List<QName> flow;
	public final QName pick;
	public final QName process;
	
	public BPELConstants(String bpelNamespace) {
		this.bpelNamespace = bpelNamespace; 
		
		link = new QName(bpelNamespace, "link");
		sequence = new QName(bpelNamespace, "sequence");
		_while = new QName(bpelNamespace, "while");
		pick = new QName(bpelNamespace, "pick");
		process = new QName(bpelNamespace, "process");
		
		List<QName> __if = new ArrayList<>();
		__if.add(new QName(bpelNamespace, "if"));
		__if.add(new QName(bpelNamespace, "switch"));
		_if = Collections.unmodifiableList(__if);
		
		List<QName> _flow = new ArrayList<>();
		_flow.add(new QName(bpelNamespace, "flow"));
		_flow.add(new QName(NAMESPACE_WPC, "flow"));
		flow = Collections.unmodifiableList(_flow);
		
		List<QName> _basicActivities = new ArrayList<QName>();
		_basicActivities.add(new QName(bpelNamespace, "assign"));
		_basicActivities.add(new QName(bpelNamespace, "invoke"));
		_basicActivities.add(new QName(bpelNamespace, "receive"));
		_basicActivities.add(new QName(bpelNamespace, "reply"));
		_basicActivities.add(new QName(bpelNamespace, "throw"));
		_basicActivities.add(new QName(bpelNamespace, "rethrow"));
		_basicActivities.add(new QName(bpelNamespace, "exit"));
		_basicActivities.add(new QName(bpelNamespace, "empty"));
		_basicActivities.add(new QName(bpelNamespace, "extensionActivity"));
		_basicActivities.add(new QName(bpelNamespace, "validate"));
		_basicActivities.add(new QName(bpelNamespace, "wait"));
		_basicActivities.add(new QName(bpelNamespace, "compensate"));
		_basicActivities.add(new QName(bpelNamespace, "compensateScope"));
		basicActivities = Collections.unmodifiableList(_basicActivities);
		
		List<QName> _structuredActivities = new ArrayList<QName>();
		_structuredActivities.add(new QName(bpelNamespace, "sequence"));
		_structuredActivities.addAll(_if);
		_structuredActivities.add(new QName(bpelNamespace, "switch"));
		_structuredActivities.add(new QName(bpelNamespace, "while"));
		_structuredActivities.add(new QName(bpelNamespace, "forEach"));
		_structuredActivities.addAll(flow);
		_structuredActivities.add(new QName(bpelNamespace, "repeatUntil"));
		_structuredActivities.add(new QName(bpelNamespace, "pick"));
		structuredActivities = Collections.unmodifiableList(_structuredActivities);
		
		List<QName> _handlers = new ArrayList<QName>();
		_handlers.add(new QName(bpelNamespace, "compensationHandler"));
		_handlers.add(new QName(bpelNamespace, "faultHandlers"));
		_handlers.add(new QName(bpelNamespace, "terminationHandler"));
		_handlers.add(new QName(bpelNamespace, "eventHandlers"));
		handlers = Collections.unmodifiableList(_handlers);
	}
}
