package de.danielluebke.bpelstats.metrics.sublanguagestats;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.Tree;

import es.uca.webservices.xquery.parser.XQueryParser.AbbrevReverseStepContext;
import es.uca.webservices.xquery.parser.XQueryParser.AllDescPathContext;
import es.uca.webservices.xquery.parser.XQueryParser.AllNamesContext;
import es.uca.webservices.xquery.parser.XQueryParser.ComparisonContext;
import es.uca.webservices.xquery.parser.XQueryParser.FuncallContext;
import es.uca.webservices.xquery.parser.XQueryParser.FunctionNameContext;
import es.uca.webservices.xquery.parser.XQueryParser.ModuleContext;
import es.uca.webservices.xquery.parser.XQueryParser.PredicateListContext;
import es.uca.webservices.xquery.parser.XQueryParser.RelativePathExprContext;
import es.uca.webservices.xquery.parser.XQueryParser.StringLiteralContext;
import es.uca.webservices.xquery.parser.XQueryParser.TextTestContext;
import es.uca.webservices.xquery.parser.XQueryParser.VarContext;
import es.uca.webservices.xquery.parser.util.XQueryParsingException;
import es.uca.webservices.xquery.parser.util.XQueryValidatingParser;

public class XPathHalsteadMetricsCalculator implements IHalsteadMetricsCalculator {
	
	private HalsteadMetrics metrics = new HalsteadMetrics();
	private XQueryValidatingParser parser = new XQueryValidatingParser();
	
	public void parse(String xpath) throws XQueryParsingException {
		if(xpath.startsWith("/")) {
			xpath = "$x" + xpath;
			metrics.addOperand("$x", -1);
		}
		if(xpath.endsWith("/")) {
			xpath += "*";
			metrics.addOperator("*", -1);
		}
		
		ANTLRInputStream csi = new ANTLRInputStream(xpath);
		ModuleContext moduleContext = parser.parse(csi);
		List<Tree> toParse = new ArrayList<Tree>();
		toParse.addAll(moduleContext.children);
//		printAntTree(moduleContext, 0);
		while (toParse.size() > 0) {
			Tree t = toParse.remove(0);
			if(t instanceof FunctionNameContext) {
				String functionName = ((TerminalNode)t.getChild(0)).getText();
				metrics.addOperator(functionName);
			} else if(t instanceof FuncallContext) {
				toParse.add(t.getChild(0)); // function name
				for(int i = 2; i < t.getChildCount() - 1; i += 2) {
					// from function(a,b) make a b (skip () and , and functionname)
					toParse.add(t.getChild(i));
				}
			} else if(t instanceof AbbrevReverseStepContext) {
				metrics.addOperator("..");
			} else if(t instanceof AllNamesContext) {
				metrics.addOperator("*");
			} else if(t instanceof AllDescPathContext) {
				metrics.addOperator("//");
				toParse.add(t.getChild(1));
			} else if(t instanceof TextTestContext) {
				metrics.addOperator("text");
			} else if(t instanceof VarContext) {
				String varName = ((TerminalNode)t.getChild(1).getChild(0).getChild(0)).getText();
				metrics.addOperand("$" + varName);
			} else if(t instanceof PredicateListContext && t.getChildCount() > 0 && "[".equals(findTextOfTerminalNode(t.getChild(0)))) {
				toParse.add(t.getChild(1));
				metrics.addOperator("[]");
			} else if(t instanceof StringLiteralContext) {
				String stringLiteral = findTextOfTerminalNode(t.getChild(1));
				metrics.addOperand("'" + stringLiteral + "'");
			} else if(t instanceof ComparisonContext) {
				toParse.add(t.getChild(0));
				toParse.add(t.getChild(2));
				String comparisonOperator = findTextOfTerminalNode(t.getChild(1));
				metrics.addOperator(comparisonOperator);
			} else if(t instanceof TerminalNode) {
				TerminalNode terminal = (TerminalNode)t;
				if(terminal.getParent() instanceof RelativePathExprContext && terminal.getText().equals("/")) {
					metrics.addOperator("/");
				} else if(terminal.getParent() instanceof RelativePathExprContext && terminal.getText().equals("//")) {
					metrics.addOperator("//");
				} else {
					metrics.addOperand(terminal.getText());
				}
			} else {
				for (int i = 0; i < t.getChildCount(); i++) {
					toParse.add(0, t.getChild(i));
				}
			}
		}
	}
	
	private String findTextOfTerminalNode(Tree t) {
		if(t instanceof TerminalNode) {
			return ((TerminalNode)t).getText();
		}
		
		for(int i = 0; i < t.getChildCount(); i++) {
			String text = findTextOfTerminalNode(t.getChild(i));
			if(text != null) {
				return text;
			}
		}
		
		return null;
	}

	@Override
	public HalsteadMetrics getHalsteadMetrics() {
		metrics.finalize();
		return metrics;
	}

	public void clear() {
		metrics.clear();
	}
	
	private static final void printAntTree(Tree t, int depth) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < depth; i++) {
			sb.append("  ");
		}
		
		sb.append(t.getClass()).append(": ").append(t.toString());
		System.out.println(sb);
		
		for(int i = 0; i < t.getChildCount(); i++) {
			printAntTree(t.getChild(i), depth+1);
		}
	}

}
