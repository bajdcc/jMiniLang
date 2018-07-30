package com.bajdcc.LALR1.interpret.os.user.routine

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【用户态】置换
 *
 * @author bajdcc
 */
class URReplace : IOSCodePage {
    override val name: String
        get() = "/usr/p/replace"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
