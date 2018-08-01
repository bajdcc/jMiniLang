package com.bajdcc.LALR1.grammar.runtime

/**
 * 【中间代码】指令结构基类
 *
 * @author bajdcc
 */
abstract class RuntimeInstBase {

    var inst = RuntimeInst.inop

    abstract val advanceLength: Int

    open fun toString(delim: String): String {
        return inst.toString()
    }

    override fun toString(): String {
        return toString(" ")
    }

    abstract fun gen(writer: ICodegenByteWriter)
}
