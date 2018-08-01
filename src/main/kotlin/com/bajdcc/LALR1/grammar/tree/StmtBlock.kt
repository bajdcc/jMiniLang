package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope

/**
 * 【语义分析】块语句
 *
 * @author bajdcc
 */
class StmtBlock : IStmt {

    var block: Block? = null

    override fun analysis(recorder: ISemanticRecorder) {
        block!!.analysis(recorder)
    }

    override fun genCode(codegen: ICodegen) {
        block!!.genCode(codegen)
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        return prefix.toString() + block!!.print(prefix)
    }

    override fun addClosure(scope: IClosureScope) {

    }
}