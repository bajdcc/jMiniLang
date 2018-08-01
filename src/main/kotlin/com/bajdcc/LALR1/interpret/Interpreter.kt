package com.bajdcc.LALR1.interpret

import com.bajdcc.LALR1.grammar.runtime.RuntimeProcess
import com.bajdcc.LALR1.interpret.os.IOSCodePage
import java.io.InputStream

/**
 * 【运行时】扩展/内建虚拟机
 *
 * @author bajdcc
 */
class Interpreter {

    private var rtProcess: RuntimeProcess? = null
    private val arrCodes = mutableMapOf<String, String>()
    private val arrFiles = mutableMapOf<String, String>()

    @Throws(Exception::class)
    fun run(name: String, input: InputStream) {
        rtProcess = RuntimeProcess(name, input, arrFiles)
        for ((key, value) in arrCodes) {
            rtProcess!!.addCodePage(key, value)
        }
        arrCodes.clear()
        rtProcess!!.run()
    }

    /**
     * 添加代码页
     *
     * @param codePage 代码页
     */
    fun load(codePage: IOSCodePage) {
        arrCodes[codePage.name] = codePage.code
        arrFiles[codePage.name] = codePage.javaClass.simpleName
    }

    /**
     * 发送停机命令
     */
    fun stop() {
        rtProcess!!.halt()
    }
}
