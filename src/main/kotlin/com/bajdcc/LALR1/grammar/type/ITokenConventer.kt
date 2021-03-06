package com.bajdcc.LALR1.grammar.type

import com.bajdcc.util.lexer.token.Token

/**
 * 【类型转换】单词类型转换接口
 *
 * @author bajdcc
 */
interface ITokenConventer {

    /**
     * 类型转换
     *
     * @param token 操作数
     * @return 结果
     */
    fun convert(token: Token): Token
}
