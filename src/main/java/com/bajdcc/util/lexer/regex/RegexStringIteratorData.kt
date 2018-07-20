package com.bajdcc.util.lexer.regex

import com.bajdcc.util.lexer.token.MetaType

/**
 * 分析时使用的数据
 * [index] 当前处理的位置
 * [current] 字符
 * [zero] 允许空字符
 * [meta] 字符类型
 */
data class RegexStringIteratorData(var index: Int = 0,
                                   var current: Char = 0.toChar(),
                                   var zero: Boolean = false,
                                   var meta: MetaType = MetaType.END) : Cloneable {

    @Throws(CloneNotSupportedException::class)
    public override fun clone(): RegexStringIteratorData {
        return super.clone() as RegexStringIteratorData
    }
}