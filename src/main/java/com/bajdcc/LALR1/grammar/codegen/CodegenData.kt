package com.bajdcc.LALR1.grammar.codegen

import com.bajdcc.LALR1.grammar.runtime.RuntimeInstBase
import com.bajdcc.LALR1.grammar.runtime.RuntimeInstUnary

import java.util.*

/**
 * 【中间代码】数据结构
 *
 * @author bajdcc
 */
class CodegenData {

    var insts = mutableListOf<RuntimeInstBase>()
    var funcEntriesMap = mutableMapOf<String, Int>()
    var callsToWriteBack = mutableListOf<RuntimeInstUnary>()
    private val stkBlock = Stack<CodegenBlock>()
    var codeIndex = 0
        private set

    val block: CodegenBlock
        get() = stkBlock.peek()

    fun pushCode(code: RuntimeInstBase) {
        insts.add(code)
        codeIndex += code.advanceLength
    }

    fun pushCodeWithFuncWriteBack(code: RuntimeInstBase) {
        checkWriteBackInst(code)
        pushCode(code)
    }

    private fun checkWriteBackInst(code: RuntimeInstBase) {
        callsToWriteBack.add(code as RuntimeInstUnary)
    }

    fun pushFuncEntry(name: String) {
        funcEntriesMap[name] = codeIndex
    }

    fun enterBlockEntry(block: CodegenBlock) {
        stkBlock.push(block)
    }

    fun leaveBlockEntry() {
        stkBlock.pop()
    }

    fun hasBlock(): Boolean {
        return !stkBlock.isEmpty()
    }
}
