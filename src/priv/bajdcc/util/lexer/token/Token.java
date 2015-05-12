package priv.bajdcc.util.lexer.token;

import java.math.BigDecimal;
import java.math.BigInteger;

import priv.bajdcc.util.Position;

/**
 * 单词
 * 
 * @author bajdcc
 */
public class Token implements Cloneable {
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
		return getRealString(kToken, object);
	}

	public static String getRealString(TokenType type, Object object) {
		if (object == null) {
			return "";
		}
		switch (type) {
		case KEYWORD:
			return ((KeywordType) object).getName();
		case OPERATOR:
			return ((OperatorType) object).getName();
		case STRING:
			return "\"" + object.toString() + "\"";
		case CHARACTER:
			char ch = (char) object;
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

	public Token copy() {
		try {
			return (Token) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Token createFromObject(Object object) {
		Token token = new Token();
		if (object instanceof String) {
			token.kToken = TokenType.STRING;
		} else if (object instanceof Character) {
			token.kToken = TokenType.CHARACTER;
		} else if (object instanceof BigInteger) {
			token.kToken = TokenType.INTEGER;
		} else if (object instanceof BigDecimal) {
			token.kToken = TokenType.DECIMAL;
		} else if (object instanceof Boolean) {
			token.kToken = TokenType.BOOL;
		} else {
			token.kToken = TokenType.ERROR;
			return token;
		}
		token.object = object;
		return token;
	}
}