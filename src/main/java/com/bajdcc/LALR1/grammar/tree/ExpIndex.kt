package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.util.lexer.token.OperatorType

/**
 * 【语义分析】间接寻址
 *
 * @author bajdcc
 */
class ExpIndex : IExp {

    /**
     * 对象
     */
    var exp: IExp? = null

    /**
     * 索引
     */
    var index: IExp? = null

    override fun isConstant(): Boolean {
        return false
    }

    override fun isEnumerable(): Boolean {
        return false
    }

    override fun simplify(recorder: ISemanticRecorder): IExp {
        exp = exp!!.simplify(recorder)
        index = index!!.simplify(recorder)
        return this
    }

    override fun analysis(recorder: ISemanticRecorder) {
        exp!!.analysis(recorder)
        index!!.analysis(recorder)
    }

    override fun genCode(codegen: ICodegen) {
        exp!!.genCode(codegen)
        index!!.genCode(codegen)
        codegen.genCode(RuntimeInst.iidx)
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        return exp!!.print(prefix) +
                OperatorType.LSQUARE.desc +
                index!!.print(prefix) +
                OperatorType.RSQUARE.desc
    }

    override fun addClosure(scope: IClosureScope) {
        exp!!.addClosure(scope)
        index!!.addClosure(scope)
    }

    override fun setYield() {

    }
}
