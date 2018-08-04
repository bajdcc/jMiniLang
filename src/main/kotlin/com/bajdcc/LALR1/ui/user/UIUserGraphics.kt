package com.bajdcc.LALR1.ui.user

import java.awt.*
import java.awt.image.BufferedImage
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

/**
 * 【用户界面】图形
 *
 * @author bajdcc
 */
class UIUserGraphics(width: Int, height: Int) {
    private var x: Int = 0
    private var y: Int = 0
    private var oldX: Int = 0
    private var oldY: Int = 0
    private val queue: Queue<SVGInst>
    private val image: Image
    private val gimage: Graphics
    private val bg: Color
    private val fg: Color

    data class SVGInst(val op: Char, val x: Int, val y: Int)

    init {
        this.queue = LinkedBlockingQueue(1024)
        this.bg = Color.white
        this.fg = Color.black
        this.image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        this.gimage = this.image.graphics
        this.gimage.color = bg
        this.gimage.fillRect(0, 0, width, height)
        this.gimage.color = fg
        this.gimage.font = Font("楷体", Font.PLAIN, 20)
        val g2d = gimage as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT)
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT)
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 140)
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF)
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT)
    }

    fun paint(g: Graphics2D) {
        while (queue.isNotEmpty()) {
            drawSVGPath(this.queue.poll()!!)
        }
        g.drawImage(image, 0, 0, null)
    }

    private fun drawSVGPath(inst: SVGInst) {
        oldX = x
        oldY = y
        when (inst.op) {
            'M' -> {
                x = inst.x
                y = inst.y
            }
            'm' -> {
                x += inst.x
                y += inst.y
            }
            'L' -> {
                x = inst.x
                y = inst.y
                drawLine(oldX, oldY, x, y)
            }
            'l' -> {
                x += inst.x
                y += inst.y
                drawLine(oldX, oldY, x, y)
            }
            'R' -> {
                x = inst.x
                y = inst.y
                clear(oldX, oldY, x, y)
            }
            'r' -> {
                x += inst.x
                y += inst.y
                clear(oldX, oldY, x, y)
            }
        }
    }

    private fun drawLine(x1: Int, y1: Int, x2: Int, y2: Int) {
        this.gimage.drawLine(x1, y1, x2, y2)
    }
    
    private fun clear(x1: Int, y1: Int, x2: Int, y2: Int) {
        val c = this.gimage.color
        this.gimage.color = Color.white
        this.gimage.fillRect(x1, y1, x2 - x1, y2 - y1)
        this.gimage.color = c
    }

    fun addSVGInst(inst: SVGInst) {
        this.queue.add(inst)
    }
}
