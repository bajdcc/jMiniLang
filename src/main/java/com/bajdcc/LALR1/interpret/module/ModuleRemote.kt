package com.bajdcc.LALR1.interpret.module

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.*
import com.bajdcc.LALR1.ui.UIMainFrame
import com.bajdcc.LALR1.ui.UIRemoteWindow
import com.bajdcc.LALR1.ui.drawing.UIRemoteGraphics
import com.bajdcc.util.ResourceLoader

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
/**
 * 【模块】远程用户界面
 *
 * @author bajdcc
 */
class ModuleRemote : IInterpreterModule {
    private var remote: UIRemoteWindow? = null
    private var graphics: UIRemoteGraphics? = null
    private var runtimeCodePage: RuntimeCodePage? = null

    override val moduleName: String
        get() = "sys.remote"

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
            buildRemoteMethods(info)

            runtimeCodePage = page
            return page
        }

    fun setGraphics() {
        if (remote == null) {
            remote = UIRemoteWindow()
            graphics = remote!!.uiGraphics
        }
    }

    private fun buildRemoteMethods(info: IRuntimeDebugInfo) {
        info.addExternalFunc("g_remote_init",
                RuntimeDebugExec("显示初始化")
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        if (enable && graphics == null)
                            setGraphics()
                        null
                    }
                })
        info.addExternalFunc("g_remote_print_internal",
                RuntimeDebugExec("显示输出", arrayOf(RuntimeObjectType.kChar))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        graphics!!.drawText(args[0].char)
                        null
                    }
                })
    }

    companion object {

        val instance = ModuleRemote()
        private var enable = false
        private var mainFrame: UIMainFrame? = null

        fun enabled() {
            enable = true
        }

        fun setMainFrame(frame: UIMainFrame) {
            mainFrame = frame
        }

        fun showMainFrame() {
            mainFrame!!.showDelay()
        }
    }
}
