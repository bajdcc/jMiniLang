package com.bajdcc.util.lexer.regex

import com.bajdcc.util.lexer.token.MetaType

/**
 * 单词类型解析接口
 *
 * @author bajdcc
 */
interface IRegexStringFilterMeta {

    /**
     * 返回单词类型数组
     *
     * @return 单词类型数组
     */
    val metaTypes: Array<MetaType>
}
