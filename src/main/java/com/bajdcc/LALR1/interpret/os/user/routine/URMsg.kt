package com.bajdcc.LALR1.interpret.os.user.routine

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【用户态】通讯
 *
 * @author bajdcc
 */
class URMsg : IOSCodePage {
    override val name: String
        get() = "/usr/p/msg"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
