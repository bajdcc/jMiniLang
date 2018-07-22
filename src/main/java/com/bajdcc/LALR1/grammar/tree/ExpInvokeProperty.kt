package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.error.SemanticException.SemanticError
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.util.lexer.token.OperatorType
import com.bajdcc.util.lexer.token.Token
import java.util.*

/**
 * 【语义分析】类方法调用表达式
 *
 * @author bajdcc
 */
class ExpInvokeProperty : IExp {

    var token: Token? = null

    /**
     * 对象
     */
    var obj: IExp? = null

    /**
     * 属性
     */
    var property: IExp? = null

    /**
     * 参数
     */
    var params: List<IExp> = ArrayList()

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
        if (params.size > 9) {
            recorder.add(SemanticError.TOO_MANY_ARGS, token)
        }
        for (exp in params) exp.analysis(recorder)
    }

    override fun genCode(codegen: ICodegen) {
        codegen.genCode(RuntimeInst.iopena)
        obj!!.genCode(codegen)
        codegen.genCode(RuntimeInst.ipusha)
        property!!.genCode(codegen)
        codegen.genCode(RuntimeInst.ipusha)
        if (params.isEmpty()) {
            codegen.genCode(RuntimeInst.ipush, codegen.genDataRef("g_invoke_method"))
            codegen.genCode(RuntimeInst.icallx)
        } else {
            params.forEach { exp ->
                exp.genCode(codegen)
                codegen.genCode(RuntimeInst.ipusha)
            }
            codegen.genCode(RuntimeInst.ipush, codegen.genDataRef("g_invoke_method_" + params.size))
            codegen.genCode(RuntimeInst.icallx)
        }
    }

    override fun toString(): String {
        return print(StringBuilder())
    }

    override fun print(prefix: StringBuilder): String {
        val sb = StringBuilder()
        sb.append(obj!!.print(prefix)).append(".").append(property!!.print(prefix))
        if (!params.isEmpty()) {
            sb.append(OperatorType.LPARAN.desc).append(" ")
            if (params.size == 1) {
                sb.append(params[0].print(prefix))
            } else {
                for (i in params.indices) {
                    sb.append(params[i].print(prefix))
                    if (i != params.size - 1) {
                        sb.append(OperatorType.COMMA.desc).append(" ")
                    }
                }
            }
            sb.append(" ").append(OperatorType.RPARAN.desc)
        } else {
            sb.append(OperatorType.LPARAN.desc).append(OperatorType.RPARAN.desc)
        }
        return sb.toString()
    }

    override fun addClosure(scope: IClosureScope) {
        obj!!.addClosure(scope)
        property!!.addClosure(scope)
        params.forEach { exp ->
            exp.addClosure(scope)
        }
    }

    override fun setYield() {

    }
}
