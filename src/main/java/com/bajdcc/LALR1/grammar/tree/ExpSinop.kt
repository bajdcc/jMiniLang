package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.error.SemanticException.SemanticError
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.LALR1.grammar.type.TokenTools
import com.bajdcc.util.lexer.token.OperatorType
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 【语义分析】一元表达式
 *
 * @author bajdcc
 */
class ExpSinop(var token: Token,
               var operand: IExp) : IExp {

    override fun isConstant(): Boolean {
        return operand.isConstant()
    }

    override fun isEnumerable(): Boolean {
        return operand.isEnumerable()
    }

    override fun simplify(recorder: ISemanticRecorder): IExp {
        if (!isConstant()) {
            return this
        }
        if (operand is ExpValue) {
            if (token.type === TokenType.OPERATOR) {
                if (TokenTools.sinop(recorder, this)) {
                    return operand
                }
            }
        }
        return this
    }

    override fun analysis(recorder: ISemanticRecorder) {
        val type = token.obj as OperatorType?
        if (type === OperatorType.PLUS_PLUS || type === OperatorType.MINUS_MINUS) {
            if (operand !is ExpValue) {
                recorder.add(SemanticError.INVALID_OPERATOR, token)
            }
        } else {
            operand.analysis(recorder)
        }
    }

    override fun genCode(codegen: ICodegen) {
        operand.genCode(codegen)
        codegen.genCode(TokenTools.op2ins(token))
        val type = token.obj as OperatorType?
        if (type == OperatorType.PLUS_PLUS || type == OperatorType.MINUS_MINUS) {
            val value = operand as ExpValue?
            codegen.genCode(RuntimeInst.ipush,
                    codegen.genDataRef(value!!.token.obj!!))
            codegen.genCode(RuntimeInst.icopy)
        }
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        return "( " + token.toRealString() + " " + operand.print(prefix) + " )"
    }

    override fun addClosure(scope: IClosureScope) {
        operand.addClosure(scope)
    }

    override fun setYield() {
        operand.setYield()
    }
}
