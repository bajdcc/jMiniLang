package priv.bajdcc.util.lexer.token;

import priv.bajdcc.util.Position;

/**
 * 单词
 * 
 * @author bajdcc
 */
public class Token {
	/**
	 * 单词类型
	 */
	public TokenType kToken = TokenType.ERROR;

	/**
	 * 数据
	 */
	public Object object = null;

	/**
	 * 位置
	 */
	public Position position = new Position();

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%04d,%03d:\t%s\t%s", position.iLine, position.iColumn,
				kToken.getName(),
				object == null ? "(null)" : object.toString()));
		return sb.toString();
	}
}