package priv.bajdcc.LALR1.grammar.tree;

import priv.bajdcc.LALR1.grammar.codegen.ICodegen;
import priv.bajdcc.LALR1.grammar.semantic.ISemanticRecorder;
import priv.bajdcc.LALR1.grammar.tree.closure.IClosureScope;

/**
 * 【语义类型】通用语义接口
 *
 * @author bajdcc
 */
public interface ICommon {

	/**
	 * 语义分析
	 *
	 * @param recorder 错误记录器
	 */
	void analysis(ISemanticRecorder recorder);

	/**
	 * 生成中间代码
	 *
	 * @param codegen 代码生成接口
	 */
	void genCode(ICodegen codegen);

	/**
	 * 输出
	 *
	 * @param prefix 前缀空白
	 * @return 结点内容
	 */
	String print(StringBuilder prefix);

	/**
	 * 求函数闭包
	 *
	 * @param scope 闭包操作接口
	 */
	void addClosure(IClosureScope scope);
}
