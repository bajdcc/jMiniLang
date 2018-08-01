package com.bajdcc.util.lexer.regex

import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.error.RegexException.RegexError

/**
 * 解析组件集合
 * [iterator] 迭代接口
 * @author bajdcc
 */
class RegexStringUtility(val iterator: IRegexStringIterator) {

    /**
     * 处理转义字符
     *
     * @param ch    字符
     * @param error 产生的错误
     * @return 处理后的字符
     * @throws RegexException 正则表达式错误
     */
    @Throws(RegexException::class)
    fun fromEscape(ch: Char, error: RegexError): Char = when (ch) {
        'r' -> '\r'
        'n' -> '\n'
        't' -> '\t'
        'b' -> '\b'
        'f' -> '\u000c'
        'v' -> '\u0002'
        'x' -> fromDigit(16, 2, error)
        'o' -> fromDigit(8, 3, error)
        'u' -> fromDigit(16, 4, error)
        in '0'..'9' -> {
            val c = (ch - '0').toChar()
            if (c.toInt() == 0) '\uffff' else c
        }
        else -> ch
    }

    /**
     * 处理数字
     *
     * @param base  基数
     * @param count 长度
     * @param error 产生的错误
     * @return 处理后的字符
     * @throws RegexException 正则表达式错误
     */
    @Throws(RegexException::class)
    fun fromDigit(base: Int, count: Int, error: RegexError): Char {
        var cnt = count
        var chv: Int
        var v = 0
        try {
            while (cnt != 0) {
                chv = Integer.valueOf(iterator.current() + "", base)
                --cnt
                v *= base
                v += chv
                iterator.next()
            }
        } catch (e: NumberFormatException) {
            iterator.err(error)
        }
        return v.toChar()
    }
}
