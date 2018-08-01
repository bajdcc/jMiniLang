package com.bajdcc.LALR1.interpret.os.irq

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【中断】文字输出
 *
 * @author bajdcc
 */
class IRPrint : IOSCodePage {
    override val name: String
        get() = "/irq/print"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
