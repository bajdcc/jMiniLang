package com.bajdcc.LALR1.ui.user

import com.bajdcc.LALR1.ui.UIRemotePanel
import com.sun.jna.platform.win32.User32
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.Timer

/**
 * 【用户界面】用户界面
 *
 * @author bajdcc
 */
class UIUserWindow(defTitle: String = defWndTitle,
                   defSize: Dimension = Dimension(UIRemotePanel.w, UIRemotePanel.h)) : JFrame() {
    private val panel: UIUserPanel = UIUserPanel(defSize)

    val graphics: UIUserGraphics
        get() = this.panel.graphics

    init {
        this.title = defTitle
        this.defaultCloseOperation = JFrame.DO_NOTHING_ON_CLOSE
        this.contentPane = panel
        if (System.getProperty("os.name")?.startsWith("Windows") == true) {
            val f = User32.INSTANCE.GetSystemMetrics(User32.SM_CXFRAME)
            val cap = User32.INSTANCE.GetSystemMetrics(User32.SM_CYCAPTION)
            this.preferredSize = Dimension(defSize.width + f, defSize.height + cap + f)
        } else {
            this.preferredSize = defSize
        }
        this.pack()
        this.setLocationRelativeTo(null)
        this.isResizable = false
        this.isVisible = true
        this.setTimer()
    }

    private fun setTimer() {
        Timer(33) { panel.repaint() }.start()
    }

    companion object {
        private const val defWndTitle = "User Window - jMiniLang"
    }
}
