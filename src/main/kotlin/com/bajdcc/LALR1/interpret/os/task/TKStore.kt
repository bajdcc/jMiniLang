package com.bajdcc.LALR1.interpret.os.task

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【服务】全局存储
 *
 * @author bajdcc
 */
class TKStore : IOSCodePage {
    override val name: String
        get() = "/task/store"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
