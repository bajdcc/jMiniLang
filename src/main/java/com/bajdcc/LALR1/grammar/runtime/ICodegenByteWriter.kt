package com.bajdcc.LALR1.grammar.runtime

/**
 * 【中间代码生成】字节码生成
 *
 * @author bajdcc
 */
interface ICodegenByteWriter {

    fun genInst(inst: RuntimeInst)

    fun genOp(op: Int)
}
