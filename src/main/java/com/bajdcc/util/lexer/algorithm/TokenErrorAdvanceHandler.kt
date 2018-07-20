package com.bajdcc.util.lexer.algorithm

import com.bajdcc.util.lexer.regex.IRegexStringIterator

/**
 * 向前进一步的错误处理器
 *
 * @author bajdcc
 */
class TokenErrorAdvanceHandler(iterator: IRegexStringIterator) : TokenErrorHandler(iterator) {

    override fun handleError() {
        iterator.advance()
    }
}