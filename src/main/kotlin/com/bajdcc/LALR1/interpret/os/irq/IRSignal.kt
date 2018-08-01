package com.bajdcc.LALR1.interpret.os.irq

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【中断】信号
 *
 * @author bajdcc
 */
class IRSignal : IOSCodePage {
    override val name: String
        get() = "/irq/signal"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
