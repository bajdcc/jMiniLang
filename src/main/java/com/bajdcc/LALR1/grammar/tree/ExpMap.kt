package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.util.lexer.token.OperatorType

/**
 * 【语义分析】字典
 *
 * @author bajdcc
 */
class ExpMap : IExp {

    /**
     * 参数
     */
    var params: MutableList<IExp> = mutableListOf()

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
        params.forEach { exp ->
            exp.analysis(recorder)
        }
    }

    override fun genCode(codegen: ICodegen) {
        params.forEach { exp ->
            exp.genCode(codegen)
        }
        codegen.genCode(RuntimeInst.imap, params.size)
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        val sb = StringBuilder()
        sb.append(OperatorType.LBRACE.desc)
        if (!params.isEmpty())
            sb.append(" ")
        params.forEach { exp ->
            sb.append(exp.print(sb))
            sb.append(", ")
        }
        if (!params.isEmpty()) {
            sb.deleteCharAt(sb.length - 1)
            sb.deleteCharAt(sb.length - 1)
        }
        if (!params.isEmpty())
            sb.append(" ")
        sb.append(OperatorType.RBRACE.desc)
        return sb.toString()
    }

    override fun addClosure(scope: IClosureScope) {
        params.forEach { exp ->
            exp.addClosure(scope)
        }
    }

    override fun setYield() {

    }
}
