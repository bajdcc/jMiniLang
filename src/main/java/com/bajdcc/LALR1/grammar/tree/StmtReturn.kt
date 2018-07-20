package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.util.lexer.token.KeywordType
import com.bajdcc.util.lexer.token.OperatorType

/**
 * 【语义分析】返回语句
 *
 * @author bajdcc
 */
class StmtReturn : IStmt {

    var exp: IExp? = null

    var isYield = false

    override fun analysis(recorder: ISemanticRecorder) {
        exp?.analysis(recorder)
    }

    override fun genCode(codegen: ICodegen) {
        if (!isYield) {
            if (exp != null) {
                exp!!.genCode(codegen)
            } else {
                codegen.genCode(RuntimeInst.ipushx)
            }
            codegen.genCode(RuntimeInst.iret)
        } else {
            if (exp != null) {
                exp!!.genCode(codegen)
                codegen.genCode(RuntimeInst.iyldi)
                codegen.genCode(RuntimeInst.iyldl)
            } else {
                codegen.genCode(RuntimeInst.ipushn)
                codegen.genCode(RuntimeInst.iyldi)
                codegen.genCode(RuntimeInst.iyldl)
            }
        }
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        val sb = StringBuilder()
        sb.append(prefix.toString())
        if (isYield) {
            sb.append(KeywordType.YIELD.desc)
            sb.append(" ")
        }
        when {
            exp != null -> {
                sb.append(KeywordType.RETURN.desc)
                sb.append(" ")
                sb.append(exp!!.print(prefix))
            }
            isYield -> sb.append(KeywordType.BREAK.desc)
            else -> sb.append(KeywordType.RETURN.desc)
        }
        sb.append(OperatorType.SEMI.desc)
        return sb.toString()
    }

    override fun addClosure(scope: IClosureScope) {
        exp?.addClosure(scope)
    }
}
