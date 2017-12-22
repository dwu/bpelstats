package de.danielluebke.bpelstats.metrics.sublanguagestats;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.Tree;

import es.uca.webservices.xquery.parser.XQueryLexer;
import es.uca.webservices.xquery.parser.XQueryParser;
import es.uca.webservices.xquery.parser.XQueryParser.AbbrevReverseStepContext;
import es.uca.webservices.xquery.parser.XQueryParser.AddContext;
import es.uca.webservices.xquery.parser.XQueryParser.AllDescPathContext;
import es.uca.webservices.xquery.parser.XQueryParser.AllNamesContext;
import es.uca.webservices.xquery.parser.XQueryParser.ComparisonContext;
import es.uca.webservices.xquery.parser.XQueryParser.FlworExprContext;
import es.uca.webservices.xquery.parser.XQueryParser.ForClauseContext;
import es.uca.webservices.xquery.parser.XQueryParser.ForVarContext;
import es.uca.webservices.xquery.parser.XQueryParser.FuncallContext;
import es.uca.webservices.xquery.parser.XQueryParser.FunctionDeclContext;
import es.uca.webservices.xquery.parser.XQueryParser.FunctionNameContext;
import es.uca.webservices.xquery.parser.XQueryParser.IfExprContext;
import es.uca.webservices.xquery.parser.XQueryParser.LetClauseContext;
import es.uca.webservices.xquery.parser.XQueryParser.LetVarContext;
import es.uca.webservices.xquery.parser.XQueryParser.ModuleContext;
import es.uca.webservices.xquery.parser.XQueryParser.MultContext;
import es.uca.webservices.xquery.parser.XQueryParser.ParamContext;
import es.uca.webservices.xquery.parser.XQueryParser.ParenContext;
import es.uca.webservices.xquery.parser.XQueryParser.PredicateListContext;
import es.uca.webservices.xquery.parser.XQueryParser.PrologContext;
import es.uca.webservices.xquery.parser.XQueryParser.RelativePathExprContext;
import es.uca.webservices.xquery.parser.XQueryParser.SequenceTypeContext;
import es.uca.webservices.xquery.parser.XQueryParser.StringLiteralContext;
import es.uca.webservices.xquery.parser.XQueryParser.TextTestContext;
import es.uca.webservices.xquery.parser.XQueryParser.TypeDeclarationContext;
import es.uca.webservices.xquery.parser.XQueryParser.VarContext;
import es.uca.webservices.xquery.parser.util.XQueryParsingException;
import es.uca.webservices.xquery.parser.validation.ExtraGrammaticalValidationListener;

public class XQueryHalsteadMetricsCalculator implements IHalsteadMetricsCalculator {
	
	class ErrorCollector extends BaseErrorListener {
		private final List<String> errors;

		public ErrorCollector() {
			this(new ArrayList<String>());
		}

		public ErrorCollector(List<String> errors) {
			this.errors = errors;
		}

		public List<String> getErrors() {
			return errors;
		}

		@Override
		public void syntaxError(Recognizer<?, ?> recognizer,
				Object offendingSymbol, int line, int charPositionInLine,
				String msg, RecognitionException e) {
			if (!msg.contains("report")) {
				errors.add("line "+ line + ":" + charPositionInLine + ": " + msg);
			}
		}
	}
	
	private HalsteadMetrics metrics = new HalsteadMetrics();
	
	public ModuleContext parse(final CodePointCharStream charStream) throws XQueryParsingException {
		final XQueryLexer lexer = new XQueryLexer(charStream);
		final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		final XQueryParser parser = new XQueryParser(tokenStream);

		final List<String> errors = new ArrayList<String>();
		final ErrorCollector errorCollector = new ErrorCollector(errors);
		lexer.addErrorListener(errorCollector);
		parser.addErrorListener(errorCollector);
		final ModuleContext tree = parser.module();

		if (tree != null && errors.isEmpty()) {
			final ExtraGrammaticalValidationListener extraValidator
				= new ExtraGrammaticalValidationListener(tokenStream, errors);
			final ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(extraValidator, tree);
		}

		if (!errorCollector.getErrors().isEmpty()) {
			throw new XQueryParsingException(errors);
		}
		return tree;
	}

	
	public void parse(String xpath) throws XQueryParsingException {
		if(xpath.startsWith("/")) {
			xpath = "$x" + xpath;
			metrics.addOperand("$x", -1);
		}
		if(xpath.endsWith("/")) {
			xpath += "*";
			metrics.addOperator("*", -1);
		}
		
		CodePointCharStream csi = CharStreams.fromString(xpath);
		ModuleContext moduleContext = parse(csi);
		List<Tree> toParse = new ArrayList<Tree>();
		toParse.addAll(moduleContext.children);
//		printAntTree(moduleContext, 0);
		while (toParse.size() > 0) {
			Tree t = toParse.remove(0);
//			System.out.println(t.getClass() + ":" + t.toString());
			if(t instanceof FunctionNameContext) {
				String functionName = ((TerminalNode)t.getChild(0)).getText();
				metrics.addOperator(functionName);
			} else if(t instanceof FuncallContext) {
				toParse.add(t.getChild(0)); // function name
				for(int i = t.getChildCount() - 1; i >= 1; i--) {
					// from function(a,b) make a b (skip () and , and functionname)
					if(! (t.getChild(i) instanceof TerminalNode)) {
						toParse.add(0, t.getChild(i));
					}
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
			} else if(t instanceof FlworExprContext) {
				metrics.addOperator("return");
				/* strip return terminal node */
				toParse.add(0, t.getChild(t.getChildCount() -1));
				for (int i = t.getChildCount() - 3; i >= 0; i--) {
					toParse.add(0, t.getChild(i));
				}
			} else if(t instanceof LetClauseContext) {
				// let <letvarcontext> [, <letvarcontext ]
				metrics.addOperator("let");
				for (int i = t.getChildCount() - 1; i >= 1; i-=2) {
					toParse.add(0, t.getChild(i));
				}
			} else if(t instanceof LetVarContext) {
				// $ <qnamecontext> := exprsinglecontext
				String varname = findTextOfTerminalNode(t.getChild(1));
				metrics.addOperand("$" + varname);
				metrics.addOperator(":=");
				toParse.add(0, t.getChild(3));
			} else if(t instanceof ForClauseContext) {
				metrics.addOperator("for");
				toParse.add(t.getChild(1));
			} else if(t instanceof ForVarContext) {
				// $ <varname> in <exprsinglecontext>
				metrics.addOperand("$" + findTextOfTerminalNode(t.getChild(1)));
				metrics.addOperator("in");
				toParse.add(0, t.getChild(3));
			} else if(t instanceof FunctionDeclContext) {
				// declare function <qnamecontext> ( paramcontext ) as <sequencetypecontext> { <ExprContext> }
				metrics.addOperator("declare function");
				metrics.addOperator(findTextOfTerminalNode(t.getChild(2)));
				for(int i = t.getChildCount() - 1; i >= 3; i--) {
					Tree child = t.getChild(i);
					if(!(child instanceof TerminalNode)) {
						toParse.add(0, child);
					}
				}
			} else if(t instanceof ParamContext) {
				// $ varname <typedeclarationcontext>
				metrics.addOperand("$" + findTextOfTerminalNode(t.getChild(1)));
				toParse.add(0, t.getChild(2));
			} else if(t instanceof TypeDeclarationContext) {
				// as <sequenceTypeContext>
				toParse.add(0, t.getChild(1));
			} else if(t instanceof SequenceTypeContext) {
				SequenceTypeContext s = (SequenceTypeContext)t;
				metrics.addOperand(findTextOfTerminalNode(t.getChild(0)));
				if(s.occurrence != null) {
					metrics.addOperator(s.occurrence.getText());
				}
			} else if(t instanceof ParenContext) {
				// ( <ExprContext>* )
				metrics.addOperator("()");
				Tree exprContext = t.getChild(1);
				for(int i = 0; i < exprContext.getChildCount(); i += 2) {
					// skip ,
					toParse.add(0, exprContext.getChild(i));
				}
			} else if(t instanceof IfExprContext) {
				// if ( exprcontext ) then exprsinglecontext else exprsinglecontext
				metrics.addOperator("if");
				toParse.add(0, t.getChild(2));
				metrics.addOperator("then");
				toParse.add(0, t.getChild(5));
				metrics.addOperator("else");
				toParse.add(0, t.getChild(7));
			} else if(t instanceof VarContext) {
				String varName = findTextOfTerminalNode(t.getChild(1));
				metrics.addOperand("$" + varName);
			} else if(t instanceof PredicateListContext && t.getChildCount() > 0 && "[".equals(findTextOfTerminalNode(t.getChild(0)))) {
				toParse.add(t.getChild(1));
				metrics.addOperator("[]");
			} else if(t instanceof AddContext || t instanceof MultContext) {
				metrics.addOperator(findTextOfTerminalNode(t.getChild(1)));
				toParse.add(0, t.getChild(2));
				toParse.add(0, t.getChild(0));
			} else if(t instanceof StringLiteralContext) {
				String stringLiteral = findTextOfTerminalNode(t.getChild(1));
				metrics.addOperand("'" + stringLiteral + "'");
			} else if(t instanceof ComparisonContext) {
				toParse.add(t.getChild(0));
				toParse.add(t.getChild(2));
				String comparisonOperator = findTextOfTerminalNode(t.getChild(1));
				metrics.addOperator(comparisonOperator);
			} else if(t instanceof PrologContext) {
				// filter out any non-countable ; 
				for(int i = t.getChildCount() - 1; i >= 0; i--) {
					Tree child = t.getChild(i);
					if(!(child instanceof TerminalNode)) {
						toParse.add(0, child);
					}
				}
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
				if(t != null) {
					for (int i = t.getChildCount() - 1; i >= 0 ; i--) {
						toParse.add(0, t.getChild(i));
					} 
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
