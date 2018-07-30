package com.bajdcc.LALR1.interpret.os.user.routine

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【用户态】管道
 *
 * @author bajdcc
 */
class URPipe : IOSCodePage {
    override val name: String
        get() = "/usr/p/pipe"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
