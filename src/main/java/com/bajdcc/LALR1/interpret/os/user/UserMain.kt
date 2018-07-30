package com.bajdcc.LALR1.interpret.os.user

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【用户态】主进程
 *
 * @author bajdcc
 */
class UserMain : IOSCodePage {
    override val name: String
        get() = "/usr/main"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
