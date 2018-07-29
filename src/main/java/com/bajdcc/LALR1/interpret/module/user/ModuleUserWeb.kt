package com.bajdcc.LALR1.interpret.module.user

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.*
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeMap
import com.bajdcc.LALR1.interpret.module.IInterpreterModule
import com.bajdcc.LALR1.interpret.module.ModuleNet
import com.bajdcc.LALR1.interpret.module.api.ModuleNetWebApiContext
import com.bajdcc.LALR1.interpret.module.web.ModuleNetWebContext
import com.bajdcc.util.ResourceLoader
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.parser.ParserEmulationProfile
import com.vladsch.flexmark.util.options.MutableDataSet

/**
 * 【模块】用户态-网页服务器
 *
 * @author bajdcc
 */
class ModuleUserWeb : IInterpreterModule {
    private var runtimeCodePage: RuntimeCodePage? = null
    private var renderer: HtmlRenderer? = null
    private var parser: Parser? = null

    override val moduleName: String
        get() = "user.web"

    override val moduleCode: String
        get() = ResourceLoader.load(javaClass)

    override val codePage: RuntimeCodePage
        @Throws(Exception::class)
        get() {
            if (runtimeCodePage != null)
                return runtimeCodePage!!

            val base = ResourceLoader.load(javaClass)

            val grammar = Grammar(base)
            val page = grammar.codePage
            val info = page.info

            info.addExternalFunc("g_web_get_context",
                    RuntimeDebugExec("获取请求上下文")
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            val server = ModuleNet.instance.webServer
                            if (server != null) {
                                val ctx = server.dequeue()
                                if (ctx != null) {
                                    val map = RuntimeMap()
                                    map.put("code", RuntimeObject(ctx.code.toLong()))
                                    map.put("request", RuntimeObject(ctx.reqHeader))
                                    map.put("response", RuntimeObject(ctx.response))
                                    map.put("header", RuntimeObject(ctx.respHeader))
                                    map.put("mime", RuntimeObject(ctx.mime))
                                    map.put("content_type", RuntimeObject(ctx.contentType.toLong()))
                                    map.put("__ctx__", RuntimeObject(ctx))
                                    RuntimeObject(map)
                                } else null
                            } else null
                        }
                    })
            info.addExternalFunc("g_web_set_context",
                    RuntimeDebugExec("设置请求上下文", arrayOf(RuntimeObjectType.kMap))
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            val map = args[0].map
                            val ctx = map["__ctx__"]!!.obj as ModuleNetWebContext
                            ctx.code = map["code"]!!.int
                            ctx.response = map["response"]!!.string
                            ctx.respHeader = map["header"]!!.map
                            ctx.mime = map["mime"]!!.string
                            ctx.contentType = map["content_type"]!!.long.toInt()
                            ctx.unblock()
                            null
                        }
                    })
            info.addExternalFunc("g_web_get_api",
                    RuntimeDebugExec("获取API请求上下文")
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            val api = ModuleNet.instance.webApi
                            if (api != null) {
                                val ctx = api.dequeue()
                                if (ctx != null) {
                                    RuntimeObject(ctx.req)
                                } else null
                            } else null
                        }
                    })
            info.addExternalFunc("g_web_set_api",
                    RuntimeDebugExec("设置API请求上下文", arrayOf(RuntimeObjectType.kMap))
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            val map = args[0].map
                            val ctx = map["__ctx__"]!!.obj as ModuleNetWebApiContext
                            ctx.resp = map["resp"]!!
                            ctx.unblock()
                            null
                        }
                    })
            val options = MutableDataSet()
            options.setFrom(ParserEmulationProfile.MARKDOWN)
            options.set(Parser.EXTENSIONS, listOf(TablesExtension.create()))
            parser = Parser.builder(options).build()
            renderer = HtmlRenderer.builder(options).build()
            info.addExternalFunc("g_web_markdown",
                    RuntimeDebugExec("MD转HTML", arrayOf(RuntimeObjectType.kString))
                    { args: List<RuntimeObject>, status: IRuntimeStatus ->
                        run {
                            val md = args[0].obj.toString()
                            val document = parser!!.parse(md)
                            val html = renderer!!.render(document)
                            RuntimeObject(html)
                        }
                    })

            runtimeCodePage = page
            return page
        }

    companion object {

        val instance = ModuleUserWeb()
    }
}