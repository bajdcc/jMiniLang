package com.bajdcc.LALR1.semantic.tracker

import com.bajdcc.LALR1.syntax.automata.npa.NPAStatus
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import java.util.*

/**
 * 跟踪器（链表）
 *
 * @author bajdcc
 */
class Tracker {
    /**
     * 指令记录集
     */
    var insts: InstructionRecord? = null

    /**
     * 错误记录集
     */
    var errors: ErrorRecord? = null

    /**
     * 当前PDA状态
     */
    var npaStatus: NPAStatus? = null
    /**
     * PDA状态堆栈
     */
    var stkStatus = Stack<NPAStatus>()

    /**
     * 单词遍历接口
     */
    var iter: IRegexStringIterator? = null

    /**
     * 是否产生了错误
     */
    var raiseError = false

    /**
     * 当前步骤是否产生了错误
     */
    var inStepError = false

    /**
     * 是否已经中止
     */
    var finished = false

    /**
     * 前向指针
     */
    var prev: Tracker? = null

    /**
     * 后向指针
     */
    var next: Tracker? = null
}
