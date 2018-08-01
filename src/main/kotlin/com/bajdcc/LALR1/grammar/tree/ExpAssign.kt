package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.util.lexer.token.KeywordType
import com.bajdcc.util.lexer.token.OperatorType
import com.bajdcc.util.lexer.token.Token

/**
 * 【语义分析】赋值表达式
 *
 * @author bajdcc
 */
class ExpAssign : IExp {

    /**
     * 变量名
     */
    var name: Token? = null

    /**
     * 表达式
     */
    var exp: IExp? = null

    /**
     * 是否为声明
     */
    var isDecleared = false

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
        if (exp != null)
            exp!!.analysis(recorder)
    }

    override fun genCode(codegen: ICodegen) {
        if (exp != null)
            exp!!.genCode(codegen)
        else
            codegen.genCode(RuntimeInst.ipushx)
        codegen.genCode(RuntimeInst.ipush, codegen.genDataRef(name!!.obj!!))
        if (isDecleared) {
            codegen.genCode(RuntimeInst.ialloc)
        } else {
            codegen.genCode(RuntimeInst.istore)
        }
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        return (if (isDecleared)
            KeywordType.VARIABLE.desc
        else
            KeywordType.LET
                    .desc) +
                " " + name!!.toRealString() +
                if (exp != null)
                    " " + OperatorType.ASSIGN.desc + " " +
                            exp!!.print(prefix)
                else
                    ""
    }

    override fun addClosure(scope: IClosureScope) {
        if (isDecleared) {
            scope.addDecl(name!!.obj!!)
        }
        if (exp != null)
            exp!!.addClosure(scope)
    }

    override fun setYield() {

    }
}