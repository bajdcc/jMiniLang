package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.LALR1.grammar.type.TokenTools
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 【语义分析】三元表达式
 *
 * @author bajdcc
 */
class ExpTriop : IExp {

    /**
     * 前操作符
     */
    var firstToken: Token? = null

    /**
     * 后操作符
     */
    var secondToken: Token? = null

    /**
     * 第一操作数
     */
    var firstOperand: IExp? = null

    /**
     * 第二操作数
     */
    var secondOperand: IExp? = null

    /**
     * 第三操作数
     */
    var thirdOperand: IExp? = null

    override fun isConstant(): Boolean {
        return firstOperand!!.isConstant()
    }

    override fun isEnumerable(): Boolean {
        return (!firstOperand!!.isEnumerable() && secondOperand!!.isEnumerable()
                && thirdOperand!!.isEnumerable())
    }

    override fun simplify(recorder: ISemanticRecorder): IExp {
        if (!isConstant()) {
            return this
        }
        if (firstOperand is ExpValue) {
            if (firstToken!!.type == TokenType.OPERATOR && secondToken!!.type == TokenType.OPERATOR) {
                val triop = TokenTools.triop(recorder, this)
                when (triop) {
                    0 -> return this
                    1 -> return secondOperand!!
                    2 -> return thirdOperand!!
                    else -> {
                    }
                }
            }
        }
        return this
    }

    override fun analysis(recorder: ISemanticRecorder) {
        firstOperand!!.analysis(recorder)
        secondOperand!!.analysis(recorder)
        thirdOperand!!.analysis(recorder)
    }

    override fun genCode(codegen: ICodegen) {
        firstOperand!!.genCode(codegen)
        val jf = codegen.genCode(RuntimeInst.ijf, -1)
        secondOperand!!.genCode(codegen)
        val jmp = codegen.genCode(RuntimeInst.ijmp, -1)
        jf.op1 = codegen.codeIndex
        thirdOperand!!.genCode(codegen)
        jmp.op1 = codegen.codeIndex
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        return ("( " + firstOperand!!.print(prefix) + " " + firstToken!!.toRealString()
                + " " + secondOperand!!.print(prefix) + " "
                + secondToken!!.toRealString() + " " + thirdOperand!!.print(prefix)
                + " )")
    }

    override fun addClosure(scope: IClosureScope) {
        firstOperand!!.addClosure(scope)
        secondOperand!!.addClosure(scope)
        thirdOperand!!.addClosure(scope)
    }

    override fun setYield() {

    }
}
