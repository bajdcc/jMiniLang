package com.bajdcc.LALR1.interpret.os.ui

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【界面】入口
 *
 * @author bajdcc
 */
class UIMain : IOSCodePage {
    override val name: String
        get() = "/ui/main"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
