package com.bajdcc.util.lexer.regex

/**
 * 字符串过滤接口
 *
 * @author bajdcc
 */
interface IRegexStringFilter {


    /**
     * 返回类型过滤接口
     *
     * @return 类型过滤接口
     */
    val filterMeta: IRegexStringFilterMeta

    /**
     * 过滤
     *
     * @param iterator 迭代器
     * @return 过滤结果
     */
    fun filter(iterator: IRegexStringIterator): RegexStringIteratorData
}
