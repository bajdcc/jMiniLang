package com.bajdcc.LALR1.interpret.os.user.routine

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【用户态】休眠
 *
 * @author bajdcc
 */
class URSleep : IOSCodePage {
    override val name: String
        get() = "/usr/p/sleep"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
