package com.bajdcc.LALR1.ui

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage
import com.bajdcc.LALR1.grammar.runtime.RuntimeException
import com.bajdcc.LALR1.interpret.Interpreter
import com.bajdcc.LALR1.interpret.module.ModuleRemote
import com.bajdcc.LALR1.interpret.os.irq.IRPrint
import com.bajdcc.LALR1.interpret.os.irq.IRRemote
import com.bajdcc.LALR1.interpret.os.irq.IRSignal
import com.bajdcc.LALR1.interpret.os.irq.IRTask
import com.bajdcc.LALR1.interpret.os.kern.OSEntry
import com.bajdcc.LALR1.interpret.os.kern.OSIrq
import com.bajdcc.LALR1.interpret.os.kern.OSTask
import com.bajdcc.LALR1.interpret.os.task.*
import com.bajdcc.LALR1.interpret.os.ui.UIClock
import com.bajdcc.LALR1.interpret.os.ui.UIHitokoto
import com.bajdcc.LALR1.interpret.os.ui.UIMain
import com.bajdcc.LALR1.interpret.os.ui.UIMonitor
import com.bajdcc.LALR1.interpret.os.user.UserMain
import com.bajdcc.LALR1.interpret.os.user.routine.*
import com.bajdcc.LALR1.interpret.os.user.routine.file.URFileAppend
import com.bajdcc.LALR1.interpret.os.user.routine.file.URFileLoad
import com.bajdcc.LALR1.interpret.os.user.routine.file.URFileSave
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.util.lexer.error.RegexException
import com.sun.jna.platform.win32.User32
import org.apache.log4j.Logger
import java.awt.Dimension
import java.awt.Font
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import javax.swing.JFrame
import javax.swing.UIManager
import javax.swing.WindowConstants
import javax.swing.plaf.FontUIResource

/**
 * 【界面】窗口
 *
 * @author bajdcc
 */
class UIMainFrame : JFrame() {

    private val panel: UIPanel
    private var isExitNormally = false
    private var interpreter: Interpreter? = null

    init {
        initGlobalFont()
        panel = UIPanel()
        this.title = mainWndTitle
        this.defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE
        this.preferredSize = Dimension(panel.uiGraphics.w, panel.uiGraphics.h)
        this.contentPane = panel
        this.pack()
        this.setLocationRelativeTo(null)
        //this.setAlwaysOnTop(true);
        this.isResizable = false
        this.isVisible = true
        this.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                if (isExitNormally) {
                    logger.info("Exit.")
                } else {
                    logger.info("Exit by window closing.")
                    this@UIMainFrame.stopOS()
                }
            }
        })
    }

    private fun stopOS() {
        interpreter!!.stop()
    }

    private fun exit() {
        isExitNormally = true
        this@UIMainFrame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        dispatchEvent(WindowEvent(this, WindowEvent.WINDOW_CLOSING))
    }

    private fun setTimer() {
        javax.swing.Timer(33) { panel.repaint() }.start()
    }

    private fun startOS() {
        val pages = arrayOf(
                // OS
                OSEntry(), OSIrq(), OSTask(),
                // IRQ
                IRPrint(), IRRemote(), IRTask(), IRSignal(),
                // TASK
                TKSystem(), TKUtil(), TKUI(), TKNet(), TKStore(), TKProc(),
                // UI
                UIMain(), UIClock(), UIHitokoto(), UIMonitor(),
                // USER
                UserMain(),
                // USER ROUTINE
                URShell(), UREcho(), URPipe(), URDup(), URGrep(), URRange(), URProc(), URTask(), URSleep(), URTime(), URCount(), URTest(), URMsg(), URNews(), URBash(), URReplace(), URUtil(), URAI(), URPC(), URMusic(),
                // USER FILE
                URFileLoad(), URFileSave(), URFileAppend())

        try {
            val code = "import \"sys.base\";\n" +
                    "import \"sys.proc\";\n" +
                    "call g_load_sync_x(\"/kern/entry\");\n"

            interpreter = Interpreter()

            for (page in pages) {
                interpreter!!.load(page)
            }

            val grammar = Grammar(code)
            //System.out.println(grammar.toString());
            val page = grammar.codePage
            //System.out.println(page.toString());
            val baos = ByteArrayOutputStream()
            RuntimeCodePage.exportFromStream(page, baos)
            val bais = ByteArrayInputStream(baos.toByteArray())
            interpreter!!.run("@main", bais)

        } catch (e: RegexException) {
            System.err.println()
            System.err.println(e.position.toString() + "," + e.message)
            e.printStackTrace()
        } catch (e: SyntaxException) {
            System.err.println()
            System.err.println(String.format("模块名：%s. 位置：%s. 错误：%s-%s(%s:%d)",
                    e.pageName, e.position, e.message,
                    e.info, e.fileName, e.position.line + 1))
            e.printStackTrace()
        } catch (e: RuntimeException) {
            System.err.println()
            System.err.println(e.error!!.message + " " + e.position + ": " + e.info)
            e.printStackTrace()
        } catch (e: Exception) {
            System.err.println()
            System.err.println(e.message)
            e.printStackTrace()
        }

    }

    fun showDelay() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                while (System.getProperty("os.desc") != null) {
                    if (System.getProperty("os.desc").startsWith("Windows")) {
                        val hwnd = User32.INSTANCE.FindWindow("SunAwtFrame", mainWndTitle)
                        if (hwnd != null) {
                            User32.INSTANCE.SetForegroundWindow(hwnd)
                            User32.INSTANCE.SetFocus(hwnd)
                            return
                        }
                    }
                    try {
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                }
                isAlwaysOnTop = true
            }
        }, 1000)
    }

    companion object {

        private val logger = Logger.getLogger("window")
        private val mainWndTitle = "jMiniLang Command Window"

        private fun initGlobalFont() {
            val fontUIResource = FontUIResource(Font("楷体", Font.PLAIN, 18))
            val keys = UIManager.getDefaults().keys()
            while (keys.hasMoreElements()) {
                val key = keys.nextElement()
                val value = UIManager.get(key)
                if (value is FontUIResource) {
                    UIManager.put(key, fontUIResource)
                }
            }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val frame = UIMainFrame()
            ModuleRemote.enabled()
            ModuleRemote.setMainFrame(frame)
            frame.setTimer()
            frame.startOS()
            frame.exit()
        }
    }
}
