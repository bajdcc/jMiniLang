package com.bajdcc.util.lexer.token

/**
 * 单词特性
 *
 * @author bajdcc
 */
object TokenUtility {

    /**
     * 是否为ASCII英文字母
     * [ch] 字符
     */
    fun isLetter(ch: Char): Boolean {
        return isUpperLetter(ch) || isLowerLetter(ch)
    }

    /**
     * 是否为ASCII大写英文字母
     * [ch] 字符
     */
    fun isUpperLetter(ch: Char): Boolean {
        return ch in 'A'..'Z'
    }

    /**
     * 是否为ASCII小写英文字母
     * [ch] 字符
     */
    fun isLowerLetter(ch: Char): Boolean {
        return ch in 'a'..'z'
    }
}
