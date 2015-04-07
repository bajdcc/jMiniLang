package priv.bajdcc.semantic.token;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Stack;

import org.vibur.objectpool.ConcurrentLinkedPool;
import org.vibur.objectpool.PoolService;

import priv.bajdcc.lexer.token.Token;
import priv.bajdcc.utility.ObjectFactory;

/**
 * 单词解析栈
 *
 * @author bajdcc
 */
public class ParsingStack implements IIndexedData {

	/**
	 * 当前索引表
	 */
	private HashMap<Integer, TokenBag> m_mapTokenBag = null;

	/**
	 * 索引池
	 */
	private PoolService<HashMap<Integer, TokenBag>> m_IndexedTokensPool = new ConcurrentLinkedPool<HashMap<Integer, TokenBag>>(
			new ObjectFactory<HashMap<Integer, TokenBag>>() {
				public HashMap<Integer, TokenBag> create() {
					return new HashMap<Integer, TokenBag>();
				};
			}, 100, 1000, false);

	/**
	 * 单词包栈
	 */
	private Stack<HashMap<Integer, TokenBag>> m_stkMapTokenBags = new Stack<HashMap<Integer, TokenBag>>();

	public ParsingStack() {
		push();
	}

	/**
	 * 放入一个索引数据，同时置当前为栈顶
	 */
	public void push() {
		m_mapTokenBag = m_IndexedTokensPool.take();
		m_stkMapTokenBags.push(m_mapTokenBag);
	}

	/**
	 * 弹出一个索引数据，同时置当前为栈顶
	 */
	public void pop() {
		if (!m_stkMapTokenBags.isEmpty()) {
			m_IndexedTokensPool.restore(m_stkMapTokenBags.pop());
			if (!m_stkMapTokenBags.isEmpty()) {
				m_mapTokenBag = m_stkMapTokenBags.peek();
			} else {
				m_mapTokenBag = null;
			}
		}
	}

	/**
	 * 设置索引数据
	 * 
	 * @param index
	 *            索引位置
	 * @param token
	 *            单词
	 */
	public void set(int index, Token token) {
		if (m_mapTokenBag != null) {
			m_mapTokenBag.put(index, new TokenBag(token));
		}
	}

	/**
	 * 设置索引数据
	 * 
	 * @param index
	 *            索引位置
	 * @param obj
	 *            对象
	 */
	public void set(int index, Object obj) {
		if (m_mapTokenBag != null) {
			m_mapTokenBag.put(index, new TokenBag(obj));
		}
	}

	@Override
	public TokenBag get(int index) {
		if (m_mapTokenBag == null) {
			return null;
		} else {
			return m_mapTokenBag.get(index);
		}
	}

	private static void printTokenBag(StringBuilder sb,
			HashMap<Integer, TokenBag> bags) {
		if (bags == null) {

		} else if (bags.isEmpty()) {
			sb.append("(empty)");
		} else {
			for (Entry<Integer, TokenBag> bag : bags.entrySet()) {
				sb.append("[" + bag.getKey() + ": ");
				if (bag.getValue().m_Token != null) {
					sb.append(bag.getValue().m_Token.toString());
				} else {
					sb.append(bag.getValue().m_Object.toString());
				}
				sb.append("]");
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("-------- stack begin --------");
		sb.append(System.getProperty("line.separator"));
		sb.append("0: ");
		printTokenBag(sb, m_mapTokenBag);
		sb.append(System.getProperty("line.separator"));
		int i = 1;
		for (HashMap<Integer, TokenBag> hashMap : m_stkMapTokenBags) {
			sb.append(i + ": ");
			printTokenBag(sb, hashMap);
			sb.append(System.getProperty("line.separator"));
			i++;
		}
		sb.append("-------- stack end --------");
		return sb.toString();
	}
}
