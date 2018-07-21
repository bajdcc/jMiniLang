package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.util.lexer.token.KeywordType
import com.bajdcc.util.lexer.token.OperatorType
import com.bajdcc.util.lexer.token.Token

/**
 * 【语义分析】导入/导出语句
 *
 * @author bajdcc
 */
class StmtPort : IStmt {

    /**
     * 名称
     */
    var name: Token? = null

    /**
     * 是否为导入
     */
    var isImported = true

    override fun analysis(recorder: ISemanticRecorder) {

    }

    override fun genCode(codegen: ICodegen) {
        if (isImported) {
            codegen.genCode(RuntimeInst.ipush, codegen.genDataRef(name!!.obj!!))
            codegen.genCode(RuntimeInst.iimp)
        }
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        return prefix.toString() +
                (if (isImported)
                    KeywordType.IMPORT.desc
                else
                    KeywordType.EXPORT
                            .desc) +
                " " + name!!.toRealString() +
                OperatorType.SEMI.desc
    }

    override fun addClosure(scope: IClosureScope) {

    }
}