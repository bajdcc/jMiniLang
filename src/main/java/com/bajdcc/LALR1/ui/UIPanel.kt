package com.bajdcc.LALR1.ui

import com.bajdcc.LALR1.interpret.module.ModuleUI
import com.bajdcc.LALR1.ui.drawing.UIGraphics
import org.apache.log4j.Logger
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JPanel

/**
 * 【界面】渲染界面
 *
 * @author bajdcc
 */
class UIPanel : JPanel() {
    val uiGraphics: UIGraphics
    private val moduleUI: ModuleUI

    init {
        this.uiGraphics = UIGraphics(UIRemotePanel.w, UIRemotePanel.h, 80, 25, 11, 25, 1)
        moduleUI = ModuleUI.instance
        moduleUI.setGraphics(this.uiGraphics)
        moduleUI.setPanel(this)
        this.isFocusable = true
        this.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent?) {
                logger.debug("Key pressed: " + e!!.keyCode + ", " + if (e.isControlDown) KeyEvent.getKeyText(e.keyCode) else e.keyCode.toChar())
                when (e.keyCode) {
                    KeyEvent.VK_UP -> moduleUI.addInputChar('\ufff0')
                    KeyEvent.VK_BACK_SPACE -> moduleUI.addInputChar('\b')
                    else -> if (e.isControlDown) {
                        when (e.keyCode) {
                            KeyEvent.VK_C -> moduleUI.addDisplayChar('\uffee')
                        }
                    } else {
                        moduleUI.addInputChar(e.keyChar)
                    }
                }
            }
        })
    }

    override fun paint(g: Graphics) {
        //super.paint(g);
        uiGraphics.paint(g as Graphics2D)
    }

    companion object {

        private val logger = Logger.getLogger("machine")
    }
}
