package com.bajdcc.LALR1.ui.drawing

import java.awt.*
import java.awt.image.BufferedImage
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.regex.Pattern

/**
 * 【界面】远程显示屏
 *
 * @author bajdcc
 */
class UIRemoteGraphics(width: Int, height: Int) {
    private var x: Int = 0
    private var y: Int = 0
    private var old_x: Int = 0
    private var old_y: Int = 0
    private var lineWidth: Int = 0
    private var svgmode: Boolean = false
    private var stringmode: Boolean = false
    private val sb: StringBuilder
    private val queue: Queue<Char>
    private val image: Image
    private val gimage: Graphics
    private val bg: Color
    private val fg: Color
    private val cache: StringBuilder

    init {
        this.x = 0
        this.y = 0
        this.old_x = 0
        this.old_y = 0
        this.lineWidth = 9999
        this.svgmode = false
        this.stringmode = false
        this.queue = LinkedBlockingQueue()
        this.cache = StringBuilder(1024)
        this.sb = StringBuilder(1024)
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
        val cmd_c = '\uffee'
        val cmd_l = '\uffed'
        val cmd_ml = '\uffec'
        while (true) {
            val c = this.queue.poll()
            if (c == null) {
                g.drawImage(image, 0, 0, null)
                return
            }
            if (c == '`')
                break
            cache.append(c)
        }
        val cmd = cache.toString()
        for (i in 0 until cmd.length) {
            val c = cmd[i]
            if (c == cmd_c && !this.stringmode) {
                if (this.svgmode) {
                    drawSVGPath(sb.toString())
                } else {
                    sb.delete(0, sb.length)
                }
                this.svgmode = !this.svgmode
            } else if ((c == cmd_l || c == cmd_ml) && !this.svgmode) {
                if (this.stringmode) {
                    if (c == cmd_l)
                        drawString(sb.toString())
                    else
                        drawStringMultiLine(sb.toString())
                } else {
                    sb.delete(0, sb.length)
                }
                this.stringmode = !this.stringmode
            } else {
                if (this.svgmode || this.stringmode) {
                    sb.append(c)
                }
            }
        }
        cache.delete(0, cache.length)
        g.drawImage(image, 0, 0, null)
    }

    private fun drawString(s: String) {
        this.gimage.drawString(s, x, y)
    }

    private fun drawStringMultiLine(text: String) {
        val m = this.gimage.fontMetrics
        drawStringMultiLine(text, m, x, y)
    }

    private fun drawStringMultiLine(text: String, m: FontMetrics, x: Int, y: Int) {
        var y1 = y
        var words = text.split("\r?\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (words.size > 1) {
            for (word in words) {
                drawStringMultiLine(word, m, x, y1)
                y1 += m.height
            }
        } else if (m.stringWidth(text) < lineWidth) {
            this.gimage.drawString(text, x, y1)
        } else {
            words = text.split("".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val currentLine = StringBuilder(128)
            currentLine.append(words[0])
            for (i in 1 until words.size) {
                if (m.stringWidth(currentLine.toString() + words[i]) < lineWidth) {
                    currentLine.append(words[i])
                } else {
                    this.gimage.drawString(currentLine.toString(), x, y1)
                    y1 += m.height
                    currentLine.delete(0, currentLine.length)
                    currentLine.append(words[i])
                }
            }
            if (currentLine.toString().trim { it <= ' ' }.length > 0) {
                this.gimage.drawString(currentLine.toString(), x, y1)
            }
        }
    }

    private fun drawSVGPath(s: String) {
        val m = pat.matcher(s)
        if (m.find()) {
            val arg1 = m.group(2)
            val arg2 = m.group(3)
            old_x = x
            old_y = y
            when (m.group(1)[0]) {
                'M' -> {
                    x = tryParse(arg1)!!
                    y = tryParse(arg2)!!
                }
                'm' -> {
                    x += tryParse(arg1)!!
                    y += tryParse(arg2)!!
                }
                'L' -> {
                    x = tryParse(arg1)!!
                    y = tryParse(arg2)!!
                    drawLine(old_x, old_y, x, y)
                }
                'l' -> {
                    x += tryParse(arg1)!!
                    y += tryParse(arg2)!!
                    drawLine(old_x, old_y, x, y)
                }
                'R' -> {
                    x = tryParse(arg1)!!
                    y = tryParse(arg2)!!
                    clear(old_x, old_y, x, y)
                }
                'r' -> {
                    x += tryParse(arg1)!!
                    y += tryParse(arg2)!!
                    clear(old_x, old_y, x, y)
                }
                'W' -> lineWidth = tryParse(arg1)!!
            }
        }
    }

    private fun drawLine(x1: Int, y1: Int, x2: Int, y2: Int) {
        this.gimage.drawLine(x1, y1, x2, y2)
    }

    fun clear(x1: Int, y1: Int, x2: Int, y2: Int) {
        val c = this.gimage.color
        this.gimage.color = Color.white
        this.gimage.fillRect(x1, y1, x2 - x1, y2 - y1)
        this.gimage.color = c
    }

    fun drawText(c: Char) {
        this.queue.add(c)
    }

    companion object {
        private val pat = Pattern.compile("(\\w)\\s*([0-9-]+)?\\s*([0-9-]+)?")

        private fun tryParse(str: String): Int? {
            var retVal: Int?
            try {
                retVal = Integer.parseInt(str)
            } catch (nfe: NumberFormatException) {
                retVal = 0
            }

            return retVal
        }
    }
}
