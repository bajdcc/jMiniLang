package priv.bajdcc.util.lexer.algorithm;

import priv.bajdcc.util.Position;
import priv.bajdcc.util.lexer.error.RegexException;
import priv.bajdcc.util.lexer.regex.IRegexStringAttribute;
import priv.bajdcc.util.lexer.regex.IRegexStringFilter;
import priv.bajdcc.util.lexer.regex.IRegexStringIterator;
import priv.bajdcc.util.lexer.regex.Regex;
import priv.bajdcc.util.lexer.token.MetaType;
import priv.bajdcc.util.lexer.token.Token;
import priv.bajdcc.util.lexer.token.TokenType;

import java.util.HashMap;

/**
 * 单词识别算法
 *
 * @author bajdcc
 */

/**
 * @author bajdcc
 *
 */
public abstract class TokenAlgorithm implements ITokenAlgorithm,
		IRegexStringAttribute {

	/**
	 * 用来匹配的正则表达式
	 */
	protected Regex regex = null;

	/**
	 * 匹配结果
	 */
	protected String strMatch = "";

	/**
	 * 字符过滤接口
	 */
	protected IRegexStringFilter filter = null;

	/**
	 * 字符类型哈段表
	 */
	protected HashMap<Character, MetaType> mapMeta = new HashMap<>();

	public TokenAlgorithm(String regex, IRegexStringFilter filter)
			throws RegexException {
		this.regex = new Regex(regex);
		if (filter != null) {
			this.filter = filter;
			this.regex.setFilter(filter);
			MetaType[] metaTypes = filter.getFilterMeta().getMetaTypes();
			for (MetaType metaType : metaTypes) {
				mapMeta.put(metaType.getChar(), metaType);
			}
		}
	}

	@Override
	public boolean accept(IRegexStringIterator iterator, Token token) {
		if (!iterator.available()) {
			token.kToken = TokenType.EOF;
			return true;
		}
		token.position = new Position(iterator.position());
		iterator.snapshot();
		if (regex.match(iterator, this)) {// 匹配成功
			if (getToken(strMatch, token, iterator) != null) {// 自动转换单词
				iterator.discard();
				return true;
			}
		}
		iterator.restore();
		return false;
	}

	@Override
	public boolean getGreedMode() {
		return false;// 默认为非贪婪模式
	}

	@Override
	public IRegexStringFilter getRegexStringFilter() {
		return filter;
	}

	@Override
	public HashMap<Character, MetaType> getMetaHash() {
		return mapMeta;
	}

	@Override
	public void setResult(String result) {
		strMatch = result;
	}

	@Override
	public String getResult() {
		return strMatch;
	}

	@Override
	public String getRegexDescription() {
		return regex.getRegexDescription();
	}
}
