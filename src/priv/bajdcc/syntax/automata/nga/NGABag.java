package priv.bajdcc.syntax.automata.nga;

import java.util.ArrayList;
import java.util.Stack;

import priv.bajdcc.syntax.ISyntaxComponent;

/**
 * 非确定性文法自动机结果包
 *
 * @author bajdcc
 */
public class NGABag {
	/**
	 * NGA栈
	 */
	public Stack<ArrayList<ENGA>> m_stkNGA = new Stack<ArrayList<ENGA>>();

	/**
	 * NGA子表
	 */
	public ArrayList<ENGA> m_childNGA = new ArrayList<ENGA>();

	/**
	 * 存储结果的ENGA
	 */
	public ENGA m_outputNGA = null;

	/**
	 * 标记前缀
	 */
	public String m_strPrefix = "";

	/**
	 * 标记前缀
	 */
	public ISyntaxComponent m_Expression = null;
}
