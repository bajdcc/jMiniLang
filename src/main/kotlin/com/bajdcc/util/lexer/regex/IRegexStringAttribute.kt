package com.bajdcc.util.lexer.regex

/**
 * 匹配信息
 *
 * @author bajdcc
 */
interface IRegexStringAttribute {

    /**
     * 设置匹配结果
     */
    var result: String

    /**
     * 返回贪婪模式
     */
    val greedMode: Boolean
}
