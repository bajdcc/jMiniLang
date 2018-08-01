package com.bajdcc.LALR1.interpret.module

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.*
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeMap
import com.bajdcc.LALR1.interpret.module.api.ModuleNetWebApi
import com.bajdcc.LALR1.interpret.module.net.ModuleNetClient
import com.bajdcc.LALR1.interpret.module.net.ModuleNetServer
import com.bajdcc.LALR1.interpret.module.web.ModuleNetWebServer
import com.bajdcc.util.ResourceLoader
import com.bajdcc.web.SpringBootstrap
import org.apache.log4j.Logger
import org.dom4j.Node
import org.dom4j.io.SAXReader
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URL
import java.net.UnknownHostException
import java.util.regex.Pattern

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
/**
 * 【模块】网络
 *
 * @author bajdcc
 */
class ModuleNet : IInterpreterModule {
    private var runtimeCodePage: RuntimeCodePage? = null
    var server: ModuleNetServer? = null
    var client: ModuleNetClient? = null
    private var lastError = ""
    @get:Synchronized
    @set:Synchronized
    var webServer: ModuleNetWebServer? = null
    @get:Synchronized
    @set:Synchronized
    var webApi: ModuleNetWebApi? = null
    private var bootstrap: SpringBootstrap? = null

    override val moduleName: String
        get() = "sys.net"

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
            buildRemoteMethods(info)

