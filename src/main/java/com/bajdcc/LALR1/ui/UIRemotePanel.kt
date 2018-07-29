package com.bajdcc.LALR1.ui

import com.bajdcc.LALR1.ui.drawing.UIRemoteGraphics
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel

/**
 * 【界面】远程渲染界面
 *
 * @author bajdcc
 */
class UIRemotePanel : JPanel() {

    val uiGraphics: UIRemoteGraphics

    init {
        this.uiGraphics = UIRemoteGraphics(w, h)
        this.isFocusable = false
    }

    override fun paint(g: Graphics) {
        uiGraphics.paint(g as Graphics2D)
    }

    companion object {

        var w = 890
        var h = 655
    }
}
