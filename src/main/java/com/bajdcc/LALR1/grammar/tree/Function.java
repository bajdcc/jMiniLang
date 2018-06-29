package com.bajdcc.LALR1.grammar.tree;

import com.bajdcc.LALR1.grammar.codegen.ICodegen;
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst;
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope;
import com.bajdcc.util.lexer.token.KeywordType;
import com.bajdcc.util.lexer.token.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * 【语义分析】函数
 *
 * @author bajdcc
 */
public class Function implements IExp {

	/**
	 * 名称（Lambda记号）
	 */
	private Token name = null;

	/**
	 * 真实名称（变量名）
	 */
	private String realName = null;

	/**
	 * 参数
	 */
	private List<Token> params = new ArrayList<>();

	/**
	 * 函数主体Block
	 */
	private Block block = null;

	/**
	 * 文档
	 */
	private List<Token> doc = null;

	/**
	 * 外部化
	 */
	private boolean extern = false;

	/**
	 * 是否支持YIELD
	 */
	private boolean yield = false;

	public Token getName() {
		return name;
	}

	public String getRefName() {
		return name.toRealString() + "$" + realName;
	}

	public void setName(Token name) {
		this.name = name;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public List<Token> getParams() {
		return params;
	}

	public void setParams(List<Token> params) {
		this.params = params;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public String getDoc() {
		if (doc == null || doc.isEmpty()) {
			return "过程无文档";
		}
		StringBuilder sb = new StringBuilder();
		for (Token token : doc) {
			sb.append(token.object.toString());
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	public void setDoc(List<Token> doc) {
		this.doc = doc;
	}

	public boolean isExtern() {
		return extern;
	}

	public void setExtern(boolean extern) {
		this.extern = extern;
	}

	public boolean isYield() {
		return yield;
	}

	public void setYield(boolean yield) {
		this.yield = yield;
	}

	@Override
	public boolean isConstant() {
		return false;
	}

	@Override
	public boolean isEnumerable() {
		return yield;
	}

	@Override
	public IExp simplify(ISemanticRecorder recorder) {
		return this;
	}

	@Override
	public void analysis(ISemanticRecorder recorder) {
		block.analysis(recorder);
	}

	@Override
	public void genCode(ICodegen codegen) {
		codegen.genFuncEntry(getRefName());
		codegen.genCode(RuntimeInst.inop);
		if (name.toRealString().equals(realName)) {
			codegen.genCode(RuntimeInst.ipush, codegen.genDataRef(realName));
			codegen.genCode(RuntimeInst.irefun);
		}
		int start = codegen.getCodeIndex();
		int i = 0;
		for (Token token : params) {
			codegen.genCode(RuntimeInst.iloada, i);
			codegen.genCode(RuntimeInst.ipush, codegen.genDataRef(token.object));
			codegen.genCode(RuntimeInst.ialloc);
			codegen.genCode(RuntimeInst.ipop);
			i++;
		}
		block.genCode(codegen);
		int end = codegen.getCodeIndex() - 1;
		if (start <= end) {
			codegen.genDebugInfo(start, end, this.toString());
		}
		codegen.genCode(RuntimeInst.inop);
	}

	@Override
	public String toString() {
		return print(new StringBuilder());
	}

	@Override
	public String print(StringBuilder prefix) {
		StringBuilder sb = new StringBuilder();
		if (yield) {
			sb.append(KeywordType.YIELD.getName());
			sb.append(" ");
		}
		sb.append(KeywordType.FUNCTION.getName());
		sb.append(" ");
		sb.append(realName);
		sb.append("(");
		for (Token param : params) {
			sb.append(param.toRealString());
			sb.append(", ");
		}
		if (!params.isEmpty()) {
			sb.deleteCharAt(sb.length() - 1);
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(") ");
		if (block != null) {
			sb.append(block.print(prefix));
		}
		return sb.toString();
	}

	@Override
	public void addClosure(IClosureScope scope) {
		for (Token param : params) {
			scope.addDecl(param.object);
		}
		block.addClosure(scope);
	}

	@Override
	public void setYield() {

	}
}
