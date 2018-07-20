package com.bajdcc.LALR1.grammar.tree

import com.bajdcc.LALR1.grammar.codegen.ICodegen
import com.bajdcc.LALR1.grammar.runtime.RuntimeInst
import com.bajdcc.LALR1.grammar.semantic.ISemanticRecorder
import com.bajdcc.LALR1.grammar.tree.closure.IClosureScope
import com.bajdcc.util.lexer.token.KeywordType
import com.bajdcc.util.lexer.token.Token

/**
 * 【语义分析】函数
 * [name]：名称（Lambda记号）
 *
 * @author bajdcc
 */
class Function(var name: Token) : IExp {

    /**
     * 真实名称（变量名）
     */
    var realName: String = ""

    /**
     * 类方法名
     */
    private var methodName: String = ""

    /**
     * 参数
     */
    var params = mutableListOf<Token>()

    /**
     * 函数主体Block
     */
    var block: Block? = null

    /**
     * 文档
     */
    private var doc: List<Token>? = null

    /**
     * 外部化
     */
    var isExtern = false

    /**
     * 是否支持YIELD
     */
    var isYield = false

    val refName: String
        get() = if (name.toRealString() == realName) {
            if (true) "$realName#$methodName" else realName
        } else name.toRealString() + "$" + realName

    fun setMethodName(methodName: String) {
        this.methodName = methodName
    }

    fun getDoc(): String {
        if (doc == null || doc!!.isEmpty()) {
            return "过程无文档"
        }
        val sb = StringBuilder()
        for (token in doc!!) {
            sb.append(token.obj.toString())
            sb.append(System.lineSeparator())
        }
        return sb.toString()
    }

    fun setDoc(doc: List<Token>) {
        this.doc = doc
    }

    override fun isConstant(): Boolean {
        return false
    }

    override fun isEnumerable(): Boolean {
        return isYield
    }

    override fun simplify(recorder: ISemanticRecorder): IExp {
        return this
    }

    override fun analysis(recorder: ISemanticRecorder) {
        block!!.analysis(recorder)
    }

    override fun genCode(codegen: ICodegen) {
        codegen.genFuncEntry(refName)
        codegen.genCode(RuntimeInst.inop)
        if (name.toRealString() == realName) {
            codegen.genCode(RuntimeInst.ipush, codegen.genDataRef(realName))
            codegen.genCode(RuntimeInst.irefun)
        }
        val start = codegen.codeIndex
        var i = 0
        for (token in params) {
            codegen.genCode(RuntimeInst.iloada, i)
            codegen.genCode(RuntimeInst.ipush, codegen.genDataRef(token.obj))
            codegen.genCode(RuntimeInst.ialloc)
            codegen.genCode(RuntimeInst.ipop)
            i++
        }
        block!!.genCode(codegen)
        val end = codegen.codeIndex - 1
        if (start <= end) {
            codegen.genDebugInfo(start, end, this.toString())
        }
        codegen.genCode(RuntimeInst.inop)
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
        sb.append(KeywordType.FUNCTION.desc)
        sb.append(" ")
        sb.append(realName)
        sb.append(" [ ").append(methodName).append(" ] ")
        sb.append("(")
        for (param in params) {
            sb.append(param.toRealString())
            sb.append(", ")
        }
        if (!params.isEmpty()) {
            sb.deleteCharAt(sb.length - 1)
            sb.deleteCharAt(sb.length - 1)
        }
        sb.append(") ")
        if (block != null) {
            sb.append(block!!.print(prefix))
        }
        return sb.toString()
    }

    override fun addClosure(scope: IClosureScope) {
        for (param in params) {
            scope.addDecl(param.obj)
        }
        block!!.addClosure(scope)
    }

    override fun setYield() {

    }
}
