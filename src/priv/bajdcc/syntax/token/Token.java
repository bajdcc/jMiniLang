package priv.bajdcc.syntax.token;

import priv.bajdcc.utility.Position;

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
		sb.append(String.format("%04d,%03d: %s %s", position.iLine,
				position.iColumn, kToken.getName(),
				object == null ? "(null)" : object.toString()));
		return sb.toString();
	}

	public static Token transfer(priv.bajdcc.lexer.token.Token token) {
		Token tk = new Token();
		tk.object = token.object;
		tk.position = token.position;
		switch (token.kToken) {
		case COMMENT:
			tk.kToken = TokenType.COMMENT;
			break;
		case EOF:
			tk.kToken = TokenType.EOF;
			break;
		case ERROR:
			tk.kToken = TokenType.ERROR;
			break;
		case ID:
			tk.kToken = TokenType.NONTERMINAL;
			break;
		case MACRO:
			tk.kToken = TokenType.HANDLER;
			break;
		case OPERATOR:
			tk.kToken = TokenType.OPERATOR;
			break;
		case STRING:
			tk.kToken = TokenType.TERMINAL;
			break;
		case WHITESPACE:
			tk.kToken = TokenType.WHITSPACE;
			break;
		case INTEGER:
			tk.kToken = TokenType.STORAGE;
			break;
		default:
			break;
		}
		return tk;
	}
}