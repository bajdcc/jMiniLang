package com.bajdcc.web

import com.bajdcc.LALR1.grammar.runtime.RuntimeProcess
import com.bajdcc.LALR1.interpret.module.ModuleNet
import com.bajdcc.LALR1.interpret.module.user.ModuleUserBase
import com.bajdcc.web.bean.SpringBeanExec
import org.apache.commons.text.StringEscapeUtils
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

/**
 * 【Web服务】API接口
 *
 * @author bajdcc
 */
@RestController
@RequestMapping("/api")
class SpringApiController {
    var execId = 0

    @RequestMapping(value = ["/query/{item}"], method = [(RequestMethod.GET)], produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
    fun queryItem(@PathVariable(value = "item") item: String): Any {
        val obj = ModuleNet.getInstance().webApi.sendRequest("query/$item")
        return if (obj != null)
            hashMapOf("code" to 200, "data" to obj)
        else
            hashMapOf("code" to 404)
    }

    @RequestMapping(value = ["/md/{item}"], method = [(RequestMethod.GET)], produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
    fun getMarkdown(@PathVariable(value = "item") item: String): Any {
        val obj = ModuleNet.getInstance().webApi.sendRequest("md/$item")
        return if (obj != null)
            hashMapOf("code" to 200, "data" to obj)
        else
            hashMapOf("code" to 404)
    }

    @RequestMapping(value = ["/vfs"], params = arrayOf("path"), method = [(RequestMethod.GET)], produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
    fun getVfs(@RequestParam("path") path: String): Any {
        val obj = ModuleNet.getInstance().webApi.sendRequest(
                "vfs/" + StringEscapeUtils.unescapeHtml4(path).replace('/', '_'))
        return if (obj != null)
            hashMapOf("code" to 200, "data" to obj)
        else
            hashMapOf("code" to 404)
    }

    @RequestMapping(value = ["/fs"], params = ["path"], method = [(RequestMethod.GET)], produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
    fun getVfsText(@RequestParam("path") path: String): Any {
        val obj = ModuleNet.getInstance().webApi.sendRequest(
                "fs/" + StringEscapeUtils.unescapeHtml4(path).replace('/', '_'))
        return if (obj != null)
            hashMapOf("code" to 200, "data" to obj)
        else
            hashMapOf("code" to 404)
    }

    @RequestMapping(value = ["/exec"], method = [(RequestMethod.POST)], produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
    fun execCode(@RequestBody ctx: SpringBeanExec): Any {
        if (ctx.code != null) {
            RuntimeProcess.writePipe(ModuleUserBase.EXEC_PREFIX + execId, ctx.code)
            execId++
            return hashMapOf("code" to 200, "data" to ModuleNet.getInstance().webApi.sendRequest("exec/$execId"))
        } else {
            return hashMapOf("code" to 404)
        }
    }

    @RequestMapping(value = ["/exec_query"], params = ["id"], method = arrayOf(RequestMethod.GET), produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
    fun execQuery(@RequestParam("id") id: String): Any {
        val obj = ModuleNet.getInstance().webApi.sendRequest("exec_query/$id")
        return if (obj != null)
            hashMapOf("code" to 200, "data" to obj)
        else
            hashMapOf("code" to 404)
    }

    @RequestMapping(value = ["/exec_kill"], params = ["id"], method = [(RequestMethod.GET)], produces = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
    fun execKill(@RequestParam("id") id: String): Any {
        val obj = ModuleNet.getInstance().webApi.sendRequest("exec_kill/$id")
        return if (obj != null)
            hashMapOf("code" to 200, "data" to obj)
        else
            hashMapOf("code" to 404)
    }
}
