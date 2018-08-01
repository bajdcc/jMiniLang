package com.bajdcc.LALR1.interpret.os.ui

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【界面】时钟
 *
 * @author bajdcc
 */
class UIClock : IOSCodePage {
    override val name: String
        get() = "/ui/clock"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
