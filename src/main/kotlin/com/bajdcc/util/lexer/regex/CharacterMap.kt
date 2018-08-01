package com.bajdcc.util.lexer.regex

import java.util.*

/**
 * 字符集合，将字符范围按状态分组（Sigma集合）
 *
 * @author bajdcc
 * @see CharacterRange
 */
class CharacterMap : IRegexComponentVisitor {

    private val unicodeMapSize = 0x10000

    /**
     * 遍历的结点的深度
     */
    private var level = 0

    /**
     * 重新分组后的范围集合
     */
    val ranges = mutableListOf<CharacterRange>()

    /**
     * 面向字符（Unicode）的状态映射表，大小65536
     */
    val status = IntArray(unicodeMapSize)

    /**
     * 排序方法
     */
    private val comparator = Comparator.comparing(CharacterRange::lowerBound).thenComparing(CharacterRange::upperBound)

    override fun visitBegin(node: Charset) {
        increaseLevel()
        if (node.bReverse) {
            preceedReverse(node)// 处理取反集合
        }
        addRanges(node)// 将状态集合分解重构
    }

    override fun visitBegin(node: Constructure) {
        increaseLevel()
    }

    override fun visitBegin(node: Repetition) {
        increaseLevel()
    }

    override fun visitEnd(node: Charset) {
        decreaseLevel()
    }

    override fun visitEnd(node: Constructure) {
        decreaseLevel()
    }

    override fun visitEnd(node: Repetition) {
        decreaseLevel()
    }

    /**
     * 查找指定字符所在的区间范围序号
     *
     * @param ch 字符
     * @return 序号，-1代表不存在
     */
    fun find(ch: Char): Int {
        return ranges.indices.firstOrNull { ranges[it].include(ch) } ?: -1
    }

    /**
     * 化简、消去字符集中的取反属性
     *
     * @param charset 字符集
     */
    private fun preceedReverse(charset: Charset) {
        charset.bReverse = false
        charset.arrPositiveBounds.sortWith(comparator)
        val ranges = mutableListOf<CharacterRange>()
        var oldRange = CharacterRange()
        charset.arrPositiveBounds.forEach { range ->
            if (range.lowerBound.toInt() > oldRange.upperBound.toInt() + 1) {// 当前下界大于之前上界，故添加
                val midRange = CharacterRange(
                        (oldRange.upperBound.toInt() + 1).toChar(),
                        (range.lowerBound.toInt() - 1).toChar())// 添加范围，从之前上界到当前下界
                ranges.add(midRange)
                oldRange = range
            }
        }
        if (oldRange.upperBound.toInt() < unicodeMapSize - 1) {
            val midRange = CharacterRange(
                    (oldRange.lowerBound.toInt() + 1).toChar(),
                    (unicodeMapSize - 1).toChar())
            ranges.add(midRange)// 添加最后的范围
        }
        charset.arrPositiveBounds = ranges
        charset.arrPositiveBounds.sortWith(comparator)
    }

    /**
     * 深度加一
     */
    private fun increaseLevel() {
        level++
    }

    /**
     * 深度减一
     */
    private fun decreaseLevel() {
        level--
        if (level == 0) {// 遍历到根结点
            putStatus()
        }
    }

    /**
     * 添加所有状态
     */
    private fun putStatus() {
        (0 until unicodeMapSize).forEach { i ->
            status[i] = -1// 所有元素置为无效状态-1
        }
        ranges.indices.forEach { i ->
            val lower = ranges[i].lowerBound.toInt()
            val upper = ranges[i].upperBound.toInt()
            (lower..upper).forEach { j ->
                status[j] = i// 将范围i中包括的所有元素置为i
            }
        }
    }

