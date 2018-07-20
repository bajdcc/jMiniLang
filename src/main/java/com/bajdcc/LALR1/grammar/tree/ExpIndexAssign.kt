package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.util.lexer.token.OperatorType
import com.bajdcc.util.lexer.token.Token

/**
 * 【语义分析】间接寻址赋值
 *
 * @author bajdcc
 */
class ExpIndexAssign : IExp {

    /**
     * 操作符
     */
    private var token: Token? = null

    /**
     * 对象
     */
    var exp: IExp? = null

    /**
     * 索引
     */
    var index: IExp? = null

    /**
     * 对象
     */
    var obj: IExp? = null

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
        exp = exp!!.simplify(recorder)
        index = index!!.simplify(recorder)
        obj = obj!!.simplify(recorder)
        return this
    }

    override fun analysis(recorder: ISemanticRecorder) {
        exp!!.analysis(recorder)
        index!!.analysis(recorder)
        obj!!.analysis(recorder)
    }

    override fun genCode(codegen: ICodegen) {
        if (token == null || token!!.obj === OperatorType.EQ_ASSIGN) {
            obj!!.genCode(codegen)
        } else {
            exp!!.genCode(codegen)
            index!!.genCode(codegen)
            codegen.genCode(RuntimeInst.iidx)
            obj!!.genCode(codegen)
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
        }
        exp!!.genCode(codegen)
        index!!.genCode(codegen)
        codegen.genCode(RuntimeInst.iidxa)
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        return exp!!.print(prefix) +
                OperatorType.LSQUARE.desc +
                index!!.print(prefix) +
                OperatorType.RSQUARE.desc +
                " " +
                (if (token == null) OperatorType.ASSIGN.desc else token!!.toRealString()) +
                " " +
                obj!!.print(prefix)
    }

    override fun addClosure(scope: IClosureScope) {
        exp!!.addClosure(scope)
        index!!.addClosure(scope)
        obj!!.addClosure(scope)
    }

    override fun setYield() {

    }
}
