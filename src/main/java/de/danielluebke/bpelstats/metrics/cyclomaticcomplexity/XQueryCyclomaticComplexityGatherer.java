package de.danielluebke.bpelstats.metrics.cyclomaticcomplexity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.Tree;

import es.uca.webservices.xquery.parser.XQueryParser.ForVarContext;
import es.uca.webservices.xquery.parser.XQueryParser.FunctionDeclContext;
import es.uca.webservices.xquery.parser.XQueryParser.IfExprContext;
import es.uca.webservices.xquery.parser.XQueryParser.ModuleContext;
import es.uca.webservices.xquery.parser.XQueryParser.QNameContext;
import es.uca.webservices.xquery.parser.util.XQueryParsingException;
import es.uca.webservices.xquery.parser.util.XQueryValidatingParser;

public class XQueryCyclomaticComplexityGatherer {
	
	public CyclomaticComplexityFileStats gather(File xqueryFile) throws XQueryParsingException, FileNotFoundException, IOException {
		return gather(new FileInputStream(xqueryFile), xqueryFile.getAbsolutePath());
	}

	public CyclomaticComplexityFileStats gather(InputStream inputstream, String xqueryFileName) throws XQueryParsingException, IOException {
		CyclomaticComplexityFileStats result = new CyclomaticComplexityFileStats(xqueryFileName, FileType.XQUERY);
		
		CyclomaticComplexityFunctionStats mainModule = new CyclomaticComplexityFunctionStats(xqueryFileName);
		CyclomaticComplexityFunctionStats currentFunction = mainModule;
		
		ANTLRInputStream csi = new ANTLRInputStream(inputstream);
		XQueryValidatingParser parser = new XQueryValidatingParser();
		ModuleContext moduleContext = parser.parse(csi);
		List<Tree> toParse = new ArrayList<Tree>();
		toParse.addAll(moduleContext.children);
		while (toParse.size() > 0) {
			Tree t = toParse.remove(0);

			if(t instanceof FunctionDeclContext) {
				FunctionDeclContext f = (FunctionDeclContext)t;
				String functionName = getFunctionName(f);
				
				currentFunction = new CyclomaticComplexityFunctionStats(functionName);
				result.getFunctionStats().add(currentFunction);
			}
			
			if (t instanceof ForVarContext || t instanceof IfExprContext) {
				currentFunction.incCyclomaticComplexity();
			}

			for (int i = 0; i < t.getChildCount(); i++) {
				toParse.add(t.getChild(i));
			}
		}

		if(currentFunction == mainModule) {
			result.getFunctionStats().add(mainModule);
		}
		
		return result;
	}

	private String getFunctionName(FunctionDeclContext f) {
		QNameContext functionQName = null;
		
		for(int i = 0; i < f.getChildCount(); i++) {
			if(f.getChild(i) instanceof QNameContext) {
				functionQName = (QNameContext)f.getChild(2);
				break;
			}
		}
		TerminalNode functionQNameTerminalNode = (TerminalNode) functionQName.getChild(0);
		String functionName = functionQNameTerminalNode.getSymbol().getText();
		return functionName;
	}
	
}
