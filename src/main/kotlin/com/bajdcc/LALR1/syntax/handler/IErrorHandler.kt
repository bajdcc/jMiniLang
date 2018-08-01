package com.bajdcc.LALR1.syntax.handler

import com.bajdcc.util.TrackerErrorBag
import com.bajdcc.util.lexer.regex.IRegexStringIterator

/**
 * 语法错误处理接口
 *
 * @author bajdcc
 */
interface IErrorHandler {
    /**
     * 处理错误
     *
     * @param iterator 迭代器
     * @param bag      参数信息
     * @return 错误信息
     */
    fun handle(iterator: IRegexStringIterator, bag: TrackerErrorBag): String
}
