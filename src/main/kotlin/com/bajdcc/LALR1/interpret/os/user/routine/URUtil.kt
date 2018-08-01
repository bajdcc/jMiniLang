package com.bajdcc.LALR1.interpret.os.user.routine

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【用户态】辅助功能
 *
 * @author bajdcc
 */
class URUtil : IOSCodePage {
    override val name: String
        get() = "/usr/p/util"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
