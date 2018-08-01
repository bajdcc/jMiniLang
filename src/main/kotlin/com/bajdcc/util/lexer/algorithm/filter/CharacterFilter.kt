package com.bajdcc.util.lexer.algorithm.filter

import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.error.RegexException.RegexError
import com.bajdcc.util.lexer.regex.IRegexStringFilter
import com.bajdcc.util.lexer.regex.IRegexStringFilterMeta
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.regex.RegexStringIteratorData
import com.bajdcc.util.lexer.token.MetaType

/**
 * 字符类型过滤
 *
 * @author bajdcc
 */
class CharacterFilter : IRegexStringFilter, IRegexStringFilterMeta {

    override val filterMeta: IRegexStringFilterMeta
        get() = this

    override val metaTypes: Array<MetaType>
        get() = arrayOf(MetaType.SINGLE_QUOTE, MetaType.ESCAPE)

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
                if (data.meta === MetaType.SINGLE_QUOTE) {// 过滤单引号
                    data.meta = MetaType.NULL
                } else if (data.meta === MetaType.ESCAPE) {// 处理转义
                    data.current = iterator.current()
                    iterator.next()
                    data.meta = MetaType.MUST_SAVE
                    if (data.current == '0')
                        data.zero = true
                    data.current = utility.fromEscape(data.current,
                            RegexError.ESCAPE)
                }
            }
        } catch (e: RegexException) {
            System.err.println(e.position.toString() + " : "
                    + e.message)
            data.meta = MetaType.ERROR
            data.current = MetaType.ERROR.char
        }

        return data
    }
}
