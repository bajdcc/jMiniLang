package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.util.lexer.token.KeywordType
import com.bajdcc.util.lexer.token.OperatorType

/**
 * 【语义分析】异常处理语句
 *
 * @author bajdcc
 */
class StmtThrow : IStmt {

    /**
     * 异常表达式
     */
    var exp: IExp? = null

    override fun analysis(recorder: ISemanticRecorder) {
        exp!!.analysis(recorder)
    }

    override fun genCode(codegen: ICodegen) {
        exp!!.genCode(codegen)
        codegen.genCode(RuntimeInst.ithrow)
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        return prefix.toString() +
                KeywordType.THROW.desc +
                " " +
                exp!!.print(prefix) +
                OperatorType.SEMI.desc
    }

    override fun addClosure(scope: IClosureScope) {
        exp!!.addClosure(scope)
    }
}