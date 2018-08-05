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
    private var w: Int = 0
    private var h: Int = 0
    private var oldX: Int = 0
    private var oldY: Int = 0
    private val queue: Queue<Inst>
    private val image: Image
    private val gimage: Graphics
    private val bg: Color
    private val fg: Color

    data class Inst(val svg: SVGInst? = null, val str: StrInst? = null)
    data class SVGInst(val op: Char, val x: Int, val y: Int)
    data class StrInst(val op: Int, val str: String)

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
        w = this.gimage.fontMetrics.widths.max()!!
        h = this.gimage.fontMetrics.height
    }

    fun paint(g: Graphics2D) {
        while (queue.isNotEmpty()) {
            val i = this.queue.poll()!!
            if (i.svg != null)
                drawSVGPath(i.svg)
            else if (i.str != null)
                drawString(i.str)
        }
        g.drawImage(image, 0, 0, null)
    }

    private fun drawSVGPath(inst: SVGInst) {
        when (inst.op) {
            'M' -> {
                oldX = x
                oldY = y
                x = inst.x
                y = inst.y
            }
            'm' -> {
                oldX = x
                oldY = y
                x += inst.x
                y += inst.y
            }
            'L' -> {
                oldX = x
                oldY = y
                x = inst.x
                y = inst.y
                drawLine(oldX, oldY, x, y)
            }
            'l' -> {
                oldX = x
                oldY = y
                x += inst.x
                y += inst.y
                drawLine(oldX, oldY, x, y)
            }
            'R' -> {
                oldX = x
                oldY = y
                x = inst.x
                y = inst.y
                clear(oldX, oldY, x, y)
            }
            'r' -> {
                oldX = x
                oldY = y
                x += inst.x
                y += inst.y
                clear(oldX, oldY, x, y)
            }
            'S' -> {
                w = inst.x
                h = inst.y
            }
        }
    }

    private fun drawString(inst: StrInst) {
        val m = this.gimage.fontMetrics
        when (inst.op) {
            0 -> this.gimage.drawString(inst.str, x, y + m.height)
            1 -> drawMultiline(inst.str, m, x, y + m.height)
        }
    }

    private fun drawMultiline(str: String, m: FontMetrics, x: Int, y: Int) {
        if (str.isEmpty())
            return
        val re = "\r?\n".toRegex()
        var y1 = y
        val words = str.split(re)
        if (words.size > 1) {
            for (word in words) {
                drawMultiline(word, m, x, y1)
                y1 += h
            }
        } else if (m.stringWidth(str) < w) {
            this.gimage.drawString(str, x, y1)
        } else {
            var start = 0
            var end = 0
            for (i in 1 until str.length) {
                val s = str.slice(start..end)
                end++
                if (m.stringWidth(s) < w) {
                } else {
                    this.gimage.drawString(s, x, y1)
                    y1 += m.height
                    start = end
                }
            }
            if (end < str.length) {
                this.gimage.drawString(str.substring(start..end), x, y1)
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
        this.queue.add(Inst(svg = inst))
    }

    fun addStrInst(inst: StrInst) {
        this.queue.add(Inst(str = inst))
    }
}