            runtimeCodePage = page
            return page
        }

    private fun buildRemoteMethods(info: IRuntimeDebugInfo) {
        info.addExternalFunc("g_net_get",
                RuntimeDebugExec("HTTP GET", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        var text = ""
                        val txt = args[0].obj.toString()
                        logger.debug("Request url: $txt")
                        try {
                            val url = URL(txt)
                            val urlConnection = url.openConnection() // 打开连接
                            val br = BufferedReader(InputStreamReader(urlConnection.getInputStream(), "utf-8")) // 获取输入流
                            val sb = StringBuilder()
                            br.readLines().forEach { sb.append(it).append("\n") }
                            text = sb.toString()
                            br.close()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        RuntimeObject(text)
                    }
                })
        info.addExternalFunc("g_net_get_json",
                RuntimeDebugExec("HTTP GET - json", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        var text = ""
                        val txt = args[0].obj.toString()
                        logger.debug("Request url(json): $txt")
                        try {
                            val url = URL(txt)
                            val urlConnection = url.openConnection() // 打开连接
                            urlConnection.setRequestProperty("accept", "*/*")
                            urlConnection.setRequestProperty("connection", "Keep-Alive")
                            urlConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)")
                            val br = BufferedReader(InputStreamReader(urlConnection.getInputStream(), "utf-8")) // 获取输入流
                            val sb = StringBuilder()
                            br.readLines().forEach { sb.append(it).append("\n") }
                            text = sb.toString()
                            br.close()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        parseJson(text)
                    }
                })
        info.addExternalFunc("g_net_post_json",
                RuntimeDebugExec("HTTP POST - json", arrayOf(RuntimeObjectType.kString, RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        var text = ""
                        val txt = args[0].obj.toString()
                        val data = args[1].obj.toString()
                        logger.debug("Request url(json): $txt")
                        try {
                            val url = URL(txt)
                            val urlConnection = url.openConnection() as HttpURLConnection // 打开连接
                            urlConnection.doOutput = true
                            urlConnection.doInput = true
                            urlConnection.requestMethod = "POST"
                            urlConnection.setRequestProperty("accept", "*/*")
                            urlConnection.setRequestProperty("connection", "Keep-Alive")
                            urlConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)")
                            val out = PrintWriter(OutputStreamWriter(urlConnection.outputStream, "utf-8"))
                            out.print(data)
                            out.flush()
                            val br = BufferedReader(InputStreamReader(urlConnection.inputStream, "utf-8")) // 获取输入流
                            val sb = StringBuilder()
                            br.readLines().forEach { sb.append(it).append("\n") }
                            text = sb.toString()
                            br.close()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        parseJson(text)
                    }
                })
        // -----------------------------------
        // server
        info.addExternalFunc("g_net_msg_create_server_internal",
                RuntimeDebugExec("MSG SERVER", arrayOf(RuntimeObjectType.kInt))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        if (server != null || client != null)
                            RuntimeObject(false)
                        else {
                            val port = args[0].int
                            server = ModuleNetServer(port)
                            server!!.start()
                            RuntimeObject(true)
                        }
                    }
                })
        info.addExternalFunc("g_net_msg_shutdown_server",
                RuntimeDebugExec("MSG SERVER SHUTDOWN")
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        if (server == null)
                            RuntimeObject(false)
                        else {
                            server!!.exit()
                            RuntimeObject(true)
                        }
                    }
                })
        info.addExternalFunc("g_net_msg_get_server_status",
                RuntimeDebugExec("MSG SERVER GET STATUS")
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        if (server != null) {
                            val s = server!!.status
                            if (s == ModuleNetServer.Status.ERROR) {
                                lastError = server!!.error
                                server = null
                            }
                            status.service.processService.sleep(status.pid, MSG_QUERY_TIME)
                            RuntimeObject(s.ordinal.toLong())
                        } else RuntimeObject(ModuleNetServer.Status.NULL.ordinal.toLong())
                    }
                })
        // server
        // -----------------------------------
        // client
        info.addExternalFunc("g_net_msg_create_client_internal",
                RuntimeDebugExec("MSG CLIENT", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        if (server != null || client != null)
                            RuntimeObject(false)
                        else {
                            val addr = args[0].obj.toString()
                            client = ModuleNetClient(addr)
                            client!!.start()
                            RuntimeObject(true)
                        }
                    }
                })
        info.addExternalFunc("g_net_msg_shutdown_client",
                RuntimeDebugExec("MSG CLIENT SHUTDOWN")
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        if (client == null)
                            RuntimeObject(false)
                        else {
                            client!!.exit()
                            RuntimeObject(true)
                        }
                    }
                })
        info.addExternalFunc("g_net_msg_get_client_status",
                RuntimeDebugExec("MSG CLIENT GET STATUS")
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        if (client != null) {
                            val s = client!!.status
                            if (s == ModuleNetClient.Status.ERROR) {
                                lastError = client!!.error
                                client = null
                            }
                            status.service.processService.sleep(status.pid, MSG_QUERY_TIME)
                            RuntimeObject(s.ordinal.toLong())
                        } else RuntimeObject(ModuleNetClient.Status.NULL.ordinal.toLong())
                    }
                })
        info.addExternalFunc("g_net_msg_client_send",
                RuntimeDebugExec("MSG CLIENT SEND", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        if (client != null) {
                            val s = client!!.status
                            if (s == ModuleNetClient.Status.RUNNING) {
                                status.service.processService.sleep(status.pid, MSG_SEND_TIME)
                                client!!.send(args[0].obj.toString())
                                RuntimeObject(true)
                            } else RuntimeObject(false)
                        } else RuntimeObject(false)
                    }
                })
        info.addExternalFunc("g_net_msg_client_send_with_origin",
                RuntimeDebugExec("MSG CLIENT SEND(ORIGIN)", arrayOf(RuntimeObjectType.kString, RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        if (client != null) {
                            val s = client!!.status
                            if (s == ModuleNetClient.Status.RUNNING) {
                                status.service.processService.sleep(status.pid, MSG_SEND_TIME)
                                client!!.send(args[0].obj.toString(), args[1].obj.toString())
                                RuntimeObject(true)
                            } else RuntimeObject(false)
                        } else RuntimeObject(false)
                    }
                })
        info.addExternalFunc("g_net_msg_server_send",
                RuntimeDebugExec("MSG SERVER SEND", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        if (server != null) {
                            val s = server!!.status
                            if (s == ModuleNetServer.Status.RUNNING) {
                                status.service.processService.sleep(status.pid, MSG_SEND_TIME)
                                server!!.send(args[0].obj.toString())
                                RuntimeObject(true)
                            } else RuntimeObject(false)
                        } else RuntimeObject(false)
                    }
                })
        info.addExternalFunc("g_net_msg_server_send_error",
                RuntimeDebugExec("MSG SERVER SEND ERROR", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        if (server != null) {
                            val s = server!!.status
                            if (s == ModuleNetServer.Status.RUNNING) {
                                status.service.processService.sleep(status.pid, MSG_SEND_TIME)
                                server!!.send_error(args[0].obj.toString())
                                RuntimeObject(true)
                            } else RuntimeObject(false)
                        } else RuntimeObject(false)
                    }
                })
        info.addExternalFunc("g_net_msg_server_send_with_origin",
                RuntimeDebugExec("MSG SERVER SEND(ORIGIN)", arrayOf(RuntimeObjectType.kString, RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        if (server != null) {
                            val s = server!!.status
                            if (s == ModuleNetServer.Status.RUNNING) {
                                status.service.processService.sleep(status.pid, MSG_SEND_TIME)
                                server!!.send(args[0].obj.toString(), args[1].obj.toString())
                                RuntimeObject(true)
                            } else RuntimeObject(false)
                        } else RuntimeObject(false)
                    }
                })
        // client
        // -----------------------------------
        info.addExternalFunc("g_net_msg_get_error",
                RuntimeDebugExec("MSG SERVER GET ERROR")
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(lastError) })
        info.addExternalFunc("g_net_msg_get_server_msg",
                RuntimeDebugExec("MSG SERVER GET MESSAGE")
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(if (server == null) null else server!!.message) })
        info.addExternalFunc("g_net_msg_get_client_msg",
                RuntimeDebugExec("MSG CLIENT GET MESSAGE")
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(if (client == null) null else client!!.message) })
        info.addExternalFunc("g_net_msg_get_client_addr",
                RuntimeDebugExec("MSG CLIENT GET ADDRESS")
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(if (client == null) null else client!!.addr) })
        info.addExternalFunc("g_net_parse_json",
                RuntimeDebugExec("Parse json", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus -> parseJsonSafe(args[0].obj.toString()) })
        info.addExternalFunc("g_net_get_rss",
                RuntimeDebugExec("RSS GET(BAIDU)", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val txt = args[0].obj.toString()
                        logger.debug("Request url(rss): $txt")
                        val array = RuntimeArray()
                        val pattern = Pattern.compile("<br>(.*)<br />")
                        try {
                            val reader = SAXReader()
                            val document = reader.read(txt)
                            val title = document.selectSingleNode("//title")
                            array.add(RuntimeObject(title.text))
                            val list = document.selectNodes("//item").map { it as Node }
                            for (item in list) {
                                val itemTitle = item.valueOf("title")
                                array.add(RuntimeObject(itemTitle))
                                val itemDescription = item.valueOf("description")
                                val matcher = pattern.matcher(itemDescription)
                                if (matcher.find()) {
                                    array.add(RuntimeObject(matcher.group(1)))
                                } else {
                                    array.add(RuntimeObject(itemDescription.replace("<br />", "")))
                                }
                            }
                            RuntimeObject(array)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            RuntimeObject(array)
                        }
                    }
                })
        info.addExternalFunc("g_net_start_web",
                RuntimeDebugExec("WEB SERVER", arrayOf(RuntimeObjectType.kString))
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        try {
                            val text = args[0].obj.toString()
                            val port = Integer.parseInt(text)
                            Thread(ModuleNetWebServer(port)).start()
                            RuntimeObject(true)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            RuntimeObject(false)
                        }
                    }
                })
        info.addExternalFunc("g_net_stop_web",
                RuntimeDebugExec("STOP WEB SERVER")
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val server = webServer
                        if (server != null) {
                            server.isRunning = false
                            webServer = null
                            RuntimeObject(true)
                        } else {
                            RuntimeObject(false)
                        }
                    }
                })
        info.addExternalFunc("g_net_query_web",
                RuntimeDebugExec("QUERY WEB SERVER")
                { args: List<RuntimeObject>, status: IRuntimeStatus -> RuntimeObject(webServer != null) })
        info.addExternalFunc("g_net_has_request",
                RuntimeDebugExec("WEB SERVER - REQUEST")
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val server = webServer
                        if (server != null) {
                            RuntimeObject(server.hasRequest())
                        } else {
                            RuntimeObject(false)
                        }
                    }
                })
        info.addExternalFunc("g_net_get_request",
                RuntimeDebugExec("WEB SERVER - PEEK REQUEST")
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        val server = webServer
                        if (server != null) {
                            RuntimeObject(server.peekRequest())
                        } else {
                            RuntimeObject(false)
                        }
                    }
                })
        info.addExternalFunc("g_web_get_ip",
                RuntimeDebugExec("获取IP地址")
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        try {
                            val address = InetAddress.getLocalHost()
                            RuntimeObject(address.hostAddress)
                        } catch (e: UnknownHostException) {
                            e.printStackTrace()
                            RuntimeObject("Unknown IP Address")
                        }
                    }
                })
        info.addExternalFunc("g_web_get_hostname",
                RuntimeDebugExec("获取主机名")
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        try {
                            val address = InetAddress.getLocalHost()
                            RuntimeObject(address.hostName)
                        } catch (e: UnknownHostException) {
                            e.printStackTrace()
                            RuntimeObject("Unknown Hostname")
                        }
                    }
                })
        info.addExternalFunc("g_web_start_spring",
                RuntimeDebugExec("启动Spring Boot")
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        webApi = ModuleNetWebApi()
                        bootstrap = SpringBootstrap()
                        bootstrap!!.start()
                        null
                    }
                })
        info.addExternalFunc("g_web_stop_spring",
                RuntimeDebugExec("关闭Spring Boot")
                { args: List<RuntimeObject>, status: IRuntimeStatus ->
                    run {
                        webApi = null
                        if (bootstrap != null) {
                            bootstrap!!.terminate()
                            bootstrap!!.join()
                        }
                        null
                    }
                })
    }

    @Synchronized
    fun resetBootstrap() {
        this.bootstrap = null
    }

    companion object {

        private val logger = Logger.getLogger("net")
        val instance = ModuleNet()

        private const val MSG_QUERY_TIME = 15
        private const val MSG_SEND_TIME = 5

        private fun parseJsonSafe(text: String): RuntimeObject? {
            try {
                val o = JSON.parseObject(text)
                return parseInternal(o)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        private fun parseJson(text: String): RuntimeObject {
            try {
                val o = JSON.parseObject(text)
                return parseInternal(o)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return RuntimeObject("json - parse error")
        }

        private fun parseInternal(o: Any): RuntimeObject {
            if (o is JSONObject) {
                val map = RuntimeMap()
                o.forEach { key, value -> map.put(key, parseInternal(value)) }
                return RuntimeObject(map)
            } else if (o is JSONArray) {
                val arr = RuntimeArray()
                o.forEach { key -> arr.add(parseInternal(key)) }
                return RuntimeObject(arr)
            } else {
                if (o is Int) {
                    return RuntimeObject(o.toLong())
                } else if (o is Float) {
                    return RuntimeObject(o.toDouble())
                }
                return RuntimeObject(o)
            }
        }
    }
}
