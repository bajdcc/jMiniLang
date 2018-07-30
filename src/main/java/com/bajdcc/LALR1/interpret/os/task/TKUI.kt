package com.bajdcc.LALR1.interpret.os.task

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【服务】用户界面
 *
 * @author bajdcc
 */
class TKUI : IOSCodePage {
    override val name: String
        get() = "/task/ui"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
