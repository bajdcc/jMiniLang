package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.util.lexer.token.KeywordType
import com.bajdcc.util.lexer.token.Token

/**
 * 【语义分析】异常处理语句
 *
 * @author bajdcc
 */
class StmtTry : IStmt {

    /**
     * 异常名
     */
    var token: Token? = null

    /**
     * try块
     */
    var tryBlock: Block? = null

    /**
     * catch块
     */
    var catchBlock: Block? = null


    override fun analysis(recorder: ISemanticRecorder) {
        tryBlock!!.analysis(recorder)
        catchBlock!!.analysis(recorder)
    }

    override fun genCode(codegen: ICodegen) {
        val t = codegen.genCode(RuntimeInst.itry, -1)
        tryBlock!!.genCode(codegen)
        val jmp = codegen.genCode(RuntimeInst.ijmp, -1)
        t.op1 = codegen.codeIndex
        codegen.genCode(RuntimeInst.iscpi)
        if (token != null) {
            // 'throw' push exp to stack top
            codegen.genCode(RuntimeInst.ipush, codegen.genDataRef(token!!.obj!!))
            codegen.genCode(RuntimeInst.ialloc)
        }
        catchBlock!!.genCode(codegen)
        if (token != null) {
            codegen.genCode(RuntimeInst.ipop)
        }
        codegen.genCode(RuntimeInst.iscpo)
        jmp.op1 = codegen.codeIndex
        codegen.genCode(RuntimeInst.itry, -1)
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        val sb = StringBuilder()
        sb.append(prefix.toString())
        sb.append(KeywordType.TRY.desc)
        sb.append(" ")
        sb.append(tryBlock!!.print(prefix))
        sb.append(" ")
        sb.append(KeywordType.CATCH.desc)
        sb.append(" ")
        if (token != null) {
            sb.append("( ")
            sb.append(token!!.toRealString())
            sb.append(" ) ")
        }
        sb.append(catchBlock!!.print(prefix))
        return sb.toString()
    }

    override fun addClosure(scope: IClosureScope) {
        if (token != null)
            scope.addRef(token!!.obj!!)
        tryBlock!!.addClosure(scope)
        catchBlock!!.addClosure(scope)
    }
}