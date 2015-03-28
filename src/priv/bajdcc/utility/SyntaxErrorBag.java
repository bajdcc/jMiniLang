package priv.bajdcc.utility;

/**
 * 语句错误参数包
 *
 * @author bajdcc
 */
public class SyntaxErrorBag {

	/**
	 * 若为假则状态机不进行状态转换
	 */
	public boolean m_bPass = false;

	/**
	 * 若为假则状态机不跳过当前记号
	 */
	public boolean m_bRead = false;

	/**
	 * 若为假则状态机不结束分析
	 */
	public boolean m_bHalt = false;

	/**
	 * 若为真则不处理本次消息
	 */
	public boolean m_bGiveUp = false;
}
