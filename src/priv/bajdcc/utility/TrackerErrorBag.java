package priv.bajdcc.utility;

/**
 * 语句错误参数包
 *
 * @author bajdcc
 */
public class TrackerErrorBag {

	/**
	 * 若为假，则状态机不进行错误状态转移
	 */
	public boolean m_bPass = false;

	/**
	 * 若为假，则状态机不跳过当前记号
	 */
	public boolean m_bRead = true;

	/**
	 * 若为假，则状态机不结束分析
	 */
	public boolean m_bHalt = false;

	/**
	 * 若为真，则不处理错误
	 */
	public boolean m_bGiveUp = false;
}
