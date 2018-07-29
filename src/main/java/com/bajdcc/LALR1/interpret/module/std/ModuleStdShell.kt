package com.bajdcc.LALR1.interpret.module.std

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage
import com.bajdcc.LALR1.interpret.module.IInterpreterModule
import com.bajdcc.util.ResourceLoader

/**
 * 【模块】命令行
 *
 * @author bajdcc
 */
class ModuleStdShell : IInterpreterModule {
    private var runtimeCodePage: RuntimeCodePage? = null

    override val moduleName: String
        get() = "std.shell"

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

            runtimeCodePage = page
            return page
        }

    companion object {

        val instance = ModuleStdShell()
    }
}