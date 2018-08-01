package com.bajdcc.LALR1.interpret.os.user.routine

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【用户态】复制流
 *
 * @author bajdcc
 */
class URDup : IOSCodePage {
    override val name: String
        get() = "/usr/p/dup"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
