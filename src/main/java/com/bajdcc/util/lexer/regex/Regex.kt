package com.bajdcc.util.lexer.regex

import com.bajdcc.util.lexer.automata.dfa.DFA
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.error.RegexException.RegexError
import com.bajdcc.util.lexer.stringify.RegexToString
import com.bajdcc.util.lexer.token.MetaType
import com.bajdcc.util.lexer.token.TokenUtility

/**
 * 【词法分析】## 正则表达式分析工具 ##<br></br>
 * 用于生成语法树<br></br>
 * 语法同一般的正则表达式，只有贪婪模式，没有前/后向匹配， 没有捕获功能，仅用于匹配。
 *
 * @author bajdcc
 */
class Regex @Throws(RegexException::class)
@JvmOverloads constructor(pattern: String, val debug: Boolean = false) : RegexStringIterator(pattern) {

    /**
     * 表达式树根结点
     */
    private lateinit var expression: IRegexComponent

    /**
     * DFA
     */
    private lateinit var dfa: DFA

    /**
     * DFA状态转换表
     */
    private lateinit var transition: Array<IntArray>

    /**
     * 终态表
     */
    private val setFinalStatus = mutableSetOf<Int>()

    /**
     * 字符区间表
     */
    private lateinit var charMap: CharacterMap

    /**
     * 字符串过滤接口
     */
    var filter: IRegexStringFilter? = null

    /**
     * 获取字符区间描述
     *
     * @return 字符区间描述
     */
    val statusString: String
        get() = dfa.statusString

    /**
     * 获取NFA描述
     *
     * @return NFA描述
     */
    val nfaString: String
        get() = dfa.nfaString

    /**
     * 获取DFA描述
     *
     * @return DFA描述
     */
    val dfaString: String
        get() = dfa.dfaString

    /**
     * 获取DFATable描述
     *
     * @return DFATable描述
     */
    val dfaTableString: String
        get() = dfa.dfaTableString

    init {
        compile()
    }

    /**
     * ## 编译表达式 ##<br></br>
     *
     * @throws RegexException 正则表达式错误
     */
    @Throws(RegexException::class)
    private fun compile() {
        translate()
        /* String->AST */
        expression = analysis(MetaType.END.char, MetaType.END)
        if (debug) {
            println("#### 正则表达式语法树 ####")
            println(toString())
        }
        /* AST->ENFA->NFA->DFA */
        dfa = DFA(expression, debug)
        /* DFA Transfer Table */
        buildTransition()
    }

    /**
     * 建立DFA状态转换表
     */
    private fun buildTransition() {
        /* 字符区间映射表 */
        charMap = dfa.characterMap
        /* DFA状态转移表 */
        transition = dfa.buildTransition(setFinalStatus)
    }

    /**
     * 匹配
     *
     * @param string 被匹配的字符串
     * @param greed  是否贪婪匹配
     * @return 匹配结果（若不成功则返回空）
     */
    @JvmOverloads
    fun match(string: String, greed: Boolean = true): String? {
        var matchString: String? = null
        val attr = object : IRegexStringAttribute {
            override var result: String = ""
            override val greedMode: Boolean
                get() = greed
        }
        if (match(RegexStringIterator(string), attr)) {
            matchString = attr.result
        }
        return matchString
    }

    /**
     * 匹配算法（DFA状态表）
     *
     * @param iterator 字符串遍历接口
     * @param attr     输出的匹配字符串
     * @return 是否匹配成功
     */
    fun match(iterator: IRegexStringIterator,
              attr: IRegexStringAttribute): Boolean {
        /* 使用全局字符映射表 */
        val charMap = charMap.status
        /* 保存当前位置 */
        iterator.snapshot()
        /* 当前状态 */
        var status = 0
        /* 上次经过的终态 */
        var lastFinalStatus = -1
        /* 上次经过的终态位置 */
        var lastIndex = -1
        /* 是否为贪婪模式 */
        val greed = attr.greedMode
        /* 存放匹配字符串 */
        val sb = StringBuilder()
        /* 是否允许通过终态结束识别 */
        var allowFinal = false
        while (true) {
            if (setFinalStatus.contains(status)) {// 经过终态
                if (greed) {// 贪婪模式
                    if (lastFinalStatus == -1) {
                        iterator.snapshot()// 保存位置
                    } else {
                        iterator.cover()// 覆盖位置
                    }
                    lastFinalStatus = status// 记录上次状态
                    lastIndex = sb.length
                } else if (!allowFinal) {// 非贪婪模式，则匹配完成
                    iterator.discard()// 匹配成功，丢弃位置
                    attr.result = sb.toString()
                    return true
                }
            }
            val local: Char
            var skipStore = false// 取消存储当前字符
            /* 获得当前字符 */
            if (filter != null) {
                val (_, current, _, meta) = filter!!.filter(iterator)// 过滤
                local = current
                skipStore = meta === MetaType.NULL
                allowFinal = meta === MetaType.MUST_SAVE// 强制跳过终态
            } else {
                if (!iterator.available()) {
                    local = 0.toChar()
                } else {
                    local = iterator.current()
                    iterator.next()
                }
            }
            /* 存储字符 */
            if (!skipStore) {
                sb.append(if (data.zero) '\u0000' else local)
            }
            /* 获得字符区间索引 */
            val charClass = charMap[local.toInt()]
            /* 状态转移 */
            var refer = -1
            if (charClass != -1) {// 区间有效，尝试转移
                refer = transition[status][charClass]
            }
            if (refer == -1) {// 失败
                iterator.restore()
                if (lastFinalStatus == -1) {// 匹配失败
                    return false
                } else {// 使用上次经过的终态匹配结果
                    iterator.discard()// 匹配成功，丢弃位置
                    attr.result = sb.substring(0, lastIndex)
                    return true
                }
            } else {
                status = refer// 更新状态
            }
        }
    }

    @Throws(RegexException::class)
    private fun analysis(terminal: Char, meta: MetaType): IRegexComponent {
        var sequence = Constructure(false)// 建立序列以存储表达式
        var branch: Constructure? = null// 建立分支以存储'|'型表达式，是否是分支有待预测
        var result = sequence

        while (true) {
            if (data.meta === meta && data.current == terminal) {// 结束字符
                if (data.index == 0) {// 表达式为空
                    err(RegexError.NULL)
                } else if (sequence.arrComponents.isEmpty()) {// 部件为空
                    err(RegexError.INCOMPLETE)
                } else {
                    next()
                    break// 正常终止
                }
            } else if (data.meta === MetaType.END) {
                err(RegexError.INCOMPLETE)
            }
            var expression: IRegexComponent? = null// 当前待赋值的表达式
            if (data.meta == MetaType.BAR) {// '|'
                next()
                if (sequence.arrComponents.isEmpty())
                // 在此之前没有存储表达式 (|...)
                {
                    err(RegexError.INCOMPLETE)
                } else {
                    if (branch == null) {// 分支为空，则建立分支
                        branch = Constructure(true)
                        branch.arrComponents.add(sequence)// 用新建的分支包含并替代当前序列
                        result = branch
                    }
                    sequence = Constructure(false)// 新建一个序列
                    branch.arrComponents.add(sequence)
                    continue
                }
            } else if (data.meta == MetaType.LPARAN) {// '('
                next()
                expression = analysis(MetaType.RPARAN.char,
                        MetaType.RPARAN)// 递归分析
            }

            if (expression == null) {// 当前不是表达式，则作为字符
                val charset = Charset()// 当前待分析的字符集
                expression = charset
                when (data.meta) {
                    MetaType.ESCAPE// '\\'
                    -> {
                        next()
                        escape(charset, true)// 处理转义
                    }
                    MetaType.DOT// '.'
                    -> {
                        data.meta = MetaType.CHARACTER
                        escape(charset, true)
                    }
                    MetaType.LSQUARE // '['
                    -> {
                        next()
                        range(charset)
                    }
                    MetaType.END // '\0'
                    -> return result
                    else -> {
                        if (!charset.addChar(data.current)) {
                            err(RegexError.RANGE)
                        }
                        next()
                    }
                }
            }

            val rep: Repetition// 循环
            when (data.meta) {
                MetaType.QUERY// '?'
                -> {
                    next()
                    rep = Repetition(expression, 0, 1)
                    sequence.arrComponents.add(rep)
                }
                MetaType.PLUS// '+'
                -> {
                    next()
                    rep = Repetition(expression, 1, -1)
                    sequence.arrComponents.add(rep)
                }
                MetaType.STAR// '*'
                -> {
                    next()
                    rep = Repetition(expression, 0, -1)
                    sequence.arrComponents.add(rep)
                }
                MetaType.LBRACE // '{'
                -> {
                    next()
                    rep = Repetition(expression, 0, -1)
                    quantity(rep)
                    sequence.arrComponents.add(rep)
                }
                else -> sequence.arrComponents.add(expression)
            }
        }

        return result
    }

    /**
     * 处理转义字符
     *
     * @param charset 字符集
     * @param extend  是否支持扩展如\d \w等
     * @throws RegexException 正则表达式错误
     */
    @Throws(RegexException::class)
    private fun escape(charset: Charset, extend: Boolean) {
        var ch = data.current
        if (data.meta === MetaType.CHARACTER) {// 字符
            next()
            if (extend) {
                if (TokenUtility.isUpperLetter(ch) || ch == '.') {
                    charset.bReverse = true// 大写则取反
                }
                val cl = Character.toLowerCase(ch)
                when (cl) {
                    'd'// 数字
                    -> {
                        charset.addRange('0', '9')
                        return
                    }
                    'a'// 字母
                    -> {
                        charset.addRange('a', 'z')
                        charset.addRange('A', 'Z')
                        return
                    }
                    'w'// 标识符
                    -> {
                        charset.addRange('a', 'z')
                        charset.addRange('A', 'Z')
                        charset.addRange('0', '9')
                        charset.addChar('_')
                        return
                    }
                    's'// 空白字符
                    -> {
                        charset.addChar('\r')
                        charset.addChar('\n')
                        charset.addChar('\t')
                        charset.addChar('\b')
                        charset.addChar('\u000c')
                        charset.addChar(' ')
                        return
                    }
                    else -> {
                    }
                }
            }
            if (TokenUtility.isLetter(ch)) {// 如果为字母
                ch = utility.fromEscape(ch, RegexError.ESCAPE)
                if (!charset.addChar(ch)) {
                    err(RegexError.RANGE)
                }
            }
        } else if (data.meta === MetaType.END) {
            err(RegexError.INCOMPLETE)
        } else {// 功能字符则转义
            next()
            if (!charset.addChar(ch)) {
                err(RegexError.RANGE)
            }
        }
    }

    /**
     * 处理字符集合
     *
     * @param charset 字符集
     * @throws RegexException 正则表达式错误
     */
    @Throws(RegexException::class)
    private fun range(charset: Charset) {
        if (data.meta === MetaType.CARET) {// '^'取反
            next()
            charset.bReverse = true
        }
        while (data.meta !== MetaType.RSQUARE) {// ']'
            if (data.meta === MetaType.CHARACTER) {
                character(charset)
                val lower = data.current // lower bound
                next()
                if (data.meta === MetaType.DASH) {// '-'
                    next()
                    character(charset)
                    val upper = data.current // upper bound
                    next()
                    if (lower > upper) {// check bound
                        err(RegexError.RANGE)
                    }
                    if (!charset.addRange(lower, upper)) {
                        err(RegexError.RANGE)
                    }
                } else {
                    if (!charset.addChar(lower)) {
                        err(RegexError.RANGE)
                    }
                }
            } else if (data.meta === MetaType.ESCAPE) {
                next()
                escape(charset, false)
            } else if (data.meta === MetaType.END) {
                err(RegexError.INCOMPLETE)
            } else {
                charset.addChar(data.current)
                next()
            }
        }
        next()
    }

    /**
     * 处理字符
     *
     * @param charset 字符集
     * @throws RegexException 正则表达式错误
     */
    @Throws(RegexException::class)
    private fun character(charset: Charset) {
        if (data.meta === MetaType.ESCAPE) {// '\\'
            next()
            escape(charset, false)
        } else if (data.meta === MetaType.END) {// '\0'
            err(RegexError.INCOMPLETE)
        } else if (data.meta !== MetaType.CHARACTER && data.meta !== MetaType.DASH) {
            err(RegexError.CTYPE)
        }
    }

    /**
     * 处理量词
     *
     * @throws RegexException 正则表达式错误
     */
    @Throws(RegexException::class)
    private fun quantity(rep: Repetition) {
        val lower: Int
        var upper: Int
        upper = digit()
        lower = upper// 循环下界
        if (lower == -1) {
            err(RegexError.BRACE)
        }
        if (data.meta === MetaType.COMMA) {// ','
            next()
            if (data.meta === MetaType.RBRACE) {// '}'
                upper = -1// 上界为无穷大
            } else {
                upper = digit()// 得到循环上界
                if (upper == -1) {
                    err(RegexError.BRACE)
                }
            }
        }
        if (upper != -1 && upper < lower) {
            err(RegexError.RANGE)
        }
        expect(MetaType.RBRACE, RegexError.BRACE)
        rep.lowerBound = lower
        rep.upperBound = upper
    }

    /**
     * 十进制数字转换
     *
     * @return 数字
     */
    private fun digit(): Int {
        val index = data.index
        while (Character.isDigit(data.current)) {
            next()
        }
        return try {
            Integer.valueOf(context.substring(index, data.index), 10)
        } catch (e: NumberFormatException) {
            -1
        }
    }

    override fun transform() {
        // 一般字符
        data.meta = g_mapMeta.getOrDefault(data.current, MetaType.CHARACTER)// 功能字符
    }

    override fun toString(): String {
        val alg = RegexToString()// 表达式树序列化算法初始化
        expression.visit(alg)// 遍历树
        return alg.toString()
    }

    companion object {
        private val g_mapMeta = mutableMapOf<Char, MetaType>()

        init {
            val metaTypes = arrayOf(MetaType.LPARAN, MetaType.RPARAN,
                    MetaType.STAR, MetaType.PLUS, MetaType.QUERY,
                    MetaType.CARET, MetaType.LSQUARE, MetaType.RSQUARE,
                    MetaType.BAR, MetaType.ESCAPE, MetaType.DASH,
                    MetaType.LBRACE, MetaType.RBRACE, MetaType.COMMA,
                    MetaType.DOT, MetaType.NEW_LINE, MetaType.CARRIAGE_RETURN,
                    MetaType.BACKSPACE)
            for (meta in metaTypes) {
                g_mapMeta[meta.char] = meta
            }
        }
    }
}
