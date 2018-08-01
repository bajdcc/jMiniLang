package com.bajdcc.util.lexer.algorithm

import com.bajdcc.util.lexer.regex.IRegexStringFilter
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.token.MetaType
import com.bajdcc.util.lexer.token.Token

/**
 * 检测/读取单词的算法接口
 *
 * @author bajdcc
 */
interface ITokenAlgorithm {

    /**
     * 返回字符串过滤组件
     *
     * @return 字符串过滤组件
     */
    val regexStringFilter: IRegexStringFilter?

    /**
     * 返回字符类型哈希映射表
     *
     * @return 字符类型哈希映射表
     */
    val metaHash: Map<Char, MetaType>

    /**
     * 返回正则表达式描述
     *
     * @return 正则表达式描述
     */
    val regexDescription: String

    /**
     * 当前算法是否接受相应Visitor对象（即是否匹配）
     *
     * @param iterator 字符串迭代对象
     * @param token    返回的单词（可能出错，或者为EOL、EOF）
     * @return 算法匹配结果
     */
    fun accept(iterator: IRegexStringIterator, token: Token): Boolean

    /**
     * 返回正则表达式字符串
     *
     * @param string   匹配的字符串
     * @param token    输入的单词
     * @param iterator 迭代器
     * @return 输出的单词
     */
    fun getToken(string: String, token: Token, iterator: IRegexStringIterator): Token?
}
