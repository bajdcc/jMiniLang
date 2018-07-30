package com.bajdcc.LALR1.interpret.os.user.routine

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【用户态】测试
 *
 * @author bajdcc
 */
class URTest : IOSCodePage {
    override val name: String
        get() = "/usr/p/test"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
