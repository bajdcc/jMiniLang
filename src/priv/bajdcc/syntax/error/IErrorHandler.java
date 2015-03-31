package priv.bajdcc.syntax.error;

import priv.bajdcc.syntax.ISyntaxToken;
import priv.bajdcc.utility.SyntaxErrorBag;

/**
 * 语法错误处理接口
 *
 * @author bajdcc
 */
public interface IErrorHandler {
	/**
	 * 处理错误
	 * 
	 * @param token
	 *            记号
	 * @param bag
	 *            参数信息
	 * @return 错误信息
	 */
	public String handle(ISyntaxToken token, SyntaxErrorBag bag);
}
