package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.CodegenBlock
import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.util.lexer.token.KeywordType
import com.bajdcc.util.lexer.token.OperatorType
import com.bajdcc.util.lexer.token.Token

/**
 * 【语义分析】迭代循环语句
 *
 * @author bajdcc
 */
class StmtForeach : IStmt {

    /**
     * 变量
     */
    var variable: Token? = null

    /**
     * 迭代表达式
     */
    var enumerator: IExp? = null

    /**
     * 块
     */
    var block: Block? = null

    override fun analysis(recorder: ISemanticRecorder) {
        enumerator!!.analysis(recorder)
        block!!.analysis(recorder)
    }

    override fun genCode(codegen: ICodegen) {
        codegen.genCode(RuntimeInst.ipushx)
        codegen.genCode(RuntimeInst.ipush, codegen.genDataRef(variable!!.obj!!))
        codegen.genCode(RuntimeInst.ialloc)
        codegen.genCode(RuntimeInst.ipop)
        val cb = CodegenBlock()
        val start = codegen.genCode(RuntimeInst.ijmp, -1)
        val exit = codegen.codeIndex
        codegen.genCode(RuntimeInst.ipop)
        cb.breakId = codegen.codeIndex
        codegen.genCode(RuntimeInst.iyldx)
        val breakJmp = codegen.genCode(RuntimeInst.ijmp, -1)
        cb.continueId = codegen.codeIndex
        val continueJmp = codegen.genCode(RuntimeInst.ijmp, -1)
        start.op1 = cb.continueId
        val content = codegen.codeIndex
        enumerator!!.genCode(codegen)
        codegen.genCode(RuntimeInst.ijnan, exit)
        codegen.genCode(RuntimeInst.ipush, codegen.genDataRef(variable!!.obj!!))
        codegen.genCode(RuntimeInst.istore)
        codegen.genCode(RuntimeInst.ipop)
        codegen.blockService.enterBlockEntry(cb)
        block!!.genCode(codegen)
        codegen.blockService.leaveBlockEntry()
        continueJmp.op1 = codegen.codeIndex
        codegen.genCode(RuntimeInst.ijmp, content)
        breakJmp.op1 = codegen.codeIndex
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String = "$prefix${KeywordType.FOREACH.desc} ( ${KeywordType.VARIABLE.desc} ${variable!!.toRealString()} ${OperatorType.COLON.desc} ${enumerator!!.print(prefix)} ) ${block!!.print(prefix)}"

    override fun addClosure(scope: IClosureScope) {
        enumerator!!.addClosure(scope)
        block!!.addClosure(scope)
    }
}