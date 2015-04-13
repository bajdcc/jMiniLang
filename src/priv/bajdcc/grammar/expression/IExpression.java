package priv.bajdcc.grammar.expression;

import priv.bajdcc.grammar.codegen.ICodegen;
import priv.bajdcc.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.lexer.token.Token;

/**
 * 【语义类型】基本表达式接口
 *
 * @author bajdcc
 */
public interface IExpression {

	/**
	 * 获取单词
	 */
	public Token getToken();

	/**
	 * 是否是标识符
	 */
	public boolean isIdentifier();

	/**
	 * 是否是左值
	 */
	public boolean isLeftValue();

	/**
	 * 语义分析
	 * 
	 * @param recorder
	 *            错误记录器
	 */
	public void analysis(ISemanticRecorder recorder);

	/**
	 * 生成目标代码（值）
	 * 
	 * @param codegen
	 *            代码生成接口
	 */
	public void genValueCode(ICodegen codegen);

	/**
	 * 生成目标代码（引用）
	 * 
	 * @param codegen
	 *            代码生成接口
	 */
	public void genRefCode(ICodegen codegen);
}
