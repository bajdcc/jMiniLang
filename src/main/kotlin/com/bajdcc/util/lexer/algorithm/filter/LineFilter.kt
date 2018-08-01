package com.bajdcc.util.lexer.algorithm.filter

import com.bajdcc.util.lexer.regex.IRegexStringFilter
import com.bajdcc.util.lexer.regex.IRegexStringFilterMeta
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.regex.RegexStringIteratorData
import com.bajdcc.util.lexer.token.MetaType

/**
 * 转义换行过滤
 *
 * @author bajdcc
 */
class LineFilter : IRegexStringFilter, IRegexStringFilterMeta {

    override val filterMeta: IRegexStringFilterMeta
        get() = this

    override val metaTypes: Array<MetaType>
        get() = arrayOf(MetaType.ESCAPE, MetaType.NEW_LINE, MetaType.CARRIAGE_RETURN)

    override fun filter(iterator: IRegexStringIterator): RegexStringIteratorData {
        val data = RegexStringIteratorData()
        if (!iterator.available()) {
            data.meta = MetaType.END
            data.current = MetaType.END.char
        } else {
            data.meta = iterator.meta()
            data.current = iterator.current()
            iterator.next()
            if (data.meta === MetaType.ESCAPE) {// 过滤转义换行
                iterator.next()
                iterator.snapshot()
                data.meta = iterator.meta()
                if (data.meta === MetaType.NEW_LINE || data.meta === MetaType.CARRIAGE_RETURN) {// 确认换行
                    iterator.next()
                    iterator.cover()
                    data.meta = iterator.meta()
                    if (data.meta === MetaType.NEW_LINE || data.meta === MetaType.CARRIAGE_RETURN) {// 确认换行
                        iterator.discard()
                        iterator.next()
                    } else {
                        iterator.restore()
                    }
                    data.meta = MetaType.MUST_SAVE
                    data.current = iterator.current()
                    iterator.next()
                } else {
                    iterator.restore()
                }
            }
        }
        return data
    }
}
