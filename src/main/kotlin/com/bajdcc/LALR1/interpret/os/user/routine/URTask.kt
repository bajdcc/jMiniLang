package com.bajdcc.LALR1.interpret.os.user.routine

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【用户态】服务
 *
 * @author bajdcc
 */
class URTask : IOSCodePage {
    override val name: String
        get() = "/usr/p/task"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
