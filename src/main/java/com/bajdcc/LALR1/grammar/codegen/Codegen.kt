package com.bajdcc.LALR1.grammar.codegen

import com.bajdcc.LALR1.grammar.runtime.*
import com.bajdcc.LALR1.grammar.symbol.SymbolTable
import com.bajdcc.LALR1.grammar.tree.Func
import com.bajdcc.util.HashListMap
import com.bajdcc.util.intervalTree.Interval

/**
 * 【中间代码】中间代码接口实现
 *
 * @author bajdcc
 */
class Codegen(symbol: SymbolTable) : ICodegen, ICodegenBlock, ICodegenByteWriter {

    private val symbolList = symbol.manageDataService.symbolList
    private val funcMap = HashListMap<Func>()
    private val data = CodegenData()
    private val info = RuntimeDebugInfo()
    private val insts = mutableListOf<Byte>()
    private val itvList = mutableListOf<Interval<Any>>()

    val codeString: String
        get() {
            val sb = StringBuilder()
            sb.append("#### 中间代码 ####")
            sb.append(System.lineSeparator())
            var idx = 0
            data.insts.forEach {
                sb.append(String.format("%03d\t%s", idx, it.toString("\t")))
                idx += it.advanceLength
                sb.append(System.lineSeparator())
            }
            return sb.toString()
        }

    init {
        for (funcs in symbol.manageDataService.funcMap.toList()) {
            for (func in funcs) {
                funcMap.add(func)
            }
        }
    }

    override fun getFuncIndex(func: Func): Int {
        return funcMap.indexOf(func)
    }

    /**
     * 产生中间代码
     */
    fun gencode() {
        genCode(RuntimeInst.iopena)
        genCodeWithFuncWriteBack(RuntimeInst.ipush, 0)
        genCode(RuntimeInst.icall)
        genCode(RuntimeInst.ipop)
        genCode(RuntimeInst.ihalt)
        genCode(RuntimeInst.inop)
        funcMap.forEach { func ->
            if (func.isExtern) {
                info.addExports(func.realName, data.codeIndex)
                info.addExternalFunc(func.realName,
                        RuntimeDebugExec(paramsDoc = func.params.joinToString(separator = "，", transform = { it.toRealString() })))
            }
            func.genCode(this)
        }
    }

    /**
     * 产生代码页
     *
     * @return 代码页
     */
    fun genCodePage(): RuntimeCodePage {
        val objs = symbolList.toList()
        data.callsToWriteBack.forEach { unary ->
            unary.op1 = data.funcEntriesMap[funcMap.get(unary.op1).refName]!!
        }
        for (inst in data.insts) {
            inst.gen(this)
        }
        return RuntimeCodePage(objs, insts.toByteArray(), info, itvList)
    }

    override fun genFuncEntry(funcName: String) {
        data.pushFuncEntry(funcName)
        info.addFunc(funcName, data.codeIndex)
    }

    override fun genCode(inst: RuntimeInst): RuntimeInstNon {
        val i = RuntimeInstNon(inst)
        data.pushCode(i)
        return i
    }

    override fun genCode(inst: RuntimeInst, op1: Int): RuntimeInstUnary {
        val i = RuntimeInstUnary(inst, op1)
        data.pushCode(i)
        return i
    }

    override fun genCodeWithFuncWriteBack(inst: RuntimeInst, op1: Int): RuntimeInstUnary {
        val i = RuntimeInstUnary(inst, op1)
        data.pushCodeWithFuncWriteBack(i)
        return i
    }

    override fun genCode(inst: RuntimeInst, op1: Int, op2: Int): RuntimeInstBinary {
        val i = RuntimeInstBinary(inst, op1, op2)
        data.pushCode(i)
        return i
    }

    override fun genDebugInfo(start: Int, end: Int, info: Any) {
        itvList.add(Interval((start - 1).toLong(), (end + 1).toLong(), info))
    }

    override val blockService: ICodegenBlock
        get() {
            return this
        }

    override fun genBreak(): RuntimeInstUnary {
        val block = data.block
        val i = RuntimeInstUnary(RuntimeInst.ijmp,
                block.breakId)
        data.pushCode(i)
        return i
    }

    override fun genContinue(): RuntimeInstUnary {
        val block = data.block
        val i = RuntimeInstUnary(RuntimeInst.ijmp,
                block.continueId)
        data.pushCode(i)
        return i
    }

    override fun enterBlockEntry(block: CodegenBlock) {
        data.enterBlockEntry(block)
    }

    override fun leaveBlockEntry() {
        data.leaveBlockEntry()
    }

    override val isInBlock: Boolean
        get() {
            return data.hasBlock()
        }

    override fun genDataRef(obj: Any): Int {
        return symbolList.put(obj)
    }

    override val codeIndex: Int
        get() {
            return data.codeIndex
        }

    override fun toString(): String {
        return codeString
    }

    override fun genInst(inst: RuntimeInst) {
        insts.add(inst.ordinal.toByte())
    }

    override fun genOp(op: Int) {
        for (i in 0..3) {
            insts.add((op shr 8 * i and 0xFF).toByte())
        }
    }
}
