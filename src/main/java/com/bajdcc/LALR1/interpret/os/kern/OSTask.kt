package com.bajdcc.LALR1.interpret.os.kern

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【内核】服务
 *
 * @author bajdcc
 */
class OSTask : IOSCodePage {
    override val name: String
        get() = "/kern/task"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
