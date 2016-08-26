package priv.bajdcc.util.lexer.regex;

import java.util.ArrayList;
import java.util.Stack;

import priv.bajdcc.util.Position;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.error.RegexException.RegexError;
import priv.bajdcc.util.lexer.token.MetaType;
import priv.bajdcc.util.lexer.token.Token;

/**
 * 字符串迭代器，提供字节流解析功能
 * 
 * @author bajdcc
 *
 */
public class RegexStringIterator implements IRegexStringIterator, Cloneable {

	/**
	 * 存储字符串
	 */
	protected String context;

	/**
	 * 用于恢复的位置堆栈
	 */
	public Stack<Integer> stkIndex = new Stack<Integer>();

	/**
	 * 位置
	 */
	protected Position position = new Position();

	/**
	 * 用于恢复行列数的堆栈
	 */
	public Stack<Position> stkPosition = new Stack<Position>();

	/**
	 * 当前的分析信息
	 */
	protected RegexStringIteratorData data = new RegexStringIteratorData();
	
	/**
	 * 记录每行起始的位置
	 */
	protected ArrayList<Integer> arrLinesNo = new ArrayList<Integer>();

	public RegexStringIterator() {
		arrLinesNo.add(0);
	}

	public RegexStringIterator(String context) {
		this();
		this.context = context;
	}

	/**
	 * 字符解析组件
	 */
	protected RegexStringUtility utility = new RegexStringUtility(this);

	@Override
	public void err(RegexError error) throws RegexException {
		throw new RegexException(error, position);
	}

	@Override
	public void next() {
		if (available()) {
			advance();
		}
		translate();
		position.iColumn++;
		if (data.chCurrent == MetaType.NEW_LINE.getChar()) {
			arrLinesNo.add(arrLinesNo.get(arrLinesNo.size() - 1) + position.iColumn + 1);
			position.iColumn = 0;
			position.iLine++;
		}
	}

	@Override
	public Token scan() {
		return null;
	}

	@Override
	public Position position() {
		return position;
	}

	@Override
	public void translate() {
		if (!available()) {
			data.chCurrent = 0;
			data.kMeta = MetaType.END;
			return;
		}
		data.chCurrent = current();
		transform();
	}

	/**
	 * 分析字符类型
	 */
	protected void transform() {
		data.kMeta = MetaType.CHARACTER;
	}

	@Override
	public boolean available() {
		return data.iIndex >= 0 && data.iIndex < context.length();
	}

	@Override
	public void advance() {
		data.iIndex++;
	}

	@Override
	public char current() {
		return context.charAt(data.iIndex);
	}

	@Override
	public MetaType meta() {
		return data.kMeta;
	}

	@Override
	public int index() {
		return data.iIndex;
	}

	@Override
	public void expect(MetaType meta, RegexError error) throws RegexException {
		if (data.kMeta == meta) {
			next();
		} else {
			err(error);
		}
	}

	@Override
	public void snapshot() {
		stkIndex.push(data.iIndex);
		stkPosition.push(new Position(position.iColumn,
				position.iLine));
	}

	@Override
	public void cover() {
		stkIndex.set(stkIndex.size() - 1, data.iIndex);
		stkPosition.set(stkPosition.size() - 1, new Position(position));
	}

	@Override
	public void restore() {
		data.iIndex = stkIndex.pop();
		position = new Position(stkPosition.pop());
	}

	@Override
	public void discard() {
		stkIndex.pop();
		stkPosition.pop();
	}

	@Override
	public RegexStringUtility utility() {
		return utility;
	}

	@Override
	public String getRegexDescription() {
		return context;
	}

	@Override
	public IRegexStringIterator copy() {
		return null;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		RegexStringIterator o = (RegexStringIterator) super.clone();
		o.position = (Position) o.position.clone();
		o.data = (RegexStringIteratorData) o.data.clone();
		return o;
	}

	@Override
	public IRegexStringIteratorEx ex() {
		return null;
	}
}