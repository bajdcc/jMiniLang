package com.bajdcc.LALR1.interpret.os.irq

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【中断】服务
 *
 * @author bajdcc
 */
class IRTask : IOSCodePage {
    override val name: String
        get() = "/irq/task"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
