package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.util.lexer.token.KeywordType
import com.bajdcc.util.lexer.token.Token

/**
 * 【语义分析】循环控制表达式
 *
 * @author bajdcc
 */
class ExpCycleCtrl : IExp {

    /**
     * 变量名
     */
    var name: Token? = null

    override fun isConstant(): Boolean {
        return false
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
        val keyword = name!!.obj as KeywordType?
        if (keyword === KeywordType.BREAK) {
            codegen.blockService.genBreak()
        } else {
            codegen.blockService.genContinue()
        }
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        return name!!.toRealString()
    }

    override fun addClosure(scope: IClosureScope) {

    }

    override fun setYield() {

    }
}