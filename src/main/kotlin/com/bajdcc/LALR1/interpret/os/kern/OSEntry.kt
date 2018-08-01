package com.bajdcc.LALR1.interpret.os.kern

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【内核】入口
 *
 * @author bajdcc
 */
class OSEntry : IOSCodePage {

    override val name: String
        get() = "/kern/entry"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
