package com.bajdcc.util.lexer.algorithm

import com.bajdcc.util.lexer.error.IErrorHandler
import com.bajdcc.util.lexer.regex.IRegexStringIterator

/**
 * 错误处理器基类
 * [iterator] 迭代器接口
 * @author bajdcc
 */
abstract class TokenErrorHandler(protected var iterator: IRegexStringIterator) : IErrorHandler
