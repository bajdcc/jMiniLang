package com.bajdcc.LALR1.grammar.runtime

import java.util.*

/**
 * 【运行时】运行时堆栈
 *
 * @author bajdcc
 */
class RuntimeStack {

    var parent = -1
    var level = 0

    internal var reg = RuntimeRegister()
    private var dataTryCounts = mutableListOf<Int>()
    private var catchState = false

    /**
     * 数据堆栈
     */
    private var stkData = Stack<RuntimeObject>()

    /**
     * 调用堆栈，临时变量堆栈
     */
    private var stkCall = mutableListOf<RuntimeFunc>()

    private val call: RuntimeFunc
        get() = stkCall[stkCall.size - 1]

    val isYield: Boolean
        get() = call.yields > 0

    val yield: Int
        get() = call.yields

    internal val isEmptyStack: Boolean
        get() = stkData.isEmpty()

    internal val funcArgsCount: Int
        get() = call.getParams().size

    internal val funcArgsCount1: Int
        get() = stkCall[stkCall.size - 2].getParams().size

    internal val funcLevel: Int
        get() = stkCall.size

    val funcName: String?
        get() = call.name

    internal val funcSimpleName: String
        get() = call.simpleName

    var trys: Int
        get() = call.tryJmp
        set(jmp) {
            if (jmp != -1)
                dataTryCounts.add(stkData.size)
            call.tryJmp = jmp
            catchState = false
        }

    constructor()

    constructor(level: Int) {
        this.level = level
    }

    fun addYield() {
        call.addYield()
    }

    internal fun popYield() {
        call.popYield()
    }

    internal fun resetYield() {
        call.resetYield()
    }

    @Throws(RuntimeException::class)
    internal fun pushData(obj: RuntimeObject?) {
        if (obj == null) {
            throw NullPointerException("obj")
        }
        stkData.push(obj)
        if (stkData.size > MAX_DATASTACKSIZE) {
            throw RuntimeException(RuntimeException.RuntimeError.THROWS_EXCEPTION, 0, "堆栈溢出")
        }
    }

    internal fun popData(): RuntimeObject {
        return stkData.pop()
    }

    fun top(): RuntimeObject {
        return stkData.peek()
    }

    internal fun findVariable(codePage: String, idx: Int): RuntimeObject {
        for (func in stkCall.reversed()) {
            if (func.currentPage == codePage) {
                val tmp = func.getTmp()
                var obj = tmp.map { it[idx] }.firstOrNull { it != null }
                if (obj != null) {
                    return obj
                }
                obj = func.getClosure()[idx]
                if (obj != null) {
                    return obj
                }
            }
        }
        return RuntimeObject(null)
    }

    internal fun storeVariableDirect(idx: Int, obj: RuntimeObject) {
        call.addTmp(idx, obj)
    }

    internal fun enterScope() {
        call.enterScope()
    }

    internal fun leaveScope() {
        call.leaveScope()
    }

    internal fun storeClosure(idx: Int, obj: RuntimeObject) {
        call.addClosure(idx, obj)
    }

    internal fun pushFuncData(): Boolean {
        stkCall.add(RuntimeFunc())
        return stkCall.size < MAX_CALLSTACKSIZE
    }

    internal fun pushFuncArgs(obj: RuntimeObject): Boolean {
        call.addParams(obj)
        return call.getParams().size < MAX_ARGSIZE
    }

    internal fun opReturn(reg: RuntimeRegister) {
        reg.execId = call.retPc
        reg.pageId = call.retPage
        stkCall.removeAt(stkCall.size - 1)
    }

    internal fun getFuncArgs(index: Int): RuntimeObject? {
        val func = stkCall.asReversed().drop(1).firstOrNull { it.name != null } ?: return null
        return func.getParams().getOrNull(index)
    }

    internal fun loadFuncArgs(idx: Int): RuntimeObject {
        return call.getParam(idx)
    }

    internal fun opCall(jmpPc: Int, jmpPage: String, retPc: Int, retPage: String,
                        funcName: String) {
        call.currentPc = jmpPc
        call.currentPage = jmpPage
        call.retPc = retPc
        call.retPage = retPage
        call.name = funcName
    }

    internal fun hasCatch(): Boolean {
        return catchState
    }

    internal fun hasNoTry(): Boolean {
        return dataTryCounts.isEmpty()
    }

    internal fun resetTry() {
        val last = dataTryCounts[dataTryCounts.size - 1]
        dataTryCounts.removeAt(dataTryCounts.size - 1)
        val obj = stkData.pop()
        while (stkData.size > last)
            stkData.pop()
        stkData.push(obj)
        catchState = true
    }

    override fun toString(): String {
        return "=========================" +
                System.lineSeparator() +
                "数据栈: " + stkData +
                System.lineSeparator() +
                "调用栈: " + stkCall +
                System.lineSeparator()
    }

    fun copy(): RuntimeStack {
        val stack = RuntimeStack()
        stack.reg = reg.copy()
        stack.catchState = catchState
        stack.stkData = Stack()
        stkData.forEach { stack.stkData.push(it) }
        stack.dataTryCounts = ArrayList(dataTryCounts)
        stack.stkCall = stkCall.map { it.copy() }.toMutableList()
        stack.parent = parent
        stack.level = level
        return stack
    }

    companion object {

        private const val MAX_DATASTACKSIZE = 100
        private const val MAX_CALLSTACKSIZE = 100
        private const val MAX_ARGSIZE = 16
    }
}
