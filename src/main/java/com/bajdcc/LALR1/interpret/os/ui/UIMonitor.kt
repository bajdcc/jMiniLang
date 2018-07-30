package com.bajdcc.LALR1.interpret.os.ui

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【界面】监视器
 *
 * @author bajdcc
 */
class UIMonitor : IOSCodePage {
    override val name: String
        get() = "/ui/monitor"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
