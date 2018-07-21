package com.bajdcc.LALR1.grammar.symbol

import com.bajdcc.LALR1.grammar.tree.Function
import com.bajdcc.util.lexer.token.Token

/**
 * 命名空间查询接口
 *
 * @author bajdcc
 */
interface IQueryScopeSymbol {

    /**
     * 获得入口名，一般为main
     *
     * @return 入口名
     */
    val entryName: String

    /**
     * 获得入口单词，一般为main
     *
     * @return 入口单词
     */
    val entryToken: Token

    /**
     * 得到当前的匿名函数
     *
     * @return 过程对象
     */
    val lambda: Function

    /**
     * 查找变量名
     *
     * @param name 查询的符号名
     * @return 符号名是否存在
     */
    fun findDeclaredSymbol(name: String): Boolean

    /**
     * 查找变量名（当前命名空间）
     *
     * @param name 查询的符号名
     * @return 符号名是否存在
     */
    fun findDeclaredSymbolDirect(name: String): Boolean

    /**
     * 在当前块下是否为唯一符号（检查重复定义）
     *
     * @param name 符号名
     * @return 是否唯一
     */
    fun isUniqueSymbolOfBlock(name: String): Boolean

    /**
     * 根据过程名查找过程对象
     *
     * @param name 查询的过程名
     * @return 过程对象
     */
    fun getFuncByName(name: String): Function?

    /**
     * 判断是否为lambda函数
     *
     * @param name 函数名
     * @return 是否为lambda
     */
    fun isLambda(name: String): Boolean

    /**
     * 过程名是否已被占用
     *
     * @param name 查询的过程名
     * @return 是否占用
     */
    fun isRegisteredFunc(name: String): Boolean
}