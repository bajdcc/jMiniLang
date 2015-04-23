package priv.bajdcc.OP.grammar.error;

import priv.bajdcc.util.Position;

/**
 * 文法生成过程中的异常
 * 
 * @author bajdcc
 *
 */
@SuppressWarnings("serial")
public class GrammarException extends Exception {

	/**
	 * 文法推导式解析过程中的错误
	 */
	public enum GrammarError {
		NULL("输入为空串"), UNDECLARED("无法识别的符号"), SYNTAX("语法错误"), MISS_PRECEDENCE(
				"操作符缺少优先级"), MISS_HANDLER("缺少处理模式");

		private String message;

		GrammarError(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	};

	public GrammarException(GrammarError error, Position pos, Object obj) {
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
	private GrammarError kError = GrammarError.SYNTAX;

	/**
	 * @return 错误类型
	 */
	public GrammarError getErrorCode() {
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
