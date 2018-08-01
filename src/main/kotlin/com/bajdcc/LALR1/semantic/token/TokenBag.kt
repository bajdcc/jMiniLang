package com.bajdcc.LALR1.semantic.token

import com.bajdcc.LALR1.grammar.tree.Block
import com.bajdcc.LALR1.grammar.tree.Func
import com.bajdcc.LALR1.grammar.tree.IExp
import com.bajdcc.LALR1.grammar.tree.IStmt
import com.bajdcc.util.lexer.token.Token

/**
 * 单词包
 * [token] 保存的单词
 * [obj] 保存的对象
 * @author bajdcc
 */
data class TokenBag(var token: Token? = null, var obj: Any? = null) {

    fun toExp() = obj!! as IExp
    fun toExps() = (obj!! as List<*>).map { it as IExp }.toMutableList()
    fun toStmt() = obj!! as IStmt
    fun toStmts() = (obj!! as List<*>).map { it as IStmt }.toMutableList()
    fun toTokens() = (obj!! as List<*>).map { it as Token }.toMutableList()
    fun toBlock() = obj!! as Block
    fun toFunc() = obj!! as Func
}