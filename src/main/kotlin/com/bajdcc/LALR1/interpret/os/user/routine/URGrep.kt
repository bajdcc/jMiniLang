package com.bajdcc.LALR1.interpret.os.user.routine

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【用户态】过滤流
 *
 * @author bajdcc
 */
class URGrep : IOSCodePage {
    override val name: String
        get() = "/usr/p/grep"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
