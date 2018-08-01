package com.bajdcc.LALR1.interpret.os.user.routine

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【用户态】行为树示例：计算机网络模拟
 *
 * @author bajdcc
 */
class URPC : IOSCodePage {
    override val name: String
        get() = "/usr/p/pc"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
