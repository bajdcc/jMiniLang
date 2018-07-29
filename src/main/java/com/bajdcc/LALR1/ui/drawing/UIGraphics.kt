package com.bajdcc.LALR1.ui.drawing

import java.awt.Color
import java.awt.Graphics2D
import java.awt.Image
import java.awt.image.BufferedImage
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

/**
 * 【界面】显示屏
 *
 * @author bajdcc
 */
class UIGraphics(val w: Int, val h: Int, val cols: Int, val rows: Int, width: Int, height: Int, private val zoom: Int) {
    private val width: Int = width * zoom
    private val height: Int = height * zoom
    private val size: Int = cols * rows
    private val data: CharArray = CharArray(cols * rows)
    private var ptrX: Int = 0
    private var ptrY: Int = 0
    private var ptrMx: Int = 0
    private var ptrMy: Int = 0
    private val queue: Queue<Char>
    private val fontImage: UIFontImage
    private val image: Image
    private var caret: Boolean = false
    private var caretPrev: Boolean = false
    private var caretState: Boolean = false
    private var caretTime: Int = 0
    private var stateColor: Int = 0
    private var countColor: Int = 0
    private val colors: IntArray
    private var autoFresh: Boolean = false

    val isHideCaret: Boolean
        get() = !this.caretState && !this.caret

