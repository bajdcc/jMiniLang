package com.bajdcc.util.lexer.regex

import com.bajdcc.util.lexer.algorithm.ITokenAlgorithm

/**
 * 字符串过滤主体
 *
 * @author bajdcc
 */
interface IRegexStringFilterHost {
    /**
     * 设置字符转换算法
     *
     * @param alg 字符转换算法
     */
    fun setFilter(alg: ITokenAlgorithm)
}
