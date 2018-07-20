package com.bajdcc.util.lexer.regex

/**
 * 基于正则表达式组件的访问接口（Visitor模式）
 *
 * @author bajdcc
 */
interface IRegexComponentVisitor {

    fun visitBegin(node: Charset)

    fun visitBegin(node: Constructure)

    fun visitBegin(node: Repetition)

    fun visitEnd(node: Charset)

    fun visitEnd(node: Constructure)

    fun visitEnd(node: Repetition)
}
