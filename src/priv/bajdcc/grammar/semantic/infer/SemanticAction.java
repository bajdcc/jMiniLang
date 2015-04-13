package priv.bajdcc.grammar.semantic.infer;

import java.util.HashMap;

import priv.bajdcc.grammar.expression.Expression;
import priv.bajdcc.grammar.expression.FixedResult;
import priv.bajdcc.semantic.token.IIndexedData;
import priv.bajdcc.semantic.token.ISemanticHandler;
import priv.bajdcc.semantic.token.ITokenFactory;

/**
 * 【语义动作】语义动作集合
 *
 * @author bajdcc
 */
public class SemanticAction {

	/**
	 * 语义动作映射表
	 */
	private HashMap<String, ISemanticHandler> mapSemanticHandler = new HashMap<String, ISemanticHandler>();

	public SemanticAction() {
		initialize();
	}

	/**
	 * 初始化
	 */
	private void initialize() {
		/* 原始表达式 */
		mapSemanticHandler.put("primary_expression", new ISemanticHandler() {
			@Override
			public Object handle(IIndexedData indexed, ITokenFactory factory,
					Object obj) {
				Expression exp = new Expression();
				if (indexed.exist(1)) {// 是否是表达式而不是单词
					return indexed.get(1).object;
				}
				exp.token = indexed.get(0).token;
				return new FixedResult(exp);
			}
		});
		/* 后缀表达式 */
		mapSemanticHandler.put("postfix_expression", new ISemanticHandler() {
			@Override
			public Object handle(IIndexedData indexed, ITokenFactory factory,
					Object obj) {
				if (indexed.exist(0)) {
					return indexed.get(0).object;
				}
				return null;
			}
		});
	}

	/**
	 * 获得语义动作
	 * 
	 * @param name
	 *            语义动作名称
	 * @return 语义动作
	 */
	public ISemanticHandler get(String name) {
		return mapSemanticHandler.get(name);
	}
}
