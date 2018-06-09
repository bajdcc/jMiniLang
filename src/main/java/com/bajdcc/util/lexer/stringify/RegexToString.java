package com.bajdcc.util.lexer.stringify;

import com.bajdcc.util.lexer.regex.Charset;
import com.bajdcc.util.lexer.regex.Constructure;
import com.bajdcc.util.lexer.regex.IRegexComponentVisitor;
import com.bajdcc.util.lexer.regex.Repetition;

public class RegexToString implements IRegexComponentVisitor {

	/**
	 * 正则表达式的树型表达
	 */
	private StringBuilder context = new StringBuilder();

	/**
	 * 前缀
	 */
	private StringBuilder prefix = new StringBuilder();

	/**
	 * 前缀缩进
	 */
	private void appendPrefix() {
		prefix.append('\t');
		context.append(" {").append(System.lineSeparator());
	}

	/**
	 * 取消前缀缩进
	 */
	private void reducePrefix() {
		prefix.deleteCharAt(0);
		context.append(prefix).append("}").append(System.lineSeparator());
	}

	@Override
	public void visitBegin(Charset node) {
		context.append(prefix).append("字符");
		context.append(node.bReverse ? "[取反]" : "").append("\t").append(node);
		context.append(System.lineSeparator());
	}

	@Override
	public void visitBegin(Constructure node) {
		context.append(prefix).append(node.bBranch ? "分支" : "序列");
		appendPrefix();
	}

	@Override
	public void visitBegin(Repetition node) {
		context.append(prefix.toString()).append("循环{").append(node.iLowerBound).append(",").append(node.iUpperBound).append("}");
		appendPrefix();
	}

	@Override
	public void visitEnd(Charset node) {

	}

	@Override
	public void visitEnd(Constructure node) {
		reducePrefix();
	}

	@Override
	public void visitEnd(Repetition node) {
		reducePrefix();
	}

	@Override
	public String toString() {
		return context.toString();
	}
}
