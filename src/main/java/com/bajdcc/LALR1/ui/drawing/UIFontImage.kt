package com.bajdcc.LALR1.ui.drawing

import org.apache.log4j.Logger
import java.awt.*
import java.awt.image.BufferedImage
import java.util.*

/**
 * 【界面】点阵文字位图
 *
 * @author bajdcc
 */
class UIFontImage(private val width: Int, private val height: Int) {
    private val imagesASCII = arrayOfNulls<BufferedImage?>(256)
    private val imagesUnicode: MutableMap<Int, Image>
    private var fgcolor: Color? = null
    private var bgcolor: Color? = null
    private var enableColor: Boolean = false
    private val enableFGColor: Boolean
    private val enableBGColor: Boolean = false

    init {
        this.imagesUnicode = HashMap()
        this.enableColor = false
        this.enableFGColor = false
        this.enableColor = false
        this.fgcolor = Color.black
        this.bgcolor = Color.white
    }

    fun setFGColor(color: Color) {
        this.fgcolor = color
        enableColor = this.fgcolor!!.rgb != -0x1000000 || enableBGColor
    }

    fun setBGColor(color: Color) {
        this.bgcolor = color
        enableColor = this.bgcolor!!.rgb != -0x1 || enableFGColor
    }

    /**
     * 按需生成字符图像，免去数秒的初始化，节省时间
     *
     * @param i 字符的编码
     * @return 是否是原始宽度（不是汉字）
     */
    private fun drawImage(i: Int): Boolean {
        val isWide = isWideChar(i.toChar())
        val w = if (isWide) 2 * width else width
        val bi = BufferedImage(w, height, BufferedImage.TYPE_INT_RGB)
        if (i < 256) {
            this.imagesASCII[i] = bi
        } else {
            this.imagesUnicode[i] = bi
        }
        val g = bi.graphics
        val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT)
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT)
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 140)
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF)
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT)
        g.setColor(Color.white)
        g.fillRect(0, 0, w, height)
        g.setColor(Color.black)
        if (i == 7) {
            g.fillRect(0, 0, w, height)
        } else if (!Character.isISOControl(i.toChar())) {
            if (i < 256) {
                g.setFont(asciiFont)
                val str = Character.toString(i.toChar())
                val x = w / 2 - g.getFontMetrics().stringWidth(str) / 2
                val y = height / 2 + g.getFontMetrics().height / 3
                g.drawString(str, x, y)
            } else {
                g.setFont(unicodeFont)
                val str = Character.toString(i.toChar())
                val x = w / 2 - g.getFontMetrics().stringWidth(str) / 2
                val y = height / 2 + g.getFontMetrics().height / 3
                g.drawString(str, x, y)
                return true // 假设是中文，比较宽
            }
        }
        return true
    }

    private fun drawImageWithColor(i: Int): Image {
        val isWide = isWideChar(i.toChar())
        val w = if (isWide) 2 * width else width
        val bi = BufferedImage(w, height, BufferedImage.TYPE_INT_RGB)
        val g = bi.graphics
        val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT)
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT)
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 140)
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF)
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT)
        g.setColor(bgcolor)
        g.fillRect(0, 0, w, height)
        g.setColor(fgcolor)
        if (!Character.isISOControl(i.toChar())) {
            if (i < 256) {
                g.setFont(asciiFont)
                val str = Character.toString(i.toChar())
                val x = w / 2 - g.getFontMetrics().stringWidth(str) / 2
                val y = height / 2 + g.getFontMetrics().height / 3
                g.drawString(str, x, y)
            } else {
                g.setFont(unicodeFont)
                val str = Character.toString(i.toChar())
                val x = w / 2 - g.getFontMetrics().stringWidth(str) / 2
                val y = height / 2 + g.getFontMetrics().height / 3
                g.drawString(str, x, y)
                return bi
            }
        }
        return bi
    }

    fun getImage(c: Int): Image {
        if (!enableColor) {
            if (c in 0..65535) {
                if (c < 256) {
                    if (this.imagesASCII[c] == null)
                        drawImage(c)
                    return this.imagesASCII[c]!!
                } else {
                    if (this.imagesUnicode[c] == null)
                        drawImage(c)
                    return this.imagesUnicode[c]!!
                }
            }
            return this.imagesASCII[0]!!
        } else {
            return drawImageWithColor(c)
        }
    }

    companion object {

        private val logger = Logger.getLogger("font")

        internal val asciiFont = Font(Font.MONOSPACED, Font.PLAIN, 18)
        internal val unicodeFont = Font("楷体", Font.PLAIN, 18)

        fun isWideChar(c: Char): Boolean {
            return !Character.isISOControl(c) && c >= '\u00ff'
        }

        fun calcWidth(str: String): Int {
            var len = 0
            for (i in 0 until str.length) {
                val c = str[i]
                if (isWideChar(c))
                    len += 2
                else
                    len++
            }
            return len
        }
    }
}
