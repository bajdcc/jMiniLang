package priv.bajdcc.syntax.error;

import priv.bajdcc.utility.Position;

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
				"推导式不完整"), EPSILON("可能产生空串"), INDIRECT_RECURSION("存在间接左递归"), FAILED(
				"不能产生字符串"), MISS_NODEPENDENCY_RULE("找不到无最左依赖的规则");

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
		m_Position = pos;
		m_kError = error;
		if (obj != null) {
			m_strInfo = obj.toString();
		}
	}

	/**
	 * 位置
	 */
	private Position m_Position = new Position();

	/**
	 * @return 错误位置
	 */
	public Position getPosition() {
		return m_Position;
	}

	/**
	 * 错误类型
	 */
	private SyntaxError m_kError = SyntaxError.NULL;

	/**
	 * @return 错误类型
	 */
	public SyntaxError getErrorCode() {
		return m_kError;
	}

	/**
	 * 错误信息
	 */
	private String m_strInfo = "";

	/**
	 * @return 错误信息
	 */
	public String getInfo() {
		return m_strInfo;
	}
}
