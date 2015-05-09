package priv.bajdcc.LALR1.grammar.error;

import priv.bajdcc.util.lexer.token.Token;

/**
 * 【语义分析】语义错误结构
 *
 * @author bajdcc
 */
@SuppressWarnings("serial")
public class SemanticException extends Exception {

	/**
	 * 语义分析过程中的错误
	 */
	public enum SemanticError {
		UNKNOWN("未知"), INVALID_OPERATOR("操作非法"), MISSING_FUNCNAME("过程名不存在"), DUP_ENTRY(
				"不能设置为入口函数名"), DUP_FUNCNAME("重复的函数名"), VARIABLE_NOT_DECLARAED(
				"变量未定义"), VARIABLE_REDECLARAED("变量重复定义");

		private String message;

		SemanticError(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	};

	public SemanticException(SemanticError error, Token token) {
		super(error.getMessage());
		this.token = token;
		kError = error;
	}

	/**
	 * 操作符
	 */
	private Token token = new Token();

	/**
	 * @return 错误位置
	 */
	public Token getPosition() {
		return token;
	}

	/**
	 * 错误类型
	 */
	private SemanticError kError = SemanticError.UNKNOWN;

	/**
	 * @return 错误类型
	 */
	public SemanticError getErrorCode() {
		return kError;
	}

	@Override
	public String toString() {
		return getMessage() + ": " + token.toString();
	}
}
