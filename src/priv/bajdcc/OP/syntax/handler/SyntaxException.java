package priv.bajdcc.OP.syntax.handler;

import priv.bajdcc.util.Position;

/**
 * 文法生成过程中的异常
 * 
 * @author bajdcc
 *
 */
@SuppressWarnings("serial")
public class SyntaxException extends Exception {

	/**
	 * 文法推导式解析过程中的错误
	 */
	public enum SyntaxError {
		NULL("推导式为空"), UNDECLARED("无法识别的符号"), SYNTAX("语法错误"), INCOMPLETE(
				"推导式不完整"), FAILED("不能产生字符串"), CONSEQUENT_NONTERMINAL(
				"存在连续的非终结符"), REDECLARATION("重复定义");

		private String message;

		SyntaxError(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	};

	public SyntaxException(SyntaxError error, Position pos, Object obj) {
		super(error.getMessage());
		position = pos;
		kError = error;
		if (obj != null) {
			info = obj.toString();
		}
	}

	/**
	 * 位置
	 */
	private Position position = new Position();

	/**
	 * @return 错误位置
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * 错误类型
	 */
	private SyntaxError kError = SyntaxError.NULL;

	/**
	 * @return 错误类型
	 */
	public SyntaxError getErrorCode() {
		return kError;
	}

	/**
	 * 错误信息
	 */
	private String info = "";

	/**
	 * @return 错误信息
	 */
	public String getInfo() {
		return info;
	}
}
