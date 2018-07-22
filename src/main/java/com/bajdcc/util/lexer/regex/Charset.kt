package com.bajdcc.util.lexer.regex

import com.bajdcc.util.lexer.token.MetaType

/**
 * 字符集
 *
 * @author bajdcc
 */
class Charset : IRegexComponent {

    /**
     * 包含的范围（正范围）
     */
    var arrPositiveBounds = mutableListOf<CharacterRange>()

    /**
     * 是否取反
     */
    var bReverse = false

    override fun visit(visitor: IRegexComponentVisitor) {
        visitor.visitBegin(this)
        visitor.visitEnd(this)
    }

    /**
     * 添加范围
     *
     * @param begin 上限
     * @param end   下限
     * @return 是否添加成功
     */
    fun addRange(begin: Char, end: Char): Boolean {
        if (begin > end) {
            return false
        }
        arrPositiveBounds.forEach { range ->
            if (begin <= range.lowerBound && end >= range.upperBound)
                return false
        }
        arrPositiveBounds.add(CharacterRange(begin, end))
        return true
    }

    /**
     * 添加字符
     *
     * @param ch 字符
     * @return 添加是否有效
     */
    fun addChar(ch: Char): Boolean {
        return addRange(ch, ch)
    }

    /**
     * 当前字符集是否包含指定字符
     *
     * @param ch 字符
     * @return 查找结果
     */
    fun include(ch: Char): Boolean {
        arrPositiveBounds.forEach { range ->
            if (range.include(ch)) {
                return true
            }
        }
        return false
    }

    override fun toString(): String {
        val sb = StringBuilder()
        var comma = false
        for (range in arrPositiveBounds) {
            if (comma)
                sb.append(MetaType.COMMA.char)
            sb.append(range)
            if (!comma)
                comma = true
        }
        return sb.toString()
    }
}
