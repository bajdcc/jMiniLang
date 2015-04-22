package priv.bajdcc.LALR1.grammar.semantic.infer;

import java.util.HashMap;

import priv.bajdcc.LALR1.grammar.expression.Expression;
import priv.bajdcc.LALR1.grammar.expression.FixedResult;
import priv.bajdcc.LALR1.grammar.symbol.IQuerySymbol;
import priv.bajdcc.LALR1.semantic.token.IIndexedData;
import priv.bajdcc.LALR1.semantic.token.ISemanticAnalyzier;
import priv.bajdcc.LALR1.syntax.handler.ISemanticAction;

/**
 * 【语义分析】语义处理器集合
 *
 * @author bajdcc
 */
public class SemanticHandler {

	/**
	 * 语义分析动作映射表
	 */
	private HashMap<String, ISemanticAnalyzier> mapSemanticAnalyzier = new HashMap<String, ISemanticAnalyzier>();

	/**
	 * 语义执行动作映射表
	 */
	private HashMap<String, ISemanticAction> mapSemanticAction = new HashMap<String, ISemanticAction>();

	public SemanticHandler() {
		initialize();
	}

	/**
	 * 初始化
	 */
	private void initialize() {
		/* 原始表达式 */
		mapSemanticAnalyzier.put("primary_expression", new ISemanticAnalyzier() {
			@Override
			public Object handle(IIndexedData indexed, IQuerySymbol factory) {
				Expression exp = new Expression();
				if (indexed.exist(1)) {// 是否是表达式而不是单词
					return indexed.get(1).object;
				}
				exp.token = indexed.get(0).token;
				return new FixedResult(exp);
			}
		});
		/* 后缀表达式 */
		mapSemanticAnalyzier.put("postfix_expression", new ISemanticAnalyzier() {
			@Override
			public Object handle(IIndexedData indexed, IQuerySymbol factory) {
				if (indexed.exist(0)) {
					return indexed.get(0).object;
				}
				return null;
			}
		});
	}

	/**
	 * 获得语义分析动作
	 * 
	 * @param name
	 *            语义分析动作名称
	 * @return 语义分析动作
	 */
	public ISemanticAnalyzier getSemanticHandler(String name) {
		return mapSemanticAnalyzier.get(name);
	}

	/**
	 * 获得语义执行动作
	 * 
	 * @param name
	 *            语义执行动作名称
	 * @return 语义执行动作
	 */
	public ISemanticAction getActionHandler(String name) {
		return mapSemanticAction.get(name);
	}
}
