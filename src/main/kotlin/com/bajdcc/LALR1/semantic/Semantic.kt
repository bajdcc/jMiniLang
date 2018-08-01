package com.bajdcc.LALR1.semantic

import com.bajdcc.LALR1.grammar.semantic.ISemanticAnalyzer
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.symbol.IManageSymbol
import com.bajdcc.LALR1.grammar.symbol.IQuerySymbol
import com.bajdcc.LALR1.grammar.tree.Func
import com.bajdcc.LALR1.semantic.lexer.TokenFactory
import com.bajdcc.LALR1.semantic.tracker.Instruction
import com.bajdcc.LALR1.semantic.tracker.Tracker
import com.bajdcc.LALR1.semantic.tracker.TrackerError
import com.bajdcc.LALR1.semantic.tracker.TrackerResource
import com.bajdcc.LALR1.syntax.Syntax
import com.bajdcc.LALR1.syntax.automata.npa.NPAEdge
import com.bajdcc.LALR1.syntax.automata.npa.NPAEdgeType
import com.bajdcc.LALR1.syntax.handler.IErrorHandler
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.LALR1.syntax.handler.SyntaxException.SyntaxError
import com.bajdcc.LALR1.syntax.rule.RuleItem
import com.bajdcc.util.Position
import com.bajdcc.util.TrackerErrorBag
import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.regex.IRegexStringIterator
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 【语义分析】语义分析
 * [tokenFactory] 单词流工厂
 * @author bajdcc
 */
open class Semantic @Throws(RegexException::class)
constructor(context: String,
            protected var tokenFactory: TokenFactory = TokenFactory(context)) : Syntax(true), IErrorHandler {

    /**
     * 错误处理器
     */
    private var errorHandler: IErrorHandler = this

    /**
     * 存放生成的指令
     */
    private val insts = mutableListOf<Instruction>()

    /**
     * 存放生成过程中的错误
     */
    private val errors = mutableListOf<TrackerError>()

    /**
     * 单词流
     */
    private val tokensList = mutableListOf<Token>()

    /**
     * 当前的语义接口
     */
    private var semanticHandler: ISemanticAnalyzer? = null

    /**
     * 语义分析结果
     */
    private var obj: Any? = null

    /**
     * 跟踪器资源
     */
    private val trackerResource = TrackerResource()

    /**
     * 没有错误的跟踪器数量
     */
    private var iTrackerWithoutErrorCount = 0

    /**
     * 当前跟踪器
     */
    private var tracker: Tracker? = null

    /**
     * 是否打印调试信息
     */
    private val debug = false

    /**
     * 获取符号表查询接口
     *
     * @return 符号表查询接口
     */
    protected open val querySymbolService: IQuerySymbol?
        get() = null

    /**
     * 获取符号表管理接口
     *
     * @return 符号表管理接口
     */
    protected open val manageSymbolService: IManageSymbol?
        get() = null

    /**
     * 获取语义错误处理接口
     *
     * @return 语义错误处理接口
     */
    protected open val semanticRecorderService: ISemanticRecorder?
        get() = null

    /**
     * 获取跟踪器信息
     */
    private val trackerStatus: String
        get() {
            System.err.println("#### #### ####")
            System.err.println(tracker!!.iter!!.index())
            System.err.println(tracker!!.iter!!.position())
            System.err.println(tracker!!.npaStatus!!.data.label)
            val items = Syntax.npa!!.ruleItems
            val sb = StringBuilder()
            sb.append(System.lineSeparator())
            var rcd = tracker!!.insts
            while (rcd != null) {
                rcd.insts.forEach { inst ->
                    sb.append(inst.toString())
                    if (inst.handler != -1) {
                        val item = items[inst.handler]
                        sb.append("\t").append(Syntax.getSingleString(item.parent.nonTerminal.name,
                                item.expression))
                    }
                    sb.append(System.lineSeparator())
                }
                rcd = rcd.prev
                sb.append(System.lineSeparator())
            }
            return sb.toString()
        }

    /**
     * 获取单词流描述
     *
     * @return 单词流描述
     */
    val tokenList: String
        get() {
            val sb = StringBuilder()
            sb.append("#### 单词流 ####")
            sb.append(System.lineSeparator())
            tokensList.forEach { token ->
                sb.append(token.toString())
                sb.append(System.lineSeparator())
            }
            return sb.toString()
        }

    /**
     * 获得指令集描述
     *
     * @return 指令集描述
     */
    open val inst: String
        get() {
            val items = Syntax.npa!!.ruleItems
            val sb = StringBuilder()
            sb.append("#### 指令集 ####")
            sb.append(System.lineSeparator())
            for (inst in insts) {
                sb.append(inst.toString())
                if (inst.handler != -1) {
                    val item = items[inst.handler]
                    sb.append("\t\t").append(Syntax.getSingleString(item.parent.nonTerminal.name,
                            item.expression))
                }
                sb.append(System.lineSeparator())
            }
            return sb.toString()
        }

    /**
     * 获得语法错误描述
     *
     * @return 语法错误描述
     */
    val trackerError: String
        get() {
            val sb = StringBuilder()
            sb.append("#### 语法错误列表 ####")
            sb.append(System.lineSeparator())
            for (error in errors) {
                sb.append(error.toString())
                sb.append(System.lineSeparator())
            }
            return sb.toString()
        }

    init {
        // 用于分析的文本
        tokenFactory.discard(TokenType.COMMENT)
        tokenFactory.discard(TokenType.WHITESPACE)
        tokenFactory.discard(TokenType.ERROR)
        tokenFactory.discard(TokenType.MACRO)
        tokenFactory.scan()
    }

    /**
     * @param handler     语义分析接口
     * @param inferString 文法推导式
     * @throws SyntaxException 词法错误
     */
    @Throws(SyntaxException::class)
    fun infer(handler: ISemanticAnalyzer, inferString: String) {
        semanticHandler = handler
        super.infer(inferString)
        semanticHandler = null
    }

    override fun onAddRuleItem(item: RuleItem) {
        item.handler = semanticHandler
    }

    /**
     * 开始解析
     *
     * @throws SyntaxException 语法错误
     */
    @Throws(SyntaxException::class)
    fun parse() {
        analysis()
        run()
    }

    /**
     * 开始分析工作
     */
    private fun analysis() {
        /* 可用NPA边表 */
        val aliveEdgeList = mutableListOf<NPAEdge>()
        /* 结束边 */
        var finalEdge: NPAEdge?
        /* 初始PDA状态集合 */
        val initStatusList = Syntax.npa!!.initStatusList
        /* 清空结果 */
        insts.clear()
        errors.clear()
        /* 初始化跟踪器 */
        for (npaStatus in initStatusList) {
            val tracker = trackerResource.addTracker()
            tracker.insts = trackerResource.addInstRecord(null)
            tracker.errors = trackerResource.addErrorRecord(null)
            tracker.npaStatus = npaStatus
            tracker.iter = tokenFactory
            iTrackerWithoutErrorCount++
        }
        /* 是否分析成功 */
        var success = false
        /* 进行分析 */
        while (trackerResource.head != null && !success) {
            tracker = trackerResource.head
            while (tracker != null && !success) {
                val nextTracker = tracker!!.next
                if (debug) {
                    System.err.println(trackerStatus)
                }
                /* 对每一个跟踪器进行计算，构造level记录可用边优先级，可以防止冲突 */
                /* 匹配=0 移进=0 左递归=1 归约=2 */
                /* 对终结符则移进，对非终结符则匹配，互不冲突，故两者优先级相同 */
                /* 左递归属于特殊的归约，区别是归约后不出栈 */
                /* 注意：已除去二义性文法，跟踪器是不带回溯的 */
                var level = 2
                /* 筛选边 */
                aliveEdgeList.clear()
                finalEdge = null
                /* 遍历出边 */
                for (npaEdge in tracker!!.npaStatus!!.outEdges) {
                    var sublevel = -1
                    /* 若当前边使用了LA表，则输入不满足LA表时会被丢弃 */
                    if (!npaEdge.data.lookaheads.isEmpty()) {
                        if (!npaEdge.data.lookaheads.contains(getTokenId(tracker!!))) {
                            continue
                        }
                    }
                    if (npaEdge.data.type == NPAEdgeType.FINISH) {
                        /* 检查状态堆栈是否为空 */
                        if (tracker!!.stkStatus.isEmpty()) {
                            finalEdge = npaEdge
                        }
                        continue
                    } else if (npaEdge.data.type == NPAEdgeType.LEFT_RECURSION) sublevel = 1
                    else if (npaEdge.data.type == NPAEdgeType.MOVE
                    /* 检查Move所需要的记号跟输入是否一致（匹配） */) {
                        if (npaEdge.data.token != getTokenId(tracker!!)) {
                            continue// 失败
                        } else {
                            sublevel = 0
                        }
                    } else if (npaEdge.data.type == NPAEdgeType.REDUCE
                    /* 检查Reduce所需要的栈状态跟状态堆栈的栈顶元素是否一致 */) {
                        if (tracker!!.stkStatus.isEmpty() ||
                                npaEdge.data.status != tracker!!.stkStatus.peek()) {
                            continue// 失败
                        } else {
                            sublevel = 2
                        }
                    } else if (npaEdge.data.type == NPAEdgeType.SHIFT) sublevel = 0
                    // 0为优先级最高
                    if (sublevel < level) {
                        aliveEdgeList.clear()
                        level = sublevel
                    }
                    if (sublevel == level) {
                        aliveEdgeList.add(npaEdge)// 添加可用边（可用解）
                    }
                }
                /* 检查是否有可用边 */
                if (!aliveEdgeList.isEmpty()) {
                    /* 如果当前跟踪器没发生过错误，则调整trackerWithoutErrorCount的数值 */
                    if (!tracker!!.raiseError) {
                        iTrackerWithoutErrorCount += aliveEdgeList.size - 1
                    }
                    /* 如果存在可供转移的边，则进行跳转，必要的时候复制跟踪器（用来回溯） */
                    var revI = aliveEdgeList.size
                    for ((_, end, data) in aliveEdgeList) {
                        revI--
                        val newTracker: Tracker
                        if (revI != 0)
                        // 未遍历到末尾
                        {
                            /* 复制新的跟踪器 */
                            newTracker = trackerResource.addTracker()
                            newTracker.raiseError = tracker!!.raiseError
                            newTracker.stkStatus.clear()
                            newTracker.stkStatus.addAll(tracker!!.stkStatus)
                            newTracker.npaStatus = tracker!!.npaStatus
                            newTracker.iter = tracker!!.iter!!.copy()
                            /* 复制错误记录集 */
                            if (!tracker!!.errors!!.errors.isEmpty()) {
                                newTracker.errors = trackerResource
                                        .addErrorRecord(tracker!!.errors!!)
                            } else {
                                newTracker.errors = trackerResource
                                        .addErrorRecord(tracker!!.errors!!.prev)
                            }
                            /* 复制指令记录集 */
                            if (!tracker!!.insts!!.insts.isEmpty()) {
                                newTracker.insts = trackerResource
                                        .addInstRecord(tracker!!.insts!!)
                            } else {
                                newTracker.insts = trackerResource
                                        .addInstRecord(tracker!!.insts!!.prev)
                            }
                        } else {
                            newTracker = tracker!!
                            /* 如果跟踪器需要分化，则在适当的时候改变自身的资源 */
                            if (aliveEdgeList.size > 1) {
                                if (!tracker!!.errors!!.errors.isEmpty()) {
                                    newTracker.insts = trackerResource
                                            .addInstRecord(tracker!!.insts!!)
                                }
                                if (!tracker!!.insts!!.insts.isEmpty()) {
                                    newTracker.insts = trackerResource
                                            .addInstRecord(tracker!!.insts!!)
                                }
                            }
                        }
                        when (data.type) {
                            NPAEdgeType.FINISH -> {
                            }
                            NPAEdgeType.LEFT_RECURSION -> {
                            }
                            NPAEdgeType.MOVE -> {
                                newTracker.iter!!.ex().saveToken()
                                newTracker.iter!!.scan()// 匹配，前进一步
                            }
                            NPAEdgeType.REDUCE -> newTracker.stkStatus.pop()// 栈顶弹出
                            NPAEdgeType.SHIFT -> newTracker.stkStatus.push(newTracker.npaStatus)// 移入新状态
                        }
                        newTracker.insts!!.insts.add(Instruction(
                                data.inst, data.index,
                                data.handler))// 添加指令和参数
                        newTracker.npaStatus = end// 通过这条边
                    }
                } else {// 无可用边
                    /* 检查当前输入时候否到结尾 */
                    if (!tracker!!.iter!!.ex().isEOF) {
                        /* 无可用边，且单词流未结束，则报错 */
                        if (!tracker!!.raiseError) {
                            /* 如果是第一次发生错误则调整状态 */
                            tracker!!.raiseError = true
                            iTrackerWithoutErrorCount--
                            /* 如果存在没有发生错误的跟踪器，则删除当前跟踪器 */
                            if (iTrackerWithoutErrorCount > 0) {
                                tracker!!.iter = null
                                trackerResource.freeTracker(tracker!!)
                                tracker = null
                            }
                        }
                        /* 不存在没有错误的跟踪器，此时判断当前跟踪器是否有继续分析的价值 */
                        if (tracker != null) {
                            validateTracker(false)
                        }
                    } else {// 单词流到达末尾，同时PDA没有可用边
                        tracker!!.finished = true// 跟踪器标记为结束
                        /* 如果记号已经读完，则判断是否走到了终结状态 */
                        if (finalEdge != null)
                        // 最终边
                        {
                            tracker!!.insts!!.insts.add(Instruction(
                                    finalEdge.data.inst, finalEdge.data.index,
                                    finalEdge.data.handler))
                            /* 判断是否分析成功 */
                            if (!tracker!!.raiseError) {
                                /* 记录一个分析结果 */
                                val instList = mutableListOf<Instruction>()
                                var instRecord = tracker!!.insts
                                /* 记录语法树指令 */
                                while (instRecord != null) {
                                    instList.addAll(0, instRecord.insts)
                                    instRecord = instRecord.prev
                                }
                                /* 保存结果 */
                                insts.addAll(instList)
                                tokensList.addAll(tracker!!.iter!!.ex().tokenList())
                                /* 删除当前跟踪器 */
                                tracker!!.iter = null
                                trackerResource.freeTracker(tracker!!)
                                tracker = null
                                /* 归约成功 */
                                success = true
                            }
                        } else {
                            validateTracker(true)
                        }
                    }
                }
                /* 取出下一个跟踪器 */
                tracker = nextTracker
            }
            if (iTrackerWithoutErrorCount == 0)
            // 没有不存在错误的跟踪器
            {
                /* 根据当前发生错误的情况剔除跟踪器 */

                /* 当前步骤没有错误的跟踪器数量 */
                var trackerWithoutErrorInStepCount = 0
                /* 当前步骤发生错误的跟踪器数量 */
                var trackerWithErrorInStepCount = 0
                /* 检查发生错误的跟踪器数量 */
                var tmpTracker = trackerResource.head
                while (tmpTracker != null) {
                    if (tmpTracker.inStepError) {
                        trackerWithErrorInStepCount++
                    } else {
                        trackerWithoutErrorInStepCount++
                    }
                    tmpTracker = tmpTracker.next
                }
                /* 如果同时存在发生错误和不发生错误的跟踪器，则剔除发生错误的跟踪器 */
                if (trackerWithoutErrorInStepCount > 0 && trackerWithErrorInStepCount > 0) {
                    tmpTracker = trackerResource.head
                    while (tmpTracker != null) {
                        if (tmpTracker.inStepError) {
                            trackerResource.freeTracker(tmpTracker)
                        }
                        tmpTracker = tmpTracker.next
                    }
                }
                /* 检查是否有到达终点的带有错误的跟踪器 */
                tmpTracker = trackerResource.head
                while (tmpTracker != null) {
                    tmpTracker.inStepError = false
                    if (tmpTracker.finished) {
                        /* 提交一份错误报告 */
                        var error = tmpTracker.errors
                        while (error != null) {
                            errors.addAll(error.errors)
                            error = error.prev
                        }
                        /* 终止分析，删除跟踪器 */
                        while (trackerResource.head != null) {
                            trackerResource.head!!.iter = null
                            trackerResource.freeTracker(trackerResource.head!!)
                        }
                        break
                    }
                    tmpTracker = tmpTracker.next
                }
            }
        }
    }

    /**
     * 分析跟踪器是否有继续分析的价值
     *
     * @param fatal 跟踪器是否遇到严重且不可恢复的错误
     */
    private fun validateTracker(fatal: Boolean) {
        /* 无可用边，且单词流未结束，则报错 */
        if (!tracker!!.raiseError) {
            /* 如果是第一次发生错误则调整状态 */
            tracker!!.raiseError = true
            iTrackerWithoutErrorCount--
            /* 如果存在没有发生错误的跟踪器，则删除当前跟踪器 */
            if (iTrackerWithoutErrorCount > 0) {
                tracker!!.iter = null
                trackerResource.freeTracker(tracker!!)
                tracker = null
            }
        }
        /* 判断该跟踪器是否有继续分析的价值 */
        if (tracker != null) {
            val position = Position(tracker!!.iter!!.position())
            val error = TrackerError(position)
            /* 寻找合适的错误处理器并处理 */
            var processed = findCorrectHandler(tracker!!, error, position)
            /* 若没有错误处理器接受这个错误，则调用缺省的错误处理器 */
            if (!processed && !fatal) {
                processed = handleError(tracker!!, error, position)
            }
            /* 如果没有错误处理程序或者错误处理程序放弃处理，则产生缺省的错误消息 */
            if (!processed) {
                if (!fatal) {
                    error.message = String.format("类型[%s]，状态[%s]", tracker!!.iter!!
                            .ex().token(), tracker!!.npaStatus!!.data.label)
                } else {
                    error.message = String.format("状态[%s]",
                            tracker!!.npaStatus!!.data.label)
                }
                tracker!!.iter!!.scan()// 跳过
            }
            /* 提交错误 */
            tracker!!.inStepError = true
            tracker!!.errors!!.errors.add(error)
        }
    }

    /**
     * 查找合适的错误处理器
     *
     * @param tracker  跟踪器
     * @param error    错误
     * @param position 错误位置
     * @return 是否找到
     */
    private fun findCorrectHandler(tracker: Tracker, error: TrackerError, position: Position): Boolean {
        /* 遍历当前状态的所有出边 */
        for ((_, end, data) in tracker.npaStatus!!.outEdges) {
            val handler = data.error
            /* 如果找到了一个错误处理器则进行处理 */
            if (handler != null) {
                val bag = TrackerErrorBag(position)
                error.message = handler.handle(tracker.iter!!, bag)
                /* 如果没有放弃则进行处理 */
                if (!bag.giveUp) {
                    if (bag.pass) {// 通过
                        if (data.errorJump != null) {// 有自定义错误处理器
                            tracker.npaStatus = data.errorJump
                        } else {
                            tracker.npaStatus = end// 通过
                        }
                    }
                    if (bag.read) {// 跳过当前记号
                        tracker.iter!!.scan()
                    }
                    if (bag.halt) {// 中止
                        tracker.finished = true
                    }
                    return true
                }
            }
        }
        return false
    }

    /**
     * 调用缺省的错误处理器处理错误
     *
     * @param tracker  跟踪器
     * @param error    错误
     * @param position 错误位置
     * @return 是否成功处理错误
     */
    private fun handleError(tracker: Tracker, error: TrackerError, position: Position): Boolean {
        val bag = TrackerErrorBag(position)
        error.message = errorHandler.handle(tracker.iter!!, bag)
        /* 如果没有放弃则进行处理 */
        if (!bag.giveUp) {
            if (bag.read) {// 跳过当前记号
                tracker.iter!!.scan()
            }
            if (bag.halt) {// 中止
                tracker.finished = true
            }
            return true
        }
        return false
    }

    /**
     * 得到当前终结符ID
     *
     * @param tracker 跟踪器
     * @return 终结符ID
     */
    private fun getTokenId(tracker: Tracker): Int {
        val token = tracker.iter!!.ex().token()
        for (exp in Syntax.arrTerminals) {
            if (exp.type === token.type && (exp.obj == null || exp.obj == token.obj)) {
                return exp.id
            }
        }
        return -1
    }

    /**
     * 进行语义处理
     */
    @Throws(SyntaxException::class)
    private fun run() {
        if (!errors.isEmpty()) {
            val error = trackerError
            System.err.println(error)
            throw SyntaxException(SyntaxError.COMPILE_ERROR,
                    errors[0].position, error)
        }
        /* 规则集合 */
        val items = Syntax.npa!!.ruleItems
        /* 符号表查询接口 */
        val query = querySymbolService
        /* 符号表管理接口 */
        val manage = manageSymbolService
        /* 语义错误处理接口 */
        val recorder = semanticRecorderService
        /* 复制单词流 */
        val tokens = tokensList.map { it.copy() }
        /* 运行时自动机 */
        val machine = SemanticMachine(items, Syntax.arrActions,
                tokens, query!!, manage!!, recorder!!, debug)
        /* 遍历所有指令 */
        insts.forEach { machine.run(it) }
        obj = machine.obj
        if (obj is Func) {
            val entry = obj as Func?
            manage.manageScopeService.registerFunc(entry!!)
        }
    }

    /**
     * 缺省的错误处理器
     *
     * @param iterator 迭代器
     * @param bag      参数包
     * @return 处理信息
     */
    override fun handle(iterator: IRegexStringIterator, bag: TrackerErrorBag): String {
        bag.halt = true
        bag.giveUp = false
        return "Error"
    }

    /**
     * 获取分析结果描述
     *
     * @return 分析结果描述
     */
    fun getObject(): String {
        val sb = StringBuilder()
        sb.append("#### 分析结果 ####")
        sb.append(System.lineSeparator())
        if (obj != null) {
            sb.append(obj!!.toString())
            sb.append(System.lineSeparator())
        }
        return sb.toString()
    }
}
