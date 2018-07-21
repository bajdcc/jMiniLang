package com.bajdcc.LALR1.grammar.codegen

import com.bajdcc.LALR1.grammar.runtime.IRuntimeDebugExec
import com.bajdcc.LALR1.grammar.runtime.IRuntimeStatus
import com.bajdcc.LALR1.grammar.runtime.RuntimeObject
import com.bajdcc.LALR1.grammar.runtime.RuntimeObjectType
import com.bajdcc.util.lexer.token.Token
import java.util.stream.Collectors

/**
 * 【扩展】扩展方法文档
 *
 * @author bajdcc
 */
class CodegenFuncDoc(private val doc: String, args: List<Token>) : IRuntimeDebugExec {
    var paramsDoc: String? = null
        private set

    init {
        this.paramsDoc = args.stream().map<String>{ it.toRealString() }.collect(Collectors.joining("，"))
        this.paramsDoc = if (this.paramsDoc!!.isEmpty()) "空" else this.paramsDoc
    }

    override fun ExternalProcCall(args: List<RuntimeObject>,
                                  status: IRuntimeStatus): RuntimeObject? {
        return null
    }

    override fun getArgsType(): Array<RuntimeObjectType>? {
        return null
    }

    override fun getDoc(): String {
        return doc
    }
}
