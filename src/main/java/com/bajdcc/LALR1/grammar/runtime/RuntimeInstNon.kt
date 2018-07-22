package com.bajdcc.LALR1.grammar.runtime

/**
 * 【中间代码】零元操作数指令
 *
 * @author bajdcc
 */
open class RuntimeInstNon(inst: RuntimeInst) : RuntimeInstBase() {

    override val advanceLength: Int
        get() = 1

    init {
        this.inst = inst
    }

    override fun gen(writer: ICodegenByteWriter) {
        writer.genInst(inst)
    }
}
