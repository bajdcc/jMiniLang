package com.bajdcc.util.lexer.regex

/**
 * 字符范围
 *
 * @author bajdcc
 */
class CharacterRange {

    /**
     * 下限（包含）
     */
    var lowerBound: Char = 0.toChar()

    /**
     * 上限（包含）
     */
    var upperBound: Char = 0.toChar()

    constructor()

    constructor(lower: Char, upper: Char) {
        lowerBound = lower
        upperBound = upper
    }

    /**
     * 当前区间是否包含字符
     *
     * @param ch 字符
     * @return 比较结果
     */
    fun include(ch: Char): Boolean {
        return ch in lowerBound..upperBound
    }

    override fun toString(): String {
        val sb = StringBuilder()
        if (lowerBound == upperBound) {
            sb.append(printChar(lowerBound))
        } else {
            sb.append(printChar(lowerBound)).append("-").append(printChar(upperBound))
        }
        return sb.toString()
    }

    private fun printChar(ch: Char): String {
        return if (Character.isISOControl(ch)) {
            String.format("[\\u%04x]", ch.toInt())
        } else {
            String.format("[\\u%04x,'%c']", ch.toInt(), ch)
        }
    }
}