package priv.bajdcc.util.lexer.stringify;

import priv.bajdcc.util.lexer.regex.Charset;
import priv.bajdcc.util.lexer.regex.Constructure;
import priv.bajdcc.util.lexer.regex.IRegexComponentVisitor;
import priv.bajdcc.util.lexer.regex.Repetition;

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
		context.append(" {" + System.lineSeparator());
	}

	/**
	 * 取消前缀缩进
	 */
	private void reducePrefix() {
		prefix.deleteCharAt(0);
		context.append(prefix + "}" + System.lineSeparator());
	}

	@Override
	public void visitBegin(Charset node) {
		context.append(prefix + "字符");
		context.append((node.bReverse ? "[取反]" : "") + "\t" + node);
		context.append(System.lineSeparator());
	}

	@Override
	public void visitBegin(Constructure node) {
		context.append(prefix + (node.bBranch ? "分支" : "序列"));
		appendPrefix();
	}

	@Override
	public void visitBegin(Repetition node) {
		context.append(prefix.toString() + "循环{" + node.iLowerBound + ","
				+ node.iUpperBound + "}");
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
