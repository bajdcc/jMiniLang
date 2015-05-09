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

	public String toRealString() {
		if (object == null) {
			return "";
		}
		switch (kToken) {
		case KEYWORD:
			return ((KeywordType) object).getName();
		case OPERATOR:
			return ((OperatorType) object).getName();
		case STRING:
			return "\"" + object.toString() + "\"";
		case CHARACTER:
			char ch = (char) (int) object;
			return "'"
					+ (Character.isISOControl(ch) ? String.format("\\u%04x",
							(int) ch) : ch + "") + "'";
		case BOOL:
		case DECIMAL:
		case INTEGER:
		case ID:
			return object.toString();
		default:
			return "";
		}
	}

	private String toSimpleString() {
		switch (kToken) {
		case KEYWORD:
			return ((KeywordType) object).getName();
		case OPERATOR:
			return ((OperatorType) object).getName();
		default:
			return "";
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%04d,%03d:\t%s\t%s %s", position.iLine,
				position.iColumn, kToken.getName(), object == null ? "(null)"
						: object.toString(), toSimpleString()));
		return sb.toString();
	}
}