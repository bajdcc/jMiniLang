package com.bajdcc.LALR1.grammar.tree.closure;

/**
 * 闭包
 *
 * @author bajdcc
 */
public interface IClosureScope {

	void addRef(Object obj);

	void addDecl(Object obj);
}
