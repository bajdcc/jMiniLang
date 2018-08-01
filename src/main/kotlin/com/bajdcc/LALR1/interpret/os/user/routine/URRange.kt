package com.bajdcc.LALR1.interpret.os.user.routine

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【用户态】生成序列
 *
 * @author bajdcc
 */
class URRange : IOSCodePage {
    override val name: String
        get() = "/usr/p/range"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
