package com.bajdcc.LALR1.grammar.runtime

/**
 * 【中间代码】一元操作符
 *
 * @author bajdcc
 */
open class RuntimeInstUnary(inst: RuntimeInst, var op1: Int) : RuntimeInstNon(inst) {

    override val advanceLength: Int
        get() = 4 + super.advanceLength

    override fun gen(writer: ICodegenByteWriter) {
        super.gen(writer)
        writer.genOp(op1)
    }

    override fun toString(delim: String): String {
        return super.toString(delim) + delim + op1
    }

    override fun toString(): String {
        return toString(" ")
    }
}
