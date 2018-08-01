package com.bajdcc.util.lexer.algorithm

import com.bajdcc.util.lexer.error.IErrorHandler
import com.bajdcc.util.lexer.regex.IRegexStringFilterHost
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 用于抽取单词的算法集合（包含数字、字符串等）
 * [iterator] 字符串迭代器
 * [filterHost] 字符转换主体
 * @author bajdcc
 */
class TokenAlgorithmCollection(var iterator: IRegexStringIterator,
                               var filterHost: IRegexStringFilterHost?) : Cloneable {
    /**
     * 算法集合
     */
    private val arrAlgorithms = mutableListOf<ITokenAlgorithm>()

    /**
     * 错误处理
     */
    private var handler: IErrorHandler = TokenErrorAdvanceHandler(iterator)

    /**
     * 添加解析组件
     *
     * @param alg 解析组件
     */
    fun attach(alg: ITokenAlgorithm) {
        arrAlgorithms.add(alg)
    }

    /**
     * 删除解析组件
     *
     * @param alg 解析组件
     */
    fun detach(alg: ITokenAlgorithm) {
        arrAlgorithms.remove(alg)
    }

    /**
     * 清空解析组件
     */
    fun clear() {
        arrAlgorithms.clear()
    }

    fun scan(): Token {
        val token = Token()
        token.type = TokenType.ERROR
        if (!iterator.available()) {
            token.type = TokenType.EOF
        } else {
            for (alg in arrAlgorithms) {
                filterHost!!.setFilter(alg)
                iterator.translate()
                if (alg.accept(iterator, token))
                    return token
            }
            handler.handleError()
        }
        return token
    }

    /**
     * 拷贝构造
     *
     * @param iter   迭代器
     * @param filter 过滤器
     * @return 拷贝
     * @throws CloneNotSupportedException 不支持拷贝
     */
    @Throws(CloneNotSupportedException::class)
    fun copy(iter: IRegexStringIterator,
             filter: IRegexStringFilterHost): TokenAlgorithmCollection {
        val o = super.clone() as TokenAlgorithmCollection
        o.iterator = iter
        o.filterHost = filter
        o.handler = TokenErrorAdvanceHandler(iter)
        return o
    }
}
