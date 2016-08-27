package priv.bajdcc.util.lexer.regex;

/**
 * 基于正则表达式组件的访问接口（Visitor模式）
 * 
 * @author bajdcc
 */
public interface IRegexComponentVisitor {

	void visitBegin(Charset node);

	void visitBegin(Constructure node);

	void visitBegin(Repetition node);

	void visitEnd(Charset node);

	void visitEnd(Constructure node);

	void visitEnd(Repetition node);
}
