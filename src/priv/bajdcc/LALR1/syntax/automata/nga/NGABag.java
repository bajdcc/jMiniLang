package priv.bajdcc.LALR1.syntax.automata.nga;

import java.util.ArrayList;
import java.util.Stack;

import priv.bajdcc.LALR1.syntax.ISyntaxComponent;

/**
 * 非确定性文法自动机结果包
 *
 * @author bajdcc
 */
public class NGABag {
	/**
	 * NGA栈
	 */
	public Stack<ArrayList<ENGA>> stkNGA = new Stack<ArrayList<ENGA>>();

	/**
	 * NGA子表
	 */
	public ArrayList<ENGA> childNGA = new ArrayList<ENGA>();

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
