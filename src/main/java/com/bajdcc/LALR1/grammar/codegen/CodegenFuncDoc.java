package com.bajdcc.LALR1.grammar.codegen;

import com.bajdcc.LALR1.grammar.runtime.IRuntimeDebugExec;
import com.bajdcc.LALR1.grammar.runtime.IRuntimeStatus;
import com.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import com.bajdcc.LALR1.grammar.runtime.RuntimeObjectType;
import com.bajdcc.util.lexer.token.Token;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 【扩展】扩展方法文档
 *
 * @author bajdcc
 */
public class CodegenFuncDoc implements IRuntimeDebugExec {

	private String doc;
	private String params;

	public CodegenFuncDoc(String doc, List<Token> args) {
		this.doc = doc;
		this.params = args.stream().map(Token::toRealString).collect(Collectors.joining("，"));
		this.params = this.params.isEmpty() ? "空" : this.params;
	}

	@Override
	public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
	                                      IRuntimeStatus status) {
		return null;
	}

	@Override
	public RuntimeObjectType[] getArgsType() {
		return null;
	}

	@Override
	public String getDoc() {
		return doc;
	}

	public String getParamsDoc() {
		return params;
	}
}
