package com.bajdcc.LALR1.interpret.os.user.routine

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【用户态】计数
 *
 * @author bajdcc
 */
class URCount : IOSCodePage {
    override val name: String
        get() = "/usr/p/count"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
