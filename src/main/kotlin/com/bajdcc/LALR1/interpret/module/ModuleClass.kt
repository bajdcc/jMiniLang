package com.bajdcc.LALR1.interpret.module

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage
import com.bajdcc.LALR1.grammar.runtime.RuntimeDebugValue
import com.bajdcc.LALR1.grammar.runtime.RuntimeObject
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeMap
import com.bajdcc.util.ResourceLoader

/**
 * 【模块】类
 *
 * @author bajdcc
 */
class ModuleClass : IInterpreterModule {
    private var runtimeCodePage: RuntimeCodePage? = null

    override val moduleName: String
        get() = "sys.class"

    override val moduleCode: String
        get() = ResourceLoader.load(javaClass)

    override val codePage: RuntimeCodePage
        @Throws(Exception::class)
        get() {
            if (runtimeCodePage != null)
                return runtimeCodePage!!

            val base = ResourceLoader.load(javaClass)

            val grammar = Grammar(base)
            val page = grammar.codePage
            val info = page.info

            info.addExternalValue("g_class_context", RuntimeDebugValue { globalContext })

            runtimeCodePage = page
            return page
        }

    companion object {

        val instance = ModuleClass()
        private val globalContext = RuntimeObject(RuntimeMap())
    }
}