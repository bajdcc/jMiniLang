package com.bajdcc.LALR1.grammar.tree.closure

/**
 * 闭包
 *
 * @author bajdcc
 */
interface IClosureScope {

    fun addRef(obj: Any)

    fun addDecl(obj: Any)
}
