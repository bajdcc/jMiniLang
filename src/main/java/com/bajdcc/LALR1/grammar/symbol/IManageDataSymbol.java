package com.bajdcc.LALR1.grammar.symbol;

import com.bajdcc.LALR1.grammar.tree.Function;
import com.bajdcc.util.HashListMap;
import com.bajdcc.util.HashListMapEx;

import java.util.List;

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
	HashListMapEx<String, List<Function>> getFuncMap();
}
