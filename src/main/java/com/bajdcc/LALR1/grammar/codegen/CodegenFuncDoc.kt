package com.bajdcc.LALR1.grammar.codegen

import com.bajdcc.LALR1.grammar.runtime.IRuntimeDebugExec
import com.bajdcc.LALR1.grammar.runtime.IRuntimeStatus
import com.bajdcc.LALR1.grammar.runtime.RuntimeObject
import com.bajdcc.LALR1.grammar.runtime.RuntimeObjectType
import com.bajdcc.util.lexer.token.Token

/**
 * 【扩展】扩展方法文档
 *
 * @author bajdcc
 */
class CodegenFuncDoc(override val doc: String, args: List<Token>) : IRuntimeDebugExec {

    var paramsDoc: String = ""
        private set

    init {
        this.paramsDoc = args.joinToString(separator = "，", transform = { it.toRealString() })
        this.paramsDoc = if (this.paramsDoc.isEmpty()) "空" else this.paramsDoc
    }

    override fun ExternalProcCall(args: List<RuntimeObject>,
                                  status: IRuntimeStatus): RuntimeObject? {
        return null
    }

    override val argsType: Array<RuntimeObjectType>
        get() = throw NotImplementedError()
}
