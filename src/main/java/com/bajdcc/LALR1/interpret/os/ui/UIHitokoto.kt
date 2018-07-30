package com.bajdcc.LALR1.interpret.os.ui

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【界面】一言
 *
 * @author bajdcc
 */
class UIHitokoto : IOSCodePage {
    override val name: String
        get() = "/ui/hitokoto"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
