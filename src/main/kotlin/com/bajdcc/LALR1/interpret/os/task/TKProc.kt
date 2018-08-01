package com.bajdcc.LALR1.interpret.os.task

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【服务】进程管理
 *
 * @author bajdcc
 */
class TKProc : IOSCodePage {
    override val name: String
        get() = "/task/proc"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
