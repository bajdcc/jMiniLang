package com.bajdcc.LALR1.interpret.os.task

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【服务】时间
 *
 * @author bajdcc
 */
class TKSystem : IOSCodePage {
    override val name: String
        get() = "/task/system"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
