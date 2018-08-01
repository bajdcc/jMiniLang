package com.bajdcc.LALR1.ui

import com.bajdcc.LALR1.interpret.module.ModuleRemote
import com.bajdcc.LALR1.ui.drawing.UIRemoteGraphics
import java.awt.Dimension
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.Timer

/**
 * 【界面】远程用户界面
 *
 * @author bajdcc
 */
class UIRemoteWindow : JFrame() {
    private val panel: UIRemotePanel = UIRemotePanel()

    val uiGraphics: UIRemoteGraphics
        get() = this.panel.uiGraphics

    init {
        this.title = remoteWndTitle
        this.defaultCloseOperation = JFrame.DO_NOTHING_ON_CLOSE
        this.preferredSize = Dimension(UIRemotePanel.w, UIRemotePanel.h)
        this.contentPane = panel
        this.pack()
        this.setLocationRelativeTo(null)
        this.isResizable = false
        this.isVisible = true
        this.setTimer()
        ModuleRemote.showMainFrame()
    }

    private fun close() {
        this.dispatchEvent(WindowEvent(this, WindowEvent.WINDOW_CLOSING))
    }

    private fun setTimer() {
        Timer(33) { panel.repaint() }.start()
    }

    companion object {
        private const val remoteWndTitle = "jMiniOS Remote Window"
    }
}
