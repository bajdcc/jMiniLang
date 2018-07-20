package com.bajdcc.util.lexer.regex

import com.bajdcc.util.Position
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.error.RegexException.RegexError
import com.bajdcc.util.lexer.token.MetaType
import com.bajdcc.util.lexer.token.Token

/**
 * 字符串迭代器接口
 *
 * @author bajdcc
 */
interface IRegexStringIterator {

    /**
     * 获得正则表达式描述
     *
     * @return 正则表达式描述
     */
    fun regexDescription(): String

    /**
     * 抛出错误
     *
     * @param error 错误类型
     * @throws RegexException 正则表达式错误
     */
    @Throws(RegexException::class)
    fun err(error: RegexError)

    /**
     * 处理下一个字符
     */
    operator fun next()

    /**
     * 处理下一个字符（会丢弃字符直到获得合法字符）
     *
     * @return 处理的字符
     */
    fun scan(): Token

    /**
     * 翻译当前字符
     */
    fun translate()

    /**
     * 判断当前位置不是末尾
     *
     * @return 流可用
     */
    fun available(): Boolean

    /**
     * 前进一个字符（look forward）
     */
    fun advance()

    /**
     * 获得当前字符
     *
     * @return 当前字符
     */
    fun current(): Char

    /**
     * 获得字符类型
     *
     * @return 字符类型
     */
    fun meta(): MetaType

    /**
     * 获得当前位置
     *
     * @return 当前位置
     */
    fun index(): Int

    /**
     * 获得当前位置
     *
     * @return 当前位置
     */
    fun position(): Position

    /**
     * 确认当前字符
     *
     * @param meta  类型
     * @param error 抛出的错误
     * @throws RegexException 正则表达式错误
     */
    @Throws(RegexException::class)
    fun expect(meta: MetaType, error: RegexError)

    /**
     * 保存当前位置
     */
    fun snapshot()

    /**
     * 覆盖当前位置
     */
    fun cover()

    /**
     * 恢复至上次位置
     */
    fun restore()

    /**
     * 丢弃上次位置
     */
    fun discard()

    /**
     * 获得解析组件
     *
     * @return 解析组件
     */
    fun utility(): RegexStringUtility

    /**
     * 复制一个对象
     *
     * @return 返回拷贝
     */
    fun copy(): IRegexStringIterator

    /**
     * 获取扩展接口
     *
     * @return 返回扩展接口
     */
    fun ex(): IRegexStringIteratorEx
}