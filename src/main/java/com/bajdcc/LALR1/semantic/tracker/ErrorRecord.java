package com.bajdcc.LALR1.semantic.tracker;

import java.util.ArrayList;

/**
 * 错误记录器链表
 *
 * @author bajdcc
 */
public class ErrorRecord {

	/**
	 * 错误集
	 */
	public ArrayList<TrackerError> arrErrors = new ArrayList<>();

	/**
	 * 前向指针
	 */
	public ErrorRecord prev = null;

	public ErrorRecord(ErrorRecord prev) {
		this.prev = prev;
	}
}
