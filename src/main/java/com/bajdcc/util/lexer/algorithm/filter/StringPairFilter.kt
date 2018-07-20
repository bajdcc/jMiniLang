package com.bajdcc.util.lexer.algorithm.filter

import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.error.RegexException.RegexError
import com.bajdcc.util.lexer.regex.IRegexStringFilter
import com.bajdcc.util.lexer.regex.IRegexStringFilterMeta
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.regex.RegexStringIteratorData
import com.bajdcc.util.lexer.token.MetaType

/**
 * 字符串类型过滤（首尾字符不同）
 *
 * @author bajdcc
 */
class StringPairFilter(
        /**
         * 字符串首的终结符
         */
        private val kMetaBegin: MetaType,
        /**
         * 字符串尾的终结符
         */
        private val kMetaEnd: MetaType) : IRegexStringFilter, IRegexStringFilterMeta {

    override val filterMeta: IRegexStringFilterMeta
        get() = this

    override val metaTypes: Array<MetaType>
        get() = arrayOf(kMetaBegin, kMetaEnd, MetaType.ESCAPE)

    override fun filter(iterator: IRegexStringIterator): RegexStringIteratorData {
        val utility = iterator.utility()// 获取解析组件
        val data = RegexStringIteratorData()
        try {
            if (!iterator.available()) {
                data.meta = MetaType.END
                data.current = MetaType.END.char
            } else {
                data.meta = iterator.meta()
                data.current = iterator.current()
                iterator.next()
                if (data.meta === kMetaBegin || data.meta === kMetaEnd) {// 过滤终结符
                    data.meta = MetaType.NULL
                } else if (data.meta === MetaType.ESCAPE) {// 处理转义
                    data.current = iterator.current()
                    iterator.next()
                    data.meta = MetaType.MUST_SAVE
                    data.current = utility.fromEscape(data.current,
                            RegexError.ESCAPE)
                }
            }
        } catch (e: RegexException) {
            System.err.println(e.position.toString() + " : " + e.message)
            data.meta = MetaType.ERROR
            data.current = MetaType.ERROR.char
        }

        return data
    }
}
