package com.bajdcc.util.lexer.regex

import com.bajdcc.util.Position
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.error.RegexException.RegexError
import com.bajdcc.util.lexer.token.MetaType
import java.util.*

/**
 * 字符串迭代器，提供字节流解析功能
 *
 * @author bajdcc
 */
open class RegexStringIterator() : IRegexStringIterator, Cloneable {

    override fun regexDescription() = context

    /**
     * 存储字符串
     */
    open var context: String = ""

    /**
     * 用于恢复的位置堆栈
     */
    var stackIndex = Stack<Int>()

    /**
     * 位置
     */
    protected var position = Position()

    /**
     * 用于恢复行列数的堆栈
     */
    var stackPosition = Stack<Position>()

    /**
     * 当前的分析信息
     */
    protected var data = RegexStringIteratorData()

    /**
     * 记录每行起始的位置
     */
    protected var arrLinesNo = mutableListOf<Int>()

    /**
     * 字符解析组件
     */
    protected var utility = RegexStringUtility(this)

    init {
        arrLinesNo.add(-1)
    }

    constructor(context: String) : this() {
        this.context = context
    }

    @Throws(RegexException::class)
    override fun err(error: RegexError) {
        throw RegexException(error, position)
    }

    override fun next() {
        if (available()) {
            advance()
        }
        translate()
        position.column = position.column + 1
        if (data.current == MetaType.NEW_LINE.char) {
            val prev = arrLinesNo[arrLinesNo.size - 1]
            if (prev < data.index)
                arrLinesNo.add(data.index)
            position.column = 0
            position.line = position.line + 1
        }
    }

    override fun scan() {
        throw NotImplementedError()
    }

    override fun position(): Position {
        return position
    }

    override fun translate() {
        if (!available()) {
            data.current = '\u0000'
            data.meta = MetaType.END
            return
        }
        data.current = current()
        transform()
    }

    /**
     * 分析字符类型
     */
    protected open fun transform() {
        data.meta = MetaType.CHARACTER
    }

    override fun available(): Boolean {
        return data.index >= 0 && data.index < context.length
    }

    override fun advance() {
        data.index++
    }

    override fun current(): Char {
        return context[data.index]
    }

    override fun meta(): MetaType {
        return data.meta
    }

    override fun index(): Int {
        return data.index
    }

    @Throws(RegexException::class)
    override fun expect(meta: MetaType, error: RegexError) {
        if (data.meta === meta) {
            next()
        } else {
            err(error)
        }
    }

    override fun snapshot() {
        stackIndex.push(data.index)
        stackPosition.push(Position(position.column, position.line))
    }

    override fun cover() {
        stackIndex[stackIndex.size - 1] = data.index
        stackPosition[stackPosition.size - 1] = Position(position)
    }

    override fun restore() {
        data.index = stackIndex.pop()
        position = Position(stackPosition.pop())
    }

    override fun discard() {
        stackIndex.pop()
        stackPosition.pop()
    }

    override fun utility(): RegexStringUtility {
        return utility
    }

    override fun copy(): IRegexStringIterator {
        throw NotImplementedError()
    }

    override fun clone(): Any {
        val o = super.clone() as RegexStringIterator
        o.position = o.position.clone() as Position
        o.data = o.data.clone()
        o.utility = RegexStringUtility(o)
        return o
    }

    override fun ex(): IRegexStringIteratorEx {
        throw NotImplementedError()
    }
}