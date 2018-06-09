package com.bajdcc.util;

/**
 * 语句错误参数包
 *
 * @author bajdcc
 */
public class TrackerErrorBag {

	public TrackerErrorBag(Position pos) {
		position = pos;
	}

	/**
	 * 若为假，则状态机不进行错误状态转移
	 */
	public boolean bPass = false;

	/**
	 * 若为假，则状态机不跳过当前记号
	 */
	public boolean bRead = true;

	/**
	 * 若为假，则状态机不结束分析
	 */
	public boolean bHalt = false;

	/**
	 * 若为真，则不处理错误
	 */
	public boolean bGiveUp = false;

	/**
	 * 错误位置
	 */
	public Position position;
}
