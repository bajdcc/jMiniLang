package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.error.SemanticException.SemanticError
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.util.lexer.token.KeywordType
import com.bajdcc.util.lexer.token.Token

/**
 * 【语义分析】函数调用表达式
 *
 * @author bajdcc
 */
class ExpInvoke : IExp {

    /**
     * 调用名
     */
    var name: Token? = null

    /**
     * 调用函数
     */
    var func: Func? = null

    /**
     * 外部函数名
     */
    var extern: Token? = null

    /**
     * 参数
     */
    var params: MutableList<IExp> = mutableListOf()

    /**
     * 是否为函数指针调用
     */
    var isInvoke = false

    /**
     * 是否为YIELD调用
     */
    var isYield = false

    /**
     * 函数名是否为函数表达式
     */
    var invokeExp: ExpInvoke? = null

    override fun isConstant(): Boolean {
        return false
    }

    override fun isEnumerable(): Boolean {
        return func == null || func!!.isEnumerable()
    }

    override fun simplify(recorder: ISemanticRecorder): IExp {
        for (i in params.indices) {
            params[i] = params[i].simplify(recorder)
        }
        return this
    }

    override fun analysis(recorder: ISemanticRecorder) {
        if (func != null && !func!!.isExtern) {
            checkArgsCount(recorder)
            if (func!!.realName.startsWith("~")) {
                func!!.analysis(recorder)
            }
            if (func!!.isYield xor isYield) {
                recorder.add(SemanticError.WRONG_YIELD, name!!)
            }
        }
        for (exp in params) exp.analysis(recorder)
    }

    /**
     * 参数个数检查
     *
     * @param recorder 错误记录
     */
    private fun checkArgsCount(recorder: ISemanticRecorder) {
        val invokeArgsCount = params.size
        val funcArgsCount = func!!.params.size
        if (invokeArgsCount != funcArgsCount) {
            recorder.add(SemanticError.MISMATCH_ARGS, name!!)
        }
    }

    override fun genCode(codegen: ICodegen) {
        if (isYield) {
            val yldLine = codegen.codeIndex
            val yld = codegen.genCode(RuntimeInst.ijyld, -1)
            if (func != null) {
                codegen.genCode(RuntimeInst.ipush, 1) // call本地地址，1
                codegen.genCode(RuntimeInst.iyldi)
                params.forEach { exp ->
                    exp.genCode(codegen)
                    codegen.genCode(RuntimeInst.iyldi)
                }
                codegen.genCodeWithFuncWriteBack(RuntimeInst.ipush,
                        codegen.getFuncIndex(func!!))
                codegen.genCode(RuntimeInst.iyldi)
            } else {
                if (isInvoke) {
                    codegen.genCode(RuntimeInst.ipush, 2) // call本地符号，2
                    codegen.genCode(RuntimeInst.iyldi)
                } else {
                    codegen.genCode(RuntimeInst.ipush, 3) // call外部模块，3
                    codegen.genCode(RuntimeInst.iyldi)
                }
                params.forEach { exp ->
                    exp.genCode(codegen)
                    codegen.genCode(RuntimeInst.iyldi)
                }
                codegen.genCode(RuntimeInst.ipush,
                        codegen.genDataRef(extern!!.obj!!))
                codegen.genCode(RuntimeInst.iyldi)
            }
            codegen.genCode(RuntimeInst.iyldy, yldLine)
            codegen.genCode(RuntimeInst.iyldo)
            val jmp = codegen.genCode(RuntimeInst.ijmp, -1)
            yld.op1 = codegen.codeIndex
            codegen.genCode(RuntimeInst.iyldr, yldLine)
            codegen.genCode(RuntimeInst.iyldo)
            jmp.op1 = codegen.codeIndex
        } else if (invokeExp != null) {
            invokeExp!!.genCode(codegen)
            codegen.genCode(RuntimeInst.iopena)
            params.forEach { exp ->
                exp.genCode(codegen)
                codegen.genCode(RuntimeInst.ipusha)
            }
            codegen.genCode(RuntimeInst.ipush, -1)
            codegen.genCode(RuntimeInst.ically)
        } else {
            codegen.genCode(RuntimeInst.iopena)
            params.forEach { exp ->
                exp.genCode(codegen)
                codegen.genCode(RuntimeInst.ipusha)
            }
            if (func != null) {
                codegen.genCodeWithFuncWriteBack(RuntimeInst.ipush,
                        codegen.getFuncIndex(func!!))
                codegen.genCode(RuntimeInst.icall)
            } else {
                codegen.genCode(RuntimeInst.ipush,
                        codegen.genDataRef(extern!!.obj!!))
                if (isInvoke) {
                    codegen.genCode(RuntimeInst.ically)
                } else {
                    codegen.genCode(RuntimeInst.icallx)
                }
            }
        }
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        val sb = StringBuilder()
        if (isYield) {
            sb.append(KeywordType.YIELD.desc)
            sb.append(" ")
        }
        sb.append(KeywordType.CALL.desc).append(" ")
        if (func != null) {
            if (!func!!.realName.startsWith("~")) {
                sb.append(func!!.realName)
            } else {
                sb.append(func!!.print(prefix))
            }
        } else if (invokeExp != null) {
            sb.append("( ")
            sb.append(invokeExp!!.print(prefix))
            sb.append(" )")
        } else {
            sb.append(KeywordType.EXTERN.desc)
            sb.append(" ")
            sb.append(extern!!.toRealString())
        }
        if (!params.isEmpty()) {
            sb.append("( ")
            if (params.size == 1) {
                sb.append(params[0].print(prefix))
            } else {
                for (i in params.indices) {
                    sb.append(params[i].print(prefix))
                    if (i != params.size - 1) {
                        sb.append(", ")
                    }
                }
            }
            sb.append(" )")
        }
        return sb.toString()
    }

    override fun addClosure(scope: IClosureScope) {
        if (isInvoke) {
            scope.addRef(extern!!.obj!!)
        }
        invokeExp?.addClosure(scope)
        params.forEach { exp ->
            exp.addClosure(scope)
        }
    }

    override fun setYield() {
        isYield = true
    }
}
