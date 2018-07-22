package com.bajdcc.LALR1.grammar.runtime

/**
 * 【中间代码】二元操作符
 *
 * @author bajdcc
 */
class RuntimeInstBinary(inst: RuntimeInst, op1: Int, var op2: Int) : RuntimeInstUnary(inst, op1) {

    override val advanceLength: Int
        get() = 4 + super.advanceLength

    override fun gen(writer: ICodegenByteWriter) {
        super.gen(writer)
        writer.genOp(op2)
    }

    override fun toString(delim: String): String {
        return super.toString(delim) + delim + op2
    }

    override fun toString(): String {
        return toString(" ")
    }
}
