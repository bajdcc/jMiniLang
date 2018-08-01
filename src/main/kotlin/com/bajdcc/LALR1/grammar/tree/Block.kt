package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope

/**
 * 【语义分析】块
 *
 * @author bajdcc
 */
class Block(var stmts: MutableList<IStmt> = mutableListOf()) : ICommon {

    override fun analysis(recorder: ISemanticRecorder) {
        stmts.forEach { stmt ->
            stmt.analysis(recorder)
        }
    }

    override fun genCode(codegen: ICodegen) {
        codegen.genCode(RuntimeInst.iscpi)
        for (stmt in stmts) {
            val start = codegen.codeIndex
            stmt.genCode(codegen)
            val end = codegen.codeIndex - 1
            if (start <= end) {
                codegen.genDebugInfo(start, end, stmt.toString())
            }
            if (stmt is StmtReturn) {
                if (!stmt.isYield)
                    break
            }
        }
        codegen.genCode(RuntimeInst.iscpo)
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        val sb = StringBuilder()
        sb.append("{")
        sb.append(System.lineSeparator())
        prefix.append("    ")
        stmts.forEach { stmt ->
            sb.append(stmt.print(prefix))
            sb.append(System.lineSeparator())
        }
        prefix.delete(0, 4)
        sb.append(prefix.toString()).append("}")
        return sb.toString()
    }

    override fun addClosure(scope: IClosureScope) {
        for (stmt in stmts) {
            stmt.addClosure(scope)
            if (stmt is StmtReturn) {
                break
            }
        }
    }
}
