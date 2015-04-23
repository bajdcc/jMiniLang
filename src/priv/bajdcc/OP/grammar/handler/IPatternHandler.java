package priv.bajdcc.OP.grammar.handler;

import java.util.List;

import priv.bajdcc.util.lexer.token.Token;

/**
 * 归约动作处理器
 *
 * @author bajdcc
 */
public interface IPatternHandler {

	/**
	 * 处理
	 * 
	 * @param tokens
	 *            有序终结符（用于判定）
	 * @param symbols
	 *            有序非终结符（用于存储）
	 * @return 处理后的结果
	 */
	public Object handle(List<Token> tokens, List<Object> symbols);

	/**
	 * 获取归约动作描述
	 * 
	 * @return 动作描述
	 */
	public String getPatternName();
}
