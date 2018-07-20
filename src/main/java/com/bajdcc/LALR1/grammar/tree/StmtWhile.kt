package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.CodegenBlock
import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.util.lexer.token.KeywordType

/**
 * 【语义分析】循环语句
 *
 * @author bajdcc
 */
class StmtWhile : IStmt {

    /**
     * 条件
     */
    var cond: IExp? = null

    /**
     * 块
     */
    var block: Block? = null

    override fun analysis(recorder: ISemanticRecorder) {
        cond!!.analysis(recorder)
        block!!.analysis(recorder)
    }

    override fun genCode(codegen: ICodegen) {
        val cb = CodegenBlock()
        val start = codegen.genCode(RuntimeInst.ijmp, -1)
        cb.breakId = codegen.codeIndex
        val breakJmp = codegen.genCode(RuntimeInst.ijmp, -1)
        cb.continueId = codegen.codeIndex
        val continueJmp = codegen.genCode(RuntimeInst.ijmp, -1)
        start.op1 = codegen.codeIndex
        cond!!.genCode(codegen)
        codegen.genCode(RuntimeInst.ijf, cb.breakId)
        codegen.blockService.enterBlockEntry(cb)
        block!!.genCode(codegen)
        codegen.blockService.leaveBlockEntry()
        continueJmp.op1 = codegen.codeIndex
        codegen.genCode(RuntimeInst.ijmp, start.op1)
        breakJmp.op1 = codegen.codeIndex
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        return prefix.toString() +
                KeywordType.WHILE.desc +
                " ( " +
                cond!!.print(prefix) +
                " ) " +
                block!!.print(prefix)
    }

    override fun addClosure(scope: IClosureScope) {
        cond!!.addClosure(scope)
        block!!.addClosure(scope)
    }
}