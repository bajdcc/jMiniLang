package priv.bajdcc.lexer.regex;

/**
 * 基于正则表达式组件的访问接口（Visitor模式）
 * 
 * @author bajdcc
 */
public interface IRegexComponentVisitor {

	public void visitBegin(Charset node);

	public void visitBegin(Constructure node);

	public void visitBegin(Repetition node);

	public void visitEnd(Charset node);

	public void visitEnd(Constructure node);

	public void visitEnd(Repetition node);
}
