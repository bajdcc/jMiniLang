package com.bajdcc.LALR1.interpret.os.user.routine

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【用户态】行为树AI
 *
 * @author bajdcc
 */
class URAI : IOSCodePage {
    override val name: String
        get() = "/usr/p/ai"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
