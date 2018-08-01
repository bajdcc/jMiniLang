package com.bajdcc.LALR1.interpret.module.web

import org.apache.log4j.Logger
import java.io.IOException
import java.io.RandomAccessFile
import java.net.StandardSocketOptions
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.channels.SelectionKey
import java.nio.channels.SocketChannel
import java.nio.charset.StandardCharsets.UTF_8
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * 【模块】请求处理
 *
 * @author bajdcc
 */
class ModuleNetWebHandler(private val ctx: ModuleNetWebContext) : Runnable {

    override fun run() {
        try {
            val sem = ctx.block()
            if (!sem.tryAcquire(3, TimeUnit.SECONDS)) {
                throw TimeoutException("Timed out.")
            }
            if (ctx.getReqHeaderValue("Connection").equals("keep-alive", ignoreCase = true)) {
                val channel = ctx.key.channel() as SocketChannel
                channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true)
            } else {
                val channel = ctx.key.channel() as SocketChannel
                channel.setOption(StandardSocketOptions.SO_KEEPALIVE, false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ctx.code = 500
            ctx.response = getHtml(DEFAULT_TITLE, "Internal Server Error", e.message)
        }

        try {
            val type = ctx.contentType
            if (type == 0) {
                sendString(ctx)
            } else if (type == 1) {
            } else if (type == 2) { // Resource file
                sendResource(ctx)
            } else if (type == 3) {
            } else {
                sendString(ctx)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        logger.info(String.format("%s exit", Thread.currentThread().name))
    }

    companion object {

        private val logger = Logger.getLogger("handler")

        private val DEFAULT_TITLE = "jMiniLang Web Server"
        private val ENDL = "\r\n"
        private val RESOURCE_PATH = "/static"

        private fun getHtml(title: String, notice: String, message: String?): String {
            return ("<html><head><link rel=\"shortcut icon\" href=\"/favicon.ico\" /><meta charset=\"UTF-8\"><title>"
                    + title + "</title></head><body><h1>" + notice + "</h1><h2><pre>"
                    + message + "</pre></h2></body></html>")
        }

        private fun handleNotFound(ctx: ModuleNetWebContext) {
            val text = String.format("%d %s", ctx.code, ModuleNetWebHelper.getStatusCodeText(ctx.code))
            ctx.response = getHtml(DEFAULT_TITLE, text, ctx.url)
            ctx.mime = "html-utf8"
        }

        @Throws(IOException::class)
        private fun sendString(ctx: ModuleNetWebContext) {
            if (ctx.code % 100 == 4) {
                handleNotFound(ctx)
            }
            val html = ctx.response
            val sb = StringBuilder()
            sb.append(ctx.respText).append(ENDL)
            ctx.respHeaderMap.forEach { key, value -> sb.append(key).append(": ").append(value).append(ENDL) }
            sb.append("Content-Type: ").append(ctx.mimeString).append(ENDL)
            sb.append("Content-Length: ").append(html.toByteArray(UTF_8).size)
            sb.append(ENDL).append(ENDL)
            sb.append(html)
            val selector = ctx.key.selector()
            val channel = ctx.key.channel() as SocketChannel
            // TODO: 断点续传，压缩
            try {
                val buffer = ByteBuffer.wrap(sb.toString().toByteArray(UTF_8))
                channel.write(buffer)
                channel.register(selector, SelectionKey.OP_WRITE)
            } catch (e: Exception) {
                e.printStackTrace()
                channel.close()
            }

        }

        @Throws(IOException::class)
        private fun sendResource(ctx: ModuleNetWebContext) {
            val url = ctx.javaClass.getResource(RESOURCE_PATH + ctx.response)
            if (url == null) {
                ctx.code = 404
                sendString(ctx)
            } else {
                // TODO: 包内路径，时间戳，断点续传
                var channel: SocketChannel? = null
                var fileChannel: FileChannel? = null
                try {
                    val selector = ctx.key.selector()
                    channel = ctx.key.channel() as SocketChannel
                    val filename = url.toURI().path.substring(1).replace('/', '\\')
                    val file = RandomAccessFile(filename, "r")
                    val sb = StringBuilder()
                    sb.append(ctx.respText).append(ENDL)
                    ctx.respHeaderMap.forEach { key, value -> sb.append(key).append(": ").append(value).append(ENDL) }
                    sb.append("Content-Type: ").append(ctx.mimeString).append(ENDL)
                    fileChannel = file.channel
                    val size = fileChannel!!.size()
                    sb.append("Content-Length: ").append(size)
                    sb.append(ENDL).append(ENDL)
                    val buffer = ByteBuffer.wrap(sb.toString().toByteArray(UTF_8))
                    channel.write(buffer)
                    logger.info(String.format("Resource: %s, Size: %d", filename, size))
                    var position = 0L
                    while (position < size) {
                        val count = fileChannel.transferTo(position, size - position, channel)
                        if (count > 0) {
                            position += count
                        }
                    }
                    channel.register(selector, SelectionKey.OP_WRITE)
                } catch (e: Exception) {
                    e.printStackTrace()
                    channel?.close()
                } finally {
                    fileChannel?.close()
                }
            }
        }
    }
}
