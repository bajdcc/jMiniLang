package com.bajdcc.LALR1.interpret.os.user.routine

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【用户态】脚本解释器
 *
 * @author bajdcc
 */
class URShell : IOSCodePage {
    override val name: String
        get() = "/usr/p/sh"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
