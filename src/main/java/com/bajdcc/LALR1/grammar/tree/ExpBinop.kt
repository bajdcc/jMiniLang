package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.error.SemanticException
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.runtime.RuntimeInstUnary
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.LALR1.grammar.type.TokenTools
import com.bajdcc.util.lexer.token.OperatorType
import com.bajdcc.util.lexer.token.Token
import com.bajdcc.util.lexer.token.TokenType

/**
 * 【语义分析】二元表达式
 *
 * @author bajdcc
 */
class ExpBinop : IExp {

    /**
     * 操作符
     */
    var token: Token? = null

    /**
     * 左操作数
     */
    var leftOperand: IExp? = null

    /**
     * 右操作数
     */
    var rightOperand: IExp? = null

    override fun isConstant(): Boolean {
        return leftOperand!!.isConstant() && rightOperand!!.isConstant()
    }

    override fun isEnumerable(): Boolean {
        return leftOperand!!.isEnumerable() xor rightOperand!!.isEnumerable()
    }

    override fun simplify(recorder: ISemanticRecorder): IExp {
        if (!isConstant()) {
            return this
        }
        if (leftOperand is ExpValue && rightOperand is ExpValue) {
            if (token!!.type === TokenType.OPERATOR) {
                val op = token!!.obj as OperatorType?
                if (op === OperatorType.COLON)
                    return this
                if (TokenTools.binop(recorder, this)) {
                    return leftOperand!!
                }
            }
        }
        return this
    }

    override fun analysis(recorder: ISemanticRecorder) {
        if (token!!.type === TokenType.OPERATOR) {
            val op = token!!.obj as OperatorType?
            if (TokenTools.isAssignment(op!!)) {
                if (leftOperand !is ExpValue) {
                    recorder.add(SemanticException.SemanticError.INVALID_ASSIGNMENT, token)
                }
            }
        }
        leftOperand!!.analysis(recorder)
        rightOperand!!.analysis(recorder)
    }

    override fun genCode(codegen: ICodegen) {
        if (token!!.type === TokenType.OPERATOR && token!!.obj === OperatorType.DOT) {
            codegen.genCode(RuntimeInst.iopena)
            leftOperand!!.genCode(codegen)
            codegen.genCode(RuntimeInst.ipusha)
            rightOperand!!.genCode(codegen)
            codegen.genCode(RuntimeInst.ipusha)
            codegen.genCode(RuntimeInst.ipush, codegen.genDataRef("g_get_property"))
            codegen.genCode(RuntimeInst.icallx)
            return
        }
        if (token!!.type === TokenType.OPERATOR) {
            val op = token!!.obj as OperatorType?
            if (TokenTools.isAssignment(op!!)) {
                val ins = TokenTools.op2ins(token!!)
                val left = leftOperand as ExpValue?
                if (ins == RuntimeInst.ice) {
                    rightOperand!!.genCode(codegen)
                    codegen.genCode(RuntimeInst.ipush, codegen.genDataRef(left!!.token!!.obj!!))
                    codegen.genCode(RuntimeInst.istore)
                    return
                }
                leftOperand!!.genCode(codegen)
                rightOperand!!.genCode(codegen)
                codegen.genCode(ins)
                codegen.genCode(RuntimeInst.ipush, codegen.genDataRef(left!!.token!!.obj))
                codegen.genCode(RuntimeInst.istore)
                return
            } else if (op === OperatorType.COLON) {
                rightOperand!!.genCode(codegen)
                leftOperand!!.genCode(codegen)
                return
            }
        }
        val inst = TokenTools.op2ins(token!!)
        leftOperand!!.genCode(codegen)
        var jmp: RuntimeInstUnary? = null
        when (inst) {
            RuntimeInst.iandl -> jmp = codegen.genCode(RuntimeInst.ijfx, -1)
            RuntimeInst.iorl -> jmp = codegen.genCode(RuntimeInst.ijtx, -1)
            else -> {
            }
        }
        rightOperand!!.genCode(codegen)
        codegen.genCode(inst)
        if (jmp != null) {
            jmp.op1 = codegen.codeIndex
        }
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        return if (token!!.type === TokenType.OPERATOR && token!!.obj === OperatorType.COLON) {
            (leftOperand!!.print(prefix) + " " + token!!.toRealString() + " "
                    + rightOperand!!.print(prefix))
        } else "( " + leftOperand!!.print(prefix) + " " + token!!.toRealString() + " " + rightOperand!!.print(prefix) + " )"
    }

    override fun addClosure(scope: IClosureScope) {
        leftOperand!!.addClosure(scope)
        rightOperand!!.addClosure(scope)
    }

    override fun setYield() {
        leftOperand!!.setYield()
        rightOperand!!.setYield()
    }
}
