package com.bajdcc.LALR1.interpret.module

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.*
import com.bajdcc.util.ResourceLoader
import java.math.BigDecimal
import java.util.*

/**
 * 【模块】数学模块
 *
 * @author bajdcc
 */
class ModuleMath : IInterpreterModule {
    private var runtimeCodePage: RuntimeCodePage? = null

    override val moduleName: String
        get() = "sys.math"

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
            info.addExternalValue("g_PI", RuntimeDebugValue { RuntimeObject(Math.PI) })
            info.addExternalValue("g_PI_2", RuntimeDebugValue { RuntimeObject(Math.PI * 2.0) })
            info.addExternalValue("g_E", RuntimeDebugValue { RuntimeObject(Math.E) })
            info.addExternalValue("g_random", RuntimeDebugValue { RuntimeObject(rand.nextDouble()) })
            buildUnaryFunc(info)

            runtimeCodePage = page
            return page
        }

    private fun buildUnaryFunc(info: IRuntimeDebugInfo) {
        info.addExternalFunc("g_sqrt",
                RuntimeDebugExec("开方", arrayOf(RuntimeObjectType.kReal, RuntimeObjectType.kInt))
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(Math.sqrt(args[0].double)) })
        info.addExternalFunc("g_cos",
                RuntimeDebugExec("余弦", arrayOf(RuntimeObjectType.kReal, RuntimeObjectType.kInt))
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(Math.cos(args[0].double)) })
        info.addExternalFunc("g_sin",
                RuntimeDebugExec("正弦", arrayOf(RuntimeObjectType.kReal, RuntimeObjectType.kInt))
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(Math.sin(args[0].double)) })
        info.addExternalFunc("g_floor",
                RuntimeDebugExec("四舍五入", arrayOf(RuntimeObjectType.kReal, RuntimeObjectType.kInt))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val d = args[0].double
                        val decimal = BigDecimal.valueOf(d)
                        val n = args[1].long
                        RuntimeObject(decimal.setScale(n.toInt(), BigDecimal.ROUND_HALF_UP).toDouble())
                    }
                })
        info.addExternalFunc("g_atan2",
                RuntimeDebugExec("atan(y, x)", arrayOf(RuntimeObjectType.kReal, RuntimeObjectType.kReal))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val y = args[0].double
                        val x = args[1].double
                        RuntimeObject(Math.atan2(y, x))
                    }
                })
        info.addExternalFunc("g_random_int",
                RuntimeDebugExec("随机数", arrayOf(RuntimeObjectType.kInt))
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(rand.nextInt(args[0].int).toLong()) })
    }

    companion object {

        val instance = ModuleMath()

        private val rand = Random()
    }
}
