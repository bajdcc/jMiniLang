package com.bajdcc.LALR1.interpret.os.user.routine.file

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【用户态】追加文件
 *
 * @author bajdcc
 */
class URFileAppend : IOSCodePage {
    override val name: String
        get() = "/usr/p/>>"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
