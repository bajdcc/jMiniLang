package com.bajdcc.LALR1.grammar.error

import com.bajdcc.LALR1.syntax.handler.IErrorHandler
import com.bajdcc.util.TrackerErrorBag
import com.bajdcc.util.lexer.regex.IRegexStringIterator

/**
 * 【语法分析】应对丢失符号的错误处理器
 *
 * @author bajdcc
 */
class LostHandler(private val message: String) : IErrorHandler {

    override fun handle(iterator: IRegexStringIterator, bag: TrackerErrorBag): String {
        bag.read = false
        bag.pass = true
        val def = "缺少：" + message + System.lineSeparator()
        val snapshot = iterator.ex().getErrorSnapshot(bag.position)
        return if (!snapshot.isEmpty()) {
            def + snapshot
        } else def
    }
}
