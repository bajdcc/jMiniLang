package com.bajdcc.LALR1.interpret.os.user.routine

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【用户态】新闻
 *
 * @author bajdcc
 */
class URNews : IOSCodePage {
    override val name: String
        get() = "/usr/p/news"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
