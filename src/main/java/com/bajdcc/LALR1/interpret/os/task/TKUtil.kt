package com.bajdcc.LALR1.interpret.os.task

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【服务】工具
 *
 * @author bajdcc
 */
class TKUtil : IOSCodePage {
    override val name: String
        get() = "/task/util"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
