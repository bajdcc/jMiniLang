package com.bajdcc.LALR1.interpret.os.user.routine

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【用户态】时间
 *
 * @author bajdcc
 */
class URTime : IOSCodePage {
    override val name: String
        get() = "/usr/p/time"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
