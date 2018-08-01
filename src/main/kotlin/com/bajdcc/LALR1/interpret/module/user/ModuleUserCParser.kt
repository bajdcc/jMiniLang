package com.bajdcc.LALR1.interpret.module.user

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage
import com.bajdcc.LALR1.interpret.module.IInterpreterModule
import com.bajdcc.util.ResourceLoader
import org.apache.log4j.Logger

/**
 * 【模块】用户态-C语言编译器
 *
 * @author bajdcc
 */
class ModuleUserCParser : IInterpreterModule {
    private var runtimeCodePage: RuntimeCodePage? = null

    override val moduleName: String
        get() = "user.cparser"

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

        val instance = ModuleUserCParser()
        private val logger = Logger.getLogger("cparser")
    }
}