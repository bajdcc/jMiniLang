package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.LALR1.grammar.type.TokenTools
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 【语义分析】基本操作数
 *
 * @author bajdcc
 */
class ExpValue : IExp {

    /**
     * 单词
     */
    var token: Token? = null

    override fun isConstant(): Boolean {
        return token!!.type !== TokenType.ID
    }

    override fun isEnumerable(): Boolean {
        return false
    }

    override fun simplify(recorder: ISemanticRecorder): IExp {
        return this
    }

    override fun analysis(recorder: ISemanticRecorder) {

    }

    override fun genCode(codegen: ICodegen) {
        codegen.genCode(RuntimeInst.ipush, codegen.genDataRef(token!!.obj!!))
        if (token!!.type !== TokenType.ID) {
            codegen.genCode(RuntimeInst.iload)
        } else {
            if (TokenTools.isExternalName(token!!)) {
                codegen.genCode(RuntimeInst.iloadx)
            } else {
                codegen.genCode(RuntimeInst.iloadv)
            }
        }
    }

    override fun toString(): String {
        return token!!.toRealString()
    }

    override fun print(prefix: StringBuilder): String {
        return toString()
    }

    override fun addClosure(scope: IClosureScope) {
        if (token!!.type === TokenType.ID) {
            scope.addRef(token!!.obj!!)
        }
    }

    override fun setYield() {

    }
}
