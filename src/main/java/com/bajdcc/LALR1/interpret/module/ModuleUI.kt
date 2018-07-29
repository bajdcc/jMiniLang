package com.bajdcc.LALR1.interpret.module

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.*
import com.bajdcc.LALR1.ui.drawing.UIGraphics
import com.bajdcc.util.ResourceLoader
import java.util.*
import java.util.concurrent.LinkedBlockingDeque
import javax.swing.JPanel

/**
 * 【模块】界面
 *
 * @author bajdcc
 */
class ModuleUI : IInterpreterModule {
    private var graphics: UIGraphics? = null
    private var panel: JPanel? = null
    private val queue = LinkedBlockingDeque<Char>(10000)
    private val queueDisplay = ArrayDeque<Char>()
    private var sb = StringBuilder()
    private var runtimeCodePage: RuntimeCodePage? = null

    override val moduleName: String
        get() = "sys.ui"

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
            buildUIMethods(info)

            runtimeCodePage = page
            return page
        }

    fun setGraphics(graphics: UIGraphics) {
        this.graphics = graphics
    }

    fun setPanel(panel: JPanel) {
        this.panel = panel
    }

    fun addInputChar(c: Char) {
        queue.add(c)
    }

    fun addDisplayChar(c: Char) {
        queueDisplay.add(c)
    }

    private fun buildUIMethods(info: IRuntimeDebugInfo) {
        info.addExternalFunc("g_ui_print_internal_block",
                RuntimeDebugExec("显示输出", arrayOf(RuntimeObjectType.kChar))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val failure = graphics!!.drawText(args[0].char)
                        if (failure) {
                            status.service.processService.waitForUI()
                            status.service.processService.sleep(status.pid, PRINT_BLOCK_TIME)
                        }
                        RuntimeObject(failure)
                    }
                })
        info.addExternalFunc("g_ui_input_internal",
                RuntimeDebugExec("显示输入")
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        status.service.processService.sleep(status.pid, INPUT_TIME)
                        val c = queue.poll()
                        when (c) {
                            null -> null
                            '\n' -> {
                                val str = sb.toString()
                                sb = StringBuilder()
                                queueDisplay.clear()
                                RuntimeObject(str)
                            }
                            '\b' -> {
                                if (sb.isNotEmpty())
                                    sb.deleteCharAt(sb.length - 1)
                                graphics!!.drawText('\b')
                                null
                            }
                            else -> {
                                if (c < '\ufff0') {
                                    sb.append(c)
                                }
                                queueDisplay.add(c)
                                null
                            }
                        }
                    }
                })
        info.addExternalFunc("g_ui_input_im",
                RuntimeDebugExec("立即显示输入")
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        status.service.processService.sleep(status.pid, INPUT_TIME)
                        val str = sb.toString()
                        sb = StringBuilder()
                        queueDisplay.clear()
                        RuntimeObject(str)
                    }
                })
        info.addExternalFunc("g_ui_input_queue",
                RuntimeDebugExec("显示输入缓冲", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        status.service.processService.sleep(status.pid, INPUT_TIME)
                        val str = args[0].obj.toString()
                        sb.append(str)
                        for (c in str.toCharArray()) {
                            queueDisplay.add(c)
                        }
                        null
                    }
                })
        info.addExternalFunc("g_ui_print_input",
                RuntimeDebugExec("实时显示输入")
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        status.service.processService.sleep(status.pid, INPUT_TIME)
                        RuntimeObject(queueDisplay.poll())
                    }
                })
        info.addExternalFunc("g_ui_caret",
                RuntimeDebugExec("设置光标闪烁", arrayOf(RuntimeObjectType.kBool))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val caret = args[0].bool
                        if (caret) {
                            graphics!!.setCaret(true)
                            null
                        } else {
                            graphics!!.setCaret(false)
                            status.service.processService.sleep(status.pid, INPUT_TIME)
                            RuntimeObject(graphics!!.isHideCaret)
                        }
                    }
                })
        info.addExternalFunc("g_ui_fallback",
                RuntimeDebugExec("撤销上次输入")
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        sb = StringBuilder()
                        graphics!!.fallback()
                        null
                    }
                })
        info.addExternalFunc("g_ui_cols",
                RuntimeDebugExec("多少列")
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(graphics!!.cols.toLong()) })
        info.addExternalFunc("g_ui_rows",
                RuntimeDebugExec("多少行")
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(graphics!!.rows.toLong()) })
        info.addExternalFunc("g_ui_text_length",
                RuntimeDebugExec("一段文字宽度", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val str = args[0].obj.toString()
                        RuntimeObject(graphics!!.calcWidth(str).toLong())
                    }
                })
        info.addExternalFunc("g_ui_create_dialog_internal",
                RuntimeDebugExec("创建对话框", arrayOf(RuntimeObjectType.kString, RuntimeObjectType.kString, RuntimeObjectType.kInt))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val caption = args[0].obj.toString()
                        val text = args[1].obj.toString()
                        val mode = args[2].long
                        RuntimeObject(status.service.dialogService.create(caption, text, mode.toInt(), panel!!))
                    }
                })
        info.addExternalFunc("g_ui_show_dialog_internal",
                RuntimeDebugExec("弹出对话框", arrayOf(RuntimeObjectType.kPtr))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val handle = args[0].int
                        RuntimeObject(status.service.dialogService.show(handle))
                    }
                })
    }

    companion object {

        private const val INPUT_TIME = 10
        val instance = ModuleUI()
        private const val PRINT_BLOCK_TIME = 5
    }
}
