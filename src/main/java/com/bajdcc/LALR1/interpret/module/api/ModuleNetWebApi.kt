package com.bajdcc.LALR1.interpret.module.api

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject
import com.bajdcc.LALR1.grammar.runtime.RuntimeProcess
import org.apache.log4j.Logger
import java.util.concurrent.ConcurrentLinkedDeque

/**
 * 【模块】网页接口服务
 *
 * @author bajdcc
 */
class ModuleNetWebApi {

    private val queue = ConcurrentLinkedDeque<ModuleNetWebApiContext>()

    fun dequeue(): ModuleNetWebApiContext? {
        return queue.poll()
    }

    fun peekRequest(): RuntimeObject? {
        val ctx = queue.peek() ?: return null
        return RuntimeObject(ctx.req)
    }

    @JvmOverloads
    fun sendRequest(route: String, params: Map<String, String>? = null): Any? {
        val ctx = ModuleNetWebApiContext(route, params)
        queue.add(ctx)
        RuntimeProcess.writePipe("int#3", "A")
        return if (ctx.block()) {
            ModuleNetWebApiHelper.toJsonObject(RuntimeObject(ctx.resp))
        } else null
    }

    companion object {

        private val logger = Logger.getLogger("api")
    }
}
