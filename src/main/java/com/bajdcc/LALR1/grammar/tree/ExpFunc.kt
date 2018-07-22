package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.ClosureScope
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.LALR1.grammar.type.TokenTools

/**
 * 【语义分析】函数定义表达式
 *
 * @author bajdcc
 */
class ExpFunc : ClosureScope(), IExp {

    /**
     * 调用函数
     */
    var func: Function? = null

    /**
     * 闭包
     */
    var closure: Set<Any>? = null

    override fun isConstant(): Boolean {
        return false
    }

    override fun isEnumerable(): Boolean {
        return func!!.isEnumerable()
    }

    fun genClosure() {
        func!!.addClosure(this)
        closure = closures
    }

    override fun simplify(recorder: ISemanticRecorder): IExp {
        return this
    }

    override fun analysis(recorder: ISemanticRecorder) {
        func!!.analysis(recorder)
    }

    override fun genCode(codegen: ICodegen) {
        if (closure == null) {
            codegen.genCode(RuntimeInst.ipushz)
        } else {
            closure!!.forEach { obj ->
                codegen.genCode(RuntimeInst.ipush, codegen.genDataRef(obj))
                if (obj is String && TokenTools.isExternalName(obj.toString()))
                    codegen.genCode(RuntimeInst.ipush, -1) // iloadx
            }
            codegen.genCode(RuntimeInst.ipush, closure!!.size)
        }
        codegen.genCodeWithFuncWriteBack(RuntimeInst.ipush, codegen.getFuncIndex(func!!))
        codegen.genCode(RuntimeInst.ildfun)
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        return func!!.print(prefix)
    }

    override fun addClosure(scope: IClosureScope) {

    }

    override fun setYield() {

    }
}
