package com.bajdcc.LALR1.interpret.os.user.routine

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【用户态】音乐
 *
 * @author bajdcc
 */
class URMusic : IOSCodePage {
    override val name: String
        get() = "/usr/p/music"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
