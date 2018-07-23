package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.util.lexer.token.OperatorType

/**
 * 【语义分析】数组
 *
 * @author bajdcc
 */
class ExpArray : IExp {

    /**
     * 参数
     */
    private var params: MutableList<IExp> = mutableListOf()

    fun getParams(): List<IExp> {
        return params
    }

    fun setParams(params: MutableList<IExp>) {
        this.params = params
    }

    override fun isConstant(): Boolean {
        return params.all { it.isConstant() }
    }

    override fun isEnumerable(): Boolean {
        return false
    }

    override fun simplify(recorder: ISemanticRecorder): IExp {
        for (i in params.indices) {
            params[i] = params[i].simplify(recorder)
        }
        return this
    }

    override fun analysis(recorder: ISemanticRecorder) {
        for (exp in params) exp.analysis(recorder)
    }

    override fun genCode(codegen: ICodegen) {
        for (i in params.indices.reversed()) {
            params[i].genCode(codegen)
        }
        codegen.genCode(RuntimeInst.iarr, params.size)
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        val sb = StringBuilder()
        sb.append(OperatorType.LSQUARE.desc)
        if (!params.isEmpty())
            sb.append(" ")
        params.forEach { exp ->
            sb.append(exp.print(prefix))
            sb.append(", ")
        }
        if (!params.isEmpty()) {
            sb.deleteCharAt(sb.length - 1)
            sb.deleteCharAt(sb.length - 1)
        }
        if (!params.isEmpty())
            sb.append(" ")
        sb.append(OperatorType.RSQUARE.desc)
        return sb.toString()
    }

    override fun addClosure(scope: IClosureScope) {
        for (exp in params) exp.addClosure(scope)
    }

    override fun setYield() {

    }
}