    /**
     * 处理新添加的字符范围，必要时将其分解，使得元素间相互独立
     *
     * @param newRange 字符区间
     */
    private fun addRange(newRange: CharacterRange) {
        var i = 0
        while (i < ranges.size) {
            ranges.sortWith(comparator)
            val oldRange = ranges[i]
            /*
			 * 防止新增区间[New]与之前区间[Old]产生交集，若有，则将集合分裂
			 */
            if (oldRange.lowerBound < newRange.lowerBound) {
                if (oldRange.upperBound < newRange.lowerBound) {

                    // [####Old####]_______________
                    // ______________[#####New####]
                    // [Old]比[New]小，没有交集

                } else if (oldRange.upperBound < newRange.upperBound) {

                    // [######Old######]__________
                    // ______________[#####New####]
                    // [Old]与[New]有交集[New.Lower,Old.Upper]
                    // [Old]=[Old.Lower,New.Lower-1]
                    // [Mid]=[New.Lower,Old.Upper]
                    // [New]=[Old.Upper+1,New.Upper]

                    ranges.removeAt(i)
                    newRange.lowerBound = (oldRange.upperBound.toInt() + 1).toChar()
                    oldRange.upperBound = (newRange.lowerBound.toInt() - 1).toChar()
                    ranges.add(oldRange)
                    ranges.add(CharacterRange(
                            (oldRange.upperBound.toInt() + 1).toChar(),
                            (newRange.lowerBound.toInt() - 1).toChar()))
                    i++
                } else if (oldRange.upperBound == newRange.upperBound) {

                    // [###########Old############]
                    // ______________[#####New####]
                    // [Old]与[New]有交集[New]
                    // [Old]=[Old.Lower,New.Lower-1]
                    // [New]=[New]

                    ranges.removeAt(i)
                    oldRange.upperBound = (newRange.lowerBound.toInt() - 1).toChar()
                    ranges.add(newRange)
                    ranges.add(oldRange)
                    return
                } else {

                    // [#############Old##############]
                    // ______________[#####New####]____
                    // [Old]与[New]有交集[New]
                    // [Left]=[Old.Lower,New.Lower-1]
                    // [Mid]=[New]
                    // [Right]=[New.Upper+1]

                    ranges.removeAt(i)
                    ranges.add(CharacterRange(oldRange.lowerBound,
                            (newRange.upperBound.toInt() - 1).toChar()))
                    ranges.add(newRange)
                    ranges.add(CharacterRange(
                            (newRange.lowerBound.toInt() + 1).toChar(),
                            oldRange.upperBound))
                    return
                }
            } else if (oldRange.lowerBound == newRange.lowerBound) {
                if (oldRange.upperBound < newRange.upperBound) {

                    // [#######Old#######]
                    // [##########New##########]
                    // [Old]与[New]有交集[Old]

                    newRange.lowerBound = (oldRange.upperBound.toInt() + 1).toChar()
                } else if (oldRange.upperBound == newRange.upperBound) {

                    // [#######Old#######]
                    // [#######New#######]
                    // [Old]=[New]

                    return
                } else {

                    // [##########Old##########]
                    // [#######New#######]
                    // [Old]与[New]有交集[New]
                    // [Old]=[New.Upper+1,Old.Upper]
                    // [New]=[New]

                    ranges.removeAt(i)
                    oldRange.lowerBound = (newRange.upperBound.toInt() + 1).toChar()
                    ranges.add(newRange)
                    ranges.add(oldRange)
                    return
                }
            } else if (oldRange.lowerBound <= newRange.upperBound) {
                if (oldRange.upperBound < newRange.upperBound) {

                    // ___[#######Old#######]___
                    // [##########New##########]
                    // [Old]与[New]有交集[Old]
                    // [Left]=[New.Lower,Old.Lower-1]
                    // [New]=[Old.Upper+1,New.Upper]

                    ranges.add(CharacterRange(newRange.lowerBound,
                            (oldRange.lowerBound.toInt() - 1).toChar()))
                    newRange.lowerBound = (oldRange.upperBound.toInt() + 1).toChar()
                    i++
                } else if (oldRange.upperBound == newRange.upperBound) {

                    // ______[#######Old#######]
                    // [##########New##########]
                    // [Old]与[New]有交集[Old]
                    // [Old]=[Old]
                    // [New]=[New.Lower,Old.Lower-1]

                    newRange.upperBound = (oldRange.lowerBound.toInt() - 1).toChar()
                    ranges.add(newRange)
                    return
                } else {

                    // ______[##########Old##########]
                    // [##########New##########]______
                    // [Old]与[New]有交集[Old.Lower,New.Upper]
                    // [Old]=[New.Upper+1,Old.Upper]
                    // [Mid]=[Old.Lower,New.Upper]
                    // [New]=[New.Lower,Old.Lower-1]

                    ranges.removeAt(i)
                    newRange.upperBound = (oldRange.lowerBound.toInt() - 1).toChar()
                    oldRange.lowerBound = (newRange.upperBound.toInt() + 1).toChar()
                    ranges.add(oldRange)
                    ranges.add(CharacterRange(oldRange.lowerBound,
                            newRange.upperBound))
                    ranges.add(newRange)
                    return
                }
            }
            i++
        }
        ranges.add(newRange)
    }

    /**
     * 处理新添加的字符范围，必要时将其分解，使得元素间相互独立
     *
     * @param charset 字符集
     */
    private fun addRanges(charset: Charset) {
        charset.arrPositiveBounds.forEach { range ->
            addRange(CharacterRange(range.lowerBound, range.upperBound))
        }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        ranges.forEach { range ->
            sb.append(range).append(System.lineSeparator())
        }
        return sb.toString()
    }
}
