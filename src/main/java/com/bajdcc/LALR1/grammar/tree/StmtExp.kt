package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.util.lexer.token.OperatorType

/**
 * 【语义分析】表达式语句
 *
 * @author bajdcc
 */
class StmtExp : IStmt {

    var exp: IExp? = null

    override fun analysis(recorder: ISemanticRecorder) {
        exp?.analysis(recorder)
    }

    override fun genCode(codegen: ICodegen) {
        if (exp != null) {
            exp!!.genCode(codegen)
            codegen.genCode(RuntimeInst.ipop)
        }
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        val sb = StringBuilder()
        sb.append(prefix.toString())
        if (exp != null) {
            sb.append(exp!!.print(prefix))
        }
        sb.append(OperatorType.SEMI.desc)
        return sb.toString()
    }

    override fun addClosure(scope: IClosureScope) {
        exp?.addClosure(scope)
    }
}
