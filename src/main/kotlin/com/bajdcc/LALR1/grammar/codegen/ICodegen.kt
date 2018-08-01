package com.bajdcc.LALR1.grammar.codegen

import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.runtime.RuntimeInstBinary
import com.bajdcc.LALR1.grammar.runtime.RuntimeInstNon
import com.bajdcc.LALR1.grammar.runtime.RuntimeInstUnary
import com.bajdcc.LALR1.grammar.tree.Func

/**
 * 【目标代码生成】接口
 *
 * @author bajdcc
 */
interface ICodegen {

    val codeIndex: Int

    val blockService: ICodegenBlock

    fun genFuncEntry(funcName: String)

    fun genCode(inst: RuntimeInst): RuntimeInstNon

    fun genCode(inst: RuntimeInst, op1: Int): RuntimeInstUnary

    fun genCodeWithFuncWriteBack(inst: RuntimeInst, op1: Int): RuntimeInstUnary

    fun genCode(inst: RuntimeInst, op1: Int, op2: Int): RuntimeInstBinary

    fun genDataRef(obj: Any): Int

    fun getFuncIndex(func: Func): Int

    fun genDebugInfo(start: Int, end: Int, info: Any)
}
