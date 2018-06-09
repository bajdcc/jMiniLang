package com.bajdcc.LALR1.syntax.automata.nga;

import com.bajdcc.LALR1.syntax.ISyntaxComponent;

import java.util.ArrayList;
import java.util.Stack;

/**
 * 非确定性文法自动机结果包
 *
 * @author bajdcc
 */
public class NGABag {
	/**
	 * NGA栈
	 */
	public Stack<ArrayList<ENGA>> stkNGA = new Stack<>();

	/**
	 * NGA子表
	 */
	public ArrayList<ENGA> childNGA = new ArrayList<>();

	/**
	 * 存储结果的ENGA
	 */
	public ENGA nga = null;

	/**
	 * 标记前缀
	 */
	public String prefix = "";

	/**
	 * 标记前缀
	 */
	public ISyntaxComponent expression = null;
}
