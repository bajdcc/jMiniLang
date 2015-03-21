package priv.bajdcc.lexer.regex;

import java.util.Stack;

import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.error.RegexException.RegexError;
import priv.bajdcc.lexer.token.MetaType;
import priv.bajdcc.lexer.utility.Position;

/**
 * 字符串迭代器，提供字节流解析功能
 * 
 * @author bajdcc
 *
 */
public abstract class RegexStringIterator implements IRegexStringIterator {
	/**
	 * 存储字符串
	 */
	protected String m_strContext;

	/**
	 * 用于恢复的位置堆栈
	 */
	public Stack<Integer> m_stkIndex = new Stack<Integer>();

	/**
	 * 位置
	 */
	protected Position m_Position = new Position();

	/**
	 * 用于恢复行列数的堆栈
	 */
	public Stack<Position> m_stkPosition = new Stack<Position>();

	/**
	 * 当前的分析信息
	 */
	protected RegexStringIteratorData m_Data = new RegexStringIteratorData();

	public RegexStringIterator(String strContext) {
		m_strContext = strContext;
	}

	/**
	 * 字符解析组件
	 */
	protected RegexStringUtility m_Utility = new RegexStringUtility(this);

	@Override
	public void err(RegexError error) throws RegexException {
		throw new RegexException(error, m_Position);
	}

	@Override
	public void next() {
		if (available()) {
			advance();
		}
		translate();
		m_Position.m_iColumn++;
		if (m_Data.m_chCurrent == MetaType.NEW_LINE.getChar()) {
			m_Position.m_iColumn = 0;
			m_Position.m_iLine++;
		}
	}

	@Override
	public Position position() {
		return m_Position;
	}

	@Override
	public void translate() {
		if (!available()) {
			m_Data.m_chCurrent = 0;
			m_Data.m_kMeta = MetaType.END;
			return;
		}
		m_Data.m_chCurrent = current();
		transform();
	}

	/**
	 * 分析字符类型
	 */
	protected void transform() {
		m_Data.m_kMeta = MetaType.CHARACTER;
	}

	@Override
	public boolean available() {
		return m_Data.m_iIndex >= 0 && m_Data.m_iIndex < m_strContext.length();
	}

	@Override
	public void advance() {
		m_Data.m_iIndex++;
	}

	@Override
	public char current() {
		return m_strContext.charAt(m_Data.m_iIndex);
	}

	@Override
	public MetaType meta() {
		return m_Data.m_kMeta;
	}

	@Override
	public int index() {
		return m_Data.m_iIndex;
	}

	@Override
	public void expect(MetaType meta, RegexError error) throws RegexException {
		if (m_Data.m_kMeta == meta) {
			next();
		} else {
			err(error);
		}
	}

	@Override
	public void snapshot() {
		m_stkIndex.push(m_Data.m_iIndex);
		m_stkPosition.push(new Position(m_Position.m_iColumn, m_Position.m_iLine));
	}

	@Override
	public void cover() {
		m_stkIndex.set(m_stkIndex.size() - 1, m_Data.m_iIndex);
		m_stkPosition.set(m_stkPosition.size() - 1, m_Position);
	}

	@Override
	public void restore() {
		m_Data.m_iIndex = m_stkIndex.pop();
		m_Position = m_stkPosition.pop();
	}

	@Override
	public void discard() {
		m_stkIndex.pop();
		m_stkPosition.pop();
	}

	@Override
	public RegexStringUtility utility() {
		return m_Utility;
	}
}