package com.bajdcc.LALR1.ui.user

import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel

/**
 * 【用户界面】渲染界面
 *
 * @author bajdcc
 */
class UIUserPanel(defSize: Dimension) : JPanel() {

    val graphics = UIUserGraphics(defSize.width, defSize.height)

    override fun paint(g: Graphics) {
        graphics.paint(g as Graphics2D)
    }
}
