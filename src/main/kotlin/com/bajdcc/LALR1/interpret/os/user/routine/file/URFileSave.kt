package com.bajdcc.LALR1.interpret.os.user.routine.file

import com.bajdcc.LALR1.interpret.os.IOSCodePage
import com.bajdcc.util.ResourceLoader

/**
 * 【用户态】写文件
 *
 * @author bajdcc
 */
class URFileSave : IOSCodePage {
    override val name: String
        get() = "/usr/p/>"

    override val code: String
        get() = ResourceLoader.load(javaClass)
}
