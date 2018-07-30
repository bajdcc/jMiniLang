package com.bajdcc.OP.syntax.precedence

import com.bajdcc.util.lexer.token.Token

/**
 * 混合数据结构
 *
 * @author bajdcc
 */
class FixedData {

    /**
     * 单词
     */
    var token: Token? = null

    /**
     * 数据
     */
    var obj: Any? = null

    constructor()

    constructor(token: Token) {
        this.token = token
    }

    constructor(obj: Any?) {
        this.obj = obj
    }

    override fun toString(): String {
        return if (token != null) {
            token!!.toString()
        } else if (obj != null) {
            obj!!.toString()
        } else {
            "(null)"
        }
    }
}
