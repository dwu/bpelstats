package de.danielluebke.bpelstats.metrics.complexity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.namespace.QName;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Gatherer for calulating the Control Flow Complexity (CFC)
 * 
 * @author dluebke
 * 
 */
public class CFCGatherer extends BPELComplexityMetricGatherer {

	private static final class GraphNode {
		public GraphNode(String name, String uri) {
			this.name = name;
			this.uri = uri;
		}

		private String name;
		private String uri;
		List<GraphNode> children = new ArrayList<GraphNode>();

		public boolean add(GraphNode e) {
			return children.add(e);
		}

		public QName getQName() {
			return new QName(uri, name);
		}

		public boolean isNode(String name, String uri) {
			return name.equals(this.name) && uri.equals(this.uri);
		}

		public boolean isNode(QName n) {
			return isNode(n.getLocalPart(), n.getNamespaceURI());
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private GraphNode process;
	private GraphNode currentNode;
	private Stack<GraphNode> stack;
	private int isInHandlerDepth;
	private BPELConstants bpelConstants;

	@Override
	public void startDocument() throws SAXException {
		process = null;
		currentNode = null;
		stack = new Stack<GraphNode>();
		isInHandlerDepth = 0;
		bpelConstants = null;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		if(bpelConstants == null) {
			bpelConstants = new BPELConstants(uri);
		}
		
		if (isInHandlerDepth > 0) {
			isInHandlerDepth++;
			return;
		}

		if (bpelConstants.bpelNamespace.equals(uri)) {
			QName element = new QName(uri, localName);
			if ("process".equals(localName)) {
				process = new GraphNode(localName, uri);
				currentNode = process;
				stack.push(process);
			} else if (bpelConstants.basicActivities.contains(element)
					|| bpelConstants.structuredActivities.contains(element) || bpelConstants.link.equals(element)) {
				createNewNode(uri, localName);
			} else if (bpelConstants.handlers.contains(element)) {
				isInHandlerDepth = Math.max(isInHandlerDepth, 1);
			}
		}
	}

	private void createNewNode(String uri, String localName) {
		stack.push(currentNode);
		GraphNode newNode = new GraphNode(localName, uri);
		currentNode.add(newNode);
		currentNode = newNode;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (isInHandlerDepth > 0) {
			isInHandlerDepth--;
		}

		if (isInHandlerDepth == 0 && currentNode != null && currentNode.isNode(localName, uri)) {
			currentNode = stack.pop();
		}
	}

	@Override
	public String getName() {
		return "CFC";
	}

	@Override
	public String getValue() {
		try {
			return "" + getValue(process);
		} catch (Exception e) {
			return "-";
		}
	}

	private double getValue(GraphNode g) {
		if (bpelConstants.basicActivities.contains(g.getQName())) {
			return 1.0;
		}

		if (bpelConstants.sequence.equals(g.getQName()) || bpelConstants.process.equals(g.getQName())) {
			return getCFCOfChildren(g);
		}

		if (bpelConstants._if.contains(g.getQName())) {
			return g.children.size() * getCFCOfChildren(g);
		}

		if (bpelConstants._while.equals(g.getQName())) {
			double innerCFC = getCFCOfChildren(g);
			return (Math.log(innerCFC + 2.0) / Math.log(2)) * innerCFC;
		}

		if (bpelConstants.flow.contains(g.getQName())) {
			int linkCount = countLinks(g);
			// 2 * linkCount because links are children in this model
			double fac = fac(g.children.size() - 2 * linkCount);
			return fac * getCFCOfChildren(g);
		}

		if (bpelConstants.pick.equals(g.getQName())) {
			double exp = exp2(g.children.size()) - 1;
			return exp * getCFCOfChildren(g);
		}

		return 0;
	}

	private static long exp2(int n) {
		return 1 << n;
	}

	private static double fac(long n) {
		if (n < 0) {
			throw new RuntimeException("Faculty of " + n + " is not valid");
		}

		double result = 1.0;
		for (int i = 2; i <= n; i++) {
			result *= i;
		}

		return result;
	}

	private int countLinks(GraphNode g) {
		int result = 0;
		for (GraphNode c : g.children) {
			if (c.isNode(bpelConstants.link)) {
				result++;
			}
		}
		return result;
	}

	private double getCFCOfChildren(GraphNode g) {
		double result = 0.0;
		for (GraphNode c : g.children) {
			result += getValue(c);
		}

		return result;
	}

}
