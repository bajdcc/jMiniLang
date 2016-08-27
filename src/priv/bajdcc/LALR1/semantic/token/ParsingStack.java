package priv.bajdcc.LALR1.semantic.token;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Stack;

import priv.bajdcc.util.lexer.token.Token;

/**
 * 单词解析栈
 *
 * @author bajdcc
 */
public class ParsingStack implements IIndexedData {

	/**
	 * 当前索引表
	 */
	private HashMap<Integer, TokenBag> mapTokenBag = null;

	/**
	 * 单词包栈
	 */
	private Stack<HashMap<Integer, TokenBag>> stkMapTokenBags = new Stack<>();

	public ParsingStack() {
		push();
	}

	/**
	 * 放入一个索引数据，同时置当前为栈顶
	 */
	public void push() {
		mapTokenBag = new HashMap<>();
		stkMapTokenBags.push(mapTokenBag);
	}

	/**
	 * 弹出一个索引数据，同时置当前为栈顶
	 */
	public void pop() {
		if (!stkMapTokenBags.isEmpty()) {
			stkMapTokenBags.pop();
			if (!stkMapTokenBags.isEmpty()) {
				mapTokenBag = stkMapTokenBags.peek();
			} else {
				mapTokenBag = null;
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
		if (mapTokenBag != null) {
			mapTokenBag.put(index, new TokenBag(token));
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
		if (mapTokenBag != null) {
			mapTokenBag.put(index, new TokenBag(obj));
		}
	}

	@Override
	public TokenBag get(int index) {
		if (mapTokenBag == null) {
			return null;
		} else {
			return mapTokenBag.get(index);
		}
	}

	@Override
	public boolean exists(int index) {
		return mapTokenBag.containsKey(index);
	}

	private static void printTokenBag(StringBuilder sb,
			HashMap<Integer, TokenBag> bags) {
		if (bags != null) {
			if (bags.isEmpty()) {
                sb.append("(empty)");
            } else {
                for (Entry<Integer, TokenBag> bag : bags.entrySet()) {
                    sb.append("[").append(bag.getKey()).append(": ");
                    if (bag.getValue().token != null) {
                        sb.append(bag.getValue().token.toString());
                    } else {
                        sb.append(bag.getValue().object.toString());
                    }
                    sb.append("]");
                }
            }
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("-------- stack begin --------");
		sb.append(System.lineSeparator());
		sb.append("0: ");
		printTokenBag(sb, mapTokenBag);
		sb.append(System.lineSeparator());
		int i = 1;
		for (HashMap<Integer, TokenBag> hashMap : stkMapTokenBags) {
			sb.append(i).append(": ");
			printTokenBag(sb, hashMap);
			sb.append(System.lineSeparator());
			i++;
		}
		sb.append("-------- stack end --------");
		return sb.toString();
	}
}
