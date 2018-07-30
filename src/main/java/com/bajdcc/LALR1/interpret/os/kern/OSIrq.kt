package com.bajdcc.LALR1.interpret.os.kern

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【内核】IRQ中断
 *
 * @author bajdcc
 */
class OSIrq : IOSCodePage {

    override val name: String
        get() = "/kern/irq"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
