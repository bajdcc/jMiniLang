package com.bajdcc.util.lexer.stringify

import com.bajdcc.util.lexer.regex.Charset
import com.bajdcc.util.lexer.regex.Constructure
import com.bajdcc.util.lexer.regex.IRegexComponentVisitor
import com.bajdcc.util.lexer.regex.Repetition

class RegexToString : IRegexComponentVisitor {

    /**
     * 正则表达式的树型表达
     */
    private val context = StringBuilder()

    /**
     * 前缀
     */
    private val prefix = StringBuilder()

    /**
     * 前缀缩进
     */
    private fun appendPrefix() {
        prefix.append('\t')
        context.append(" {").append(System.lineSeparator())
    }

    /**
     * 取消前缀缩进
     */
    private fun reducePrefix() {
        prefix.deleteCharAt(0)
        context.append(prefix).append("}").append(System.lineSeparator())
    }

    override fun visitBegin(node: Charset) {
        context.append(prefix).append("字符")
        context.append(if (node.bReverse) "[取反]" else "").append("\t").append(node)
        context.append(System.lineSeparator())
    }

    override fun visitBegin(node: Constructure) {
        context.append(prefix).append(if (node.branch) "分支" else "序列")
        appendPrefix()
    }

    override fun visitBegin(node: Repetition) {
        context.append(prefix.toString()).append("循环{").append(node.lowerBound).append(",").append(node.upperBound).append("}")
        appendPrefix()
    }

    override fun visitEnd(node: Charset) {

    }

    override fun visitEnd(node: Constructure) {
        reducePrefix()
    }

    override fun visitEnd(node: Repetition) {
        reducePrefix()
    }

    override fun toString(): String {
        return context.toString()
    }
}
