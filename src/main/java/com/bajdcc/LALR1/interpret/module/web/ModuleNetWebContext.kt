package com.bajdcc.LALR1.interpret.module.web

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeMap
import java.net.MalformedURLException
import java.net.URL
import java.nio.channels.SelectionKey
import java.util.*
import java.util.concurrent.Semaphore
import java.util.regex.Pattern

/**
 * 【模块】请求上下文
 *
 * @author bajdcc
 */
class ModuleNetWebContext(val key: SelectionKey) {

    private var sem: Semaphore? = null // 多线程同步只能用信号量
    private var header: String? = null
    var response = ""
    var mime = "html-utf8"
    var code = 200
    var contentType = 0

    private var method: String? = null
    private var uri: String? = null
    private var protocol: String? = null
    private var version: String? = null
    var url: String? = null
        private set
    private val mapReqHeaders = HashMap<String, String>()
    private val mapRespHeaders = HashMap<String, String>()

    /**
     * 请求的结构：
     * headers(map), method, uri, protocol, version
     * @return HTTP请求
     */
    val reqHeader: RuntimeMap
        get() {
            val req = RuntimeMap()
            val headers = RuntimeMap()
            mapReqHeaders.forEach { key, value -> headers.put(key, RuntimeObject(value)) }
            req.put("headers", RuntimeObject(headers))
            req.put("method", RuntimeObject(method))
            req.put("uri", RuntimeObject(uri))
            req.put("version", RuntimeObject(version))
            req.put("protocol", RuntimeObject(protocol!!.toLowerCase()))
            req.put("url", RuntimeObject(url))
            try {
                val u = URL(url!!)
                req.put("port", RuntimeObject(u.port.toLong()))
                req.put("host", RuntimeObject(u.host))
                req.put("path", RuntimeObject(u.path))
                req.put("query", RuntimeObject(u.query))
                req.put("authority", RuntimeObject(u.authority))
                val idx = u.path.lastIndexOf(".")
                if (idx >= 0) {
                    val postfix = u.path.substring(idx + 1)
                    req.put("ext", RuntimeObject(postfix))
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }

            return req
        }

    var respHeader: RuntimeMap
        get() {
            val resp = RuntimeMap()
            mapRespHeaders.forEach { key, value -> resp.put(key, RuntimeObject(value)) }
            return resp
        }
        set(map) = map.getMap().forEach { key, value -> mapRespHeaders[key] = value.obj.toString() }

    val respHeaderMap: Map<String, String>
        get() = mapRespHeaders

    val respText: String
        get() = String.format("%s/%s %d %s", protocol, version, code, ModuleNetWebHelper.getStatusCodeText(code))

    val mimeString: String
        get() = ModuleNetWebHelper.getMimeByExtension(mime)

    fun setReqHeader(header: String) {
        this.header = header
        initHeader()
    }

    fun getReqHeaderValue(key: String): String {
        return mapReqHeaders.getOrDefault(key, "")
    }

    fun getRespHeaderValue(key: String): String {
        return mapRespHeaders.getOrDefault(key, "")
    }

    private fun initHeader() {
        val re1 = Pattern.compile("([A-Z]+) ([^ ]+) ([A-Z]+)/(\\d\\.\\d)")
        val headers = header!!.split("\r\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in 1 until headers.size) {
            val colon = headers[i].indexOf(':')
            if (colon != -1) {
                val key = headers[i].substring(0, colon)
                val value = headers[i].substring(colon + 1)
                mapReqHeaders[key.trim { it <= ' ' }] = value.trim { it <= ' ' }
            }
        }
        val m = re1.matcher(headers[0])
        if (m.find()) {
            method = m.group(1).trim { it <= ' ' }
            uri = m.group(2).trim { it <= ' ' }
            protocol = m.group(3).trim { it <= ' ' }
            version = m.group(4).trim { it <= ' ' }
            url = String.format("%s://%s%s", protocol!!.toLowerCase(), mapReqHeaders.getOrDefault("Host", "unknown"), uri)
        }
        mapRespHeaders["Server"] = "jMiniLang Server"
    }

    fun block(): Semaphore {
        sem = Semaphore(0)
        return sem!!
    }

    fun unblock() {
        try {
            while (sem == null) {
                Thread.sleep(100)
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        sem!!.release()
    }
}
