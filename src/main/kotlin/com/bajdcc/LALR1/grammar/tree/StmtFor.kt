package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.CodegenBlock
import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.util.lexer.token.KeywordType
import com.bajdcc.util.lexer.token.OperatorType

/**
 * 【语义分析】循环语句
 *
 * @author bajdcc
 */
class StmtFor : IStmt {

    /**
     * 初始化
     */
    var variable: IExp? = null

    /**
     * 条件
     */
    var cond: IExp? = null

    /**
     * 控制
     */
    var ctrl: IExp? = null

    /**
     * 块
     */
    var block: Block? = null

    override fun analysis(recorder: ISemanticRecorder) {
        variable?.analysis(recorder)
        cond?.analysis(recorder)
        ctrl?.analysis(recorder)
        block!!.analysis(recorder)
    }

    override fun genCode(codegen: ICodegen) {
        if (variable != null) {
            variable!!.genCode(codegen)
            codegen.genCode(RuntimeInst.ipop)
        }
        val cb = CodegenBlock()
        val start = codegen.genCode(RuntimeInst.ijmp, -1)
        cb.breakId = codegen.codeIndex
        val breakJmp = codegen.genCode(RuntimeInst.ijmp, -1)
        cb.continueId = codegen.codeIndex
        val continueJmp = codegen.genCode(RuntimeInst.ijmp, -1)
        start.op1 = codegen.codeIndex
        if (cond != null) {
            cond!!.genCode(codegen)
            codegen.genCode(RuntimeInst.ijf, cb.breakId)
        }
        codegen.blockService.enterBlockEntry(cb)
        block!!.genCode(codegen)
        codegen.blockService.leaveBlockEntry()
        continueJmp.op1 = codegen.codeIndex
        if (ctrl != null) {
            ctrl!!.genCode(codegen)
            codegen.genCode(RuntimeInst.ipop)
        }
        codegen.genCode(RuntimeInst.ijmp, start.op1)
        breakJmp.op1 = codegen.codeIndex
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        val sb = StringBuilder()
        sb.append(prefix.toString())
        sb.append(KeywordType.FOR.desc)
        sb.append(" ( ")
        if (variable != null) {
            sb.append(variable!!.print(prefix))
        }
        sb.append(OperatorType.SEMI.desc)
        if (cond != null) {
            sb.append(" ")
            sb.append(cond!!.print(prefix))
        }
        sb.append(OperatorType.SEMI.desc)
        if (ctrl != null) {
            sb.append(" ")
            sb.append(ctrl!!.print(prefix))
        }
        sb.append(" ) ")
        sb.append(block!!.print(prefix))
        return sb.toString()
    }

    override fun addClosure(scope: IClosureScope) {
        variable?.addClosure(scope)
        cond?.addClosure(scope)
        ctrl?.addClosure(scope)
        block!!.addClosure(scope)
    }
}