package com.bajdcc.LALR1.interpret.os.user.routine

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【用户态】打印输出
 *
 * @author bajdcc
 */
class UREcho : IOSCodePage {
    override val name: String
        get() = "/usr/p/echo"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