    init {
        this.ptrX = 0
        this.ptrY = 0
        this.ptrMx = 0
        this.ptrMy = 0
        this.queue = LinkedBlockingQueue(MAX_QUEUE_SIZE)
        this.fontImage = UIFontImage(this.width, this.height)
        this.image = BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)
        this.image.graphics.color = Color.white
        this.image.graphics.fillRect(0, 0, w, h)
        this.stateColor = 0
        this.countColor = 0
        this.colors = IntArray(3)
        this.autoFresh = true
    }

    fun paint(g: Graphics2D) {
        var len = 0
        while (true) {
            var c = this.queue.poll() ?: break
            if (stateColor != 0 && countColor < 3) {
                markColor(c)
                continue
            }
            if (c == '\uffef') {
                markInput()
            } else if (c == '\u000c') {
                clear(g)
            } else if (c == '\uffe1' || c == '\uffe2') {
                if (c == '\uffe1')
                    autoFresh = !autoFresh
                else
                    refresh(g)
            } else if (c == '\uffd2' || c == '\uffd3') {
                stateColor = if (c == '\uffd2') 1 else 2
                countColor = 0
            } else {
                if (c == '\t')
                    c = ' '
                draw(g, c)
                len++
            }
        }
        if (len > 0 && caretTime > 0) {
            caretTime = 0
        }
        if (caret != caretPrev) {
            if (caretPrev && caretState) {
                hideCaret()
                caretTime = 0
                caretState = false
            }
            caretPrev = caret
        } else if (caret) {
            if (caretState) {
                showCaret(g)
            } else {
                hideCaret()
            }
            if (caretTime++ >= CARET_TIME) {
                caretState = !caretState
                caretTime = 0
            }
        }
        g.drawImage(image, 0, 0, null)
    }

    private fun drawText(row: Int, col: Int, text: String) {
        setFGColor(230, 230, 230)
        setBGColor(25, 25, 25)
        for (i in 0 until text.length) {
            image.graphics.drawImage(fontImage.getImage(text[i].toInt()),
                    (col + i) * width, row * height, null)
        }
        setFGColor(0, 0, 0)
        setBGColor(255, 255, 255)
    }

    private fun refresh(g: Graphics2D) {
        image.graphics.color = Color.white
        image.graphics.fillRect(0, 0, w, h)
        g.drawImage(image, 0, 0, null)
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val c = this.data[i * cols + j]
                if (c != FILL_FONT_SPAN) {
                    image.graphics.drawImage(fontImage.getImage(c.toInt()),
                            j * width, i * height, null)
                }
            }
        }
        val logo = "--== jMiniOS made by bajdcc ==--"
        drawText(0, (cols - logo.length) / 2, logo)
        this.ptrX = 0
        this.ptrY = 0
        for (i in 0 until size) {
            this.data[i] = '\u0000'
        }
    }

    private fun markColor(c: Char) {
        colors[countColor++] = c.toInt() and 255
        if (countColor >= 3) {
            if (stateColor == 1)
                setFGColor(colors[0], colors[1], colors[2])
            else
                setBGColor(colors[0], colors[1], colors[2])
            stateColor = 0
        }
    }

    private fun showCaret(g: Graphics2D) {
        if (ptrX == cols) {
            ptrX = 0
            if (ptrY == rows) {
                clear(g)
            } else {
                ptrY++
            }
        }
        drawChar('_')
    }

    private fun hideCaret() {
        drawChar('\u0000')
    }

    private fun draw(g: Graphics2D, c: Char) {
        drawIntern(g, c)
        if (ptrX > 0 && UIFontImage.isWideChar(c)) {
            drawIntern(g, FILL_FONT_SPAN)
        }
    }

    private fun drawIntern(g: Graphics2D, c: Char) {
        if (c == '\n') {
            if (ptrY == rows - 1) {
                newline(g)
            } else {
                ptrX = 0
                ptrY++
            }
        } else if (c == '\b') {
            if (ptrMx + ptrMy * cols < ptrX + ptrY * cols) {
                if (ptrY == 0) {
                    if (ptrX != 0) {
                        drawChar('\u0000')
                        ptrX--
                    }
                } else {
                    if (ptrX != 0) {
                        drawChar('\u0000')
                        ptrX--
                    } else {
                        drawChar('\u0000')
                        ptrX = cols - 1
                        ptrY--
                    }
                }
            }
        } else if (c == '\u0002') {
            ptrX--
            while (ptrX >= 0) {
                drawChar('\u0000')
                ptrX--
            }
            ptrX = 0
        } else if (c == '\r') {
            ptrX = 0
        } else if (ptrX == cols - 1) {
            if (ptrY == rows - 1) {
                drawChar(c)
                if (autoFresh)
                    newline(g)
            } else {
                drawChar(c)
                ptrX = 0
                ptrY++
            }
        } else {
            drawChar(c)
            ptrX++
        }
    }

    private fun drawChar(c: Char) {
        this.data[ptrY * cols + ptrX] = c
        if (autoFresh && c != FILL_FONT_SPAN) {
            image.graphics.drawImage(fontImage.getImage(c.toInt()),
                    ptrX * width, ptrY * height, null)
        }
    }

    fun clear(g: Graphics2D) {
        this.ptrX = 0
        this.ptrY = 0
        for (i in 0 until size) {
            this.data[i] = '\u0000'
        }
        image.graphics.color = Color.white
        image.graphics.fillRect(0, 0, w, h)
        g.drawImage(image, 0, 0, null)
    }

    private fun newline(g: Graphics2D) {
        this.ptrX = 0
        val end = size - cols
        System.arraycopy(data, cols, data, 0, size - cols)
        Arrays.fill(data, end, size - 1, '\u0000')
        image.graphics.copyArea(0, height, w, (rows - 1) * height, 0, -height)
        image.graphics.color = Color.white
        image.graphics.fillRect(0, (rows - 1) * height, w, height)
        g.drawImage(image, 0, 0, null)
    }

    fun drawText(c: Char): Boolean {
        if (this.queue.size == MAX_QUEUE_SIZE) { // 满了
            return true
        }
        this.queue.add(c)
        return false
    }

    fun setCaret(caret: Boolean) {
        if (this.caret != caret)
            this.caret = caret
    }

    private fun markInput() {
        ptrMx = ptrX
        ptrMy = ptrY
    }

    fun fallback() {
        var x = ptrX
        var y = ptrY
        while (ptrMx + ptrMy * cols < x + y * cols) {
            if (x == 0) {
                x = cols - 1
                y--
            }
            this.data[y * cols + x] = '\u0000'
            image.graphics.drawImage(fontImage.getImage(0),
                    x * width, y * height, null)
            x--
        }
        ptrX = x
        ptrY = y
    }

    fun calcWidth(str: String): Int {
        return UIFontImage.calcWidth(str)
    }

    private fun setFGColor(r: Int, g: Int, b: Int) {
        fontImage.setFGColor(Color(r, g, b))
    }

    private fun setBGColor(r: Int, g: Int, b: Int) {
        fontImage.setBGColor(Color(r, g, b))
    }

    companion object {

        private const val CARET_TIME = 20
        private const val MAX_QUEUE_SIZE = 2000
        private const val FILL_FONT_SPAN = '\uffea'
    }
}

