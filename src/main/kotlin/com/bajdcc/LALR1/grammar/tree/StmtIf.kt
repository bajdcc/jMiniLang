package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.util.lexer.token.KeywordType

/**
 * 【语义分析】条件语句
 *
 * @author bajdcc
 */
class StmtIf : IStmt {

    /**
     * 表达式
     */
    var exp: IExp? = null

    /**
     * 真块
     */
    var trueBlock: Block? = null

    /**
     * 假块
     */
    var falseBlock: Block? = null

    override fun analysis(recorder: ISemanticRecorder) {
        exp!!.analysis(recorder)
        trueBlock!!.analysis(recorder)
        falseBlock?.analysis(recorder)
    }

    override fun genCode(codegen: ICodegen) {
        exp!!.genCode(codegen)
        val jf = codegen.genCode(RuntimeInst.ijf, -1)
        trueBlock!!.genCode(codegen)
        if (falseBlock != null) {
            val jmp = codegen.genCode(RuntimeInst.ijmp, -1)
            jf.op1 = codegen.codeIndex
            falseBlock!!.genCode(codegen)
            jmp.op1 = codegen.codeIndex
        } else {
            jf.op1 = codegen.codeIndex
        }
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        val sb = StringBuilder()
        sb.append(prefix.toString())
        sb.append(KeywordType.IF.desc)
        sb.append(" (")
        sb.append(exp!!.print(prefix))
        sb.append(") ")
        sb.append(trueBlock!!.print(prefix))
        if (falseBlock != null) {
            sb.append(" ")
            sb.append(KeywordType.ELSE.desc)
            sb.append(" ")
            sb.append(falseBlock!!.print(prefix))
        }
        return sb.toString()
    }

    override fun addClosure(scope: IClosureScope) {
        exp!!.addClosure(scope)
        trueBlock!!.addClosure(scope)
        falseBlock?.addClosure(scope)
    }
}