package priv.bajdcc.LALR1.grammar.symbol;

import priv.bajdcc.LALR1.grammar.tree.Function;
import priv.bajdcc.util.HashListMap;
import priv.bajdcc.util.HashListMapEx;

/**
 * 符号表数据接口
 *
 * @author bajdcc
 */
public interface IManageDataSymbol {

	/**
	 * @return 符号表
	 */
	HashListMap<Object> getSymbolList();

	/**
	 * @return 过程表
	 */
	HashListMapEx<String, Function> getFuncMap();
}
