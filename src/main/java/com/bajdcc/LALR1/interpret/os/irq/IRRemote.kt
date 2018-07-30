package com.bajdcc.LALR1.interpret.os.irq

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【中断】远程用户界面
 *
 * @author bajdcc
 */
class IRRemote : IOSCodePage {
    override val name: String
        get() = "/irq/remote"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
