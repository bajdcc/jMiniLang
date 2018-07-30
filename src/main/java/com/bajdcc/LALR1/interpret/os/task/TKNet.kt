package com.bajdcc.LALR1.interpret.os.task

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【服务】网络
 *
 * @author bajdcc
 */
class TKNet : IOSCodePage {
    override val name: String
        get() = "/task/net"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
