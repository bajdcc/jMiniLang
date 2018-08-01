package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.util.lexer.token.OperatorType
import com.bajdcc.util.lexer.token.Token

/**
 * 【语义分析】类属性赋值表达式
 *
 * @author bajdcc
 */
class ExpAssignProperty : IExp {

    /**
     * 操作符
     */
    private var token: Token? = null

    /**
     * 对象
     */
    var obj: IExp? = null

    /**
     * 属性
     */
    var property: IExp? = null

    /**
     * 表达式
     */
    var exp: IExp? = null

    fun setToken(token: Token) {
        this.token = token
    }

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
        obj!!.analysis(recorder)
        property!!.analysis(recorder)
        exp?.analysis(recorder)
    }

    override fun genCode(codegen: ICodegen) {
        if (token == null || token!!.obj === OperatorType.EQ_ASSIGN) {
            codegen.genCode(RuntimeInst.iopena)
            obj!!.genCode(codegen)
            codegen.genCode(RuntimeInst.ipusha)
            property!!.genCode(codegen)
            codegen.genCode(RuntimeInst.ipusha)
            exp!!.genCode(codegen)
            codegen.genCode(RuntimeInst.ipusha)
            codegen.genCode(RuntimeInst.ipush, codegen.genDataRef("g_set_property_unary"))
            codegen.genCode(RuntimeInst.icallx)
        } else if (token!!.obj === OperatorType.PLUS_PLUS || token!!.obj === OperatorType.MINUS_MINUS) {
            codegen.genCode(RuntimeInst.iopena)
            obj!!.genCode(codegen)
            codegen.genCode(RuntimeInst.ipusha)
            property!!.genCode(codegen)
            codegen.genCode(RuntimeInst.ipusha)
            codegen.genCode(RuntimeInst.iopena)
            obj!!.genCode(codegen)
            codegen.genCode(RuntimeInst.ipusha)
            property!!.genCode(codegen)
            codegen.genCode(RuntimeInst.ipusha)
            codegen.genCode(RuntimeInst.ipush, codegen.genDataRef("g_get_property"))
            codegen.genCode(RuntimeInst.icallx)
            if (token!!.obj === OperatorType.PLUS_PLUS) {
                codegen.genCode(RuntimeInst.iinc)
            } else {
                codegen.genCode(RuntimeInst.idec)
            }
            codegen.genCode(RuntimeInst.ipusha)
            codegen.genCode(RuntimeInst.ipush, codegen.genDataRef("g_set_property_unary"))
            codegen.genCode(RuntimeInst.icallx)
        } else {
            codegen.genCode(RuntimeInst.iopena)
            obj!!.genCode(codegen)
            codegen.genCode(RuntimeInst.ipusha)
            property!!.genCode(codegen)
            codegen.genCode(RuntimeInst.ipusha)
            codegen.genCode(RuntimeInst.iopena)
            obj!!.genCode(codegen)
            codegen.genCode(RuntimeInst.ipusha)
            property!!.genCode(codegen)
            codegen.genCode(RuntimeInst.ipusha)
            codegen.genCode(RuntimeInst.ipush, codegen.genDataRef("g_get_property"))
            codegen.genCode(RuntimeInst.icallx)
            exp!!.genCode(codegen)
            when (token!!.obj as OperatorType?) {
                OperatorType.PLUS_ASSIGN -> codegen.genCode(RuntimeInst.iadd)
                OperatorType.MINUS_ASSIGN -> codegen.genCode(RuntimeInst.isub)
                OperatorType.TIMES_ASSIGN -> codegen.genCode(RuntimeInst.imul)
                OperatorType.DIV_ASSIGN -> codegen.genCode(RuntimeInst.idiv)
                OperatorType.AND_ASSIGN -> codegen.genCode(RuntimeInst.iand)
                OperatorType.OR_ASSIGN -> codegen.genCode(RuntimeInst.ior)
                OperatorType.XOR_ASSIGN -> codegen.genCode(RuntimeInst.ixor)
                OperatorType.MOD_ASSIGN -> codegen.genCode(RuntimeInst.imod)
                else -> {}
            }
            codegen.genCode(RuntimeInst.ipusha)
            codegen.genCode(RuntimeInst.ipush, codegen.genDataRef("g_set_property_unary"))
            codegen.genCode(RuntimeInst.icallx)
        }
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        return if (token != null && (token!!.obj === OperatorType.PLUS_PLUS || token!!.obj === OperatorType.MINUS_MINUS)) {
            obj!!.print(prefix) + "." + property!!.print(prefix) +
                    " " + token!!.toRealString()
        } else obj!!.print(prefix) + "." + property!!.print(prefix) +
                " " + (if (token == null) OperatorType.ASSIGN.desc else token!!.toRealString()) + " " +
                exp!!.print(prefix)
    }

    override fun addClosure(scope: IClosureScope) {
        exp?.addClosure(scope)
        property!!.addClosure(scope)
        obj!!.addClosure(scope)
    }

    override fun setYield() {

    }
}