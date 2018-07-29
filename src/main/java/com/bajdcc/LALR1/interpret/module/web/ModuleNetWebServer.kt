package com.bajdcc.LALR1.interpret.module.web

import com.bajdcc.LALR1.grammar.runtime.data.RuntimeMap
import com.bajdcc.LALR1.interpret.module.ModuleNet
import org.apache.log4j.Logger
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.StandardSocketOptions
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.nio.charset.StandardCharsets.UTF_8
import java.util.concurrent.ConcurrentLinkedDeque

/**
 * 【模块】网页服务器
 *
 * @author bajdcc
 */
class ModuleNetWebServer(private val port: Int) : Runnable {
    var isRunning = true
    private val queue = ConcurrentLinkedDeque<ModuleNetWebContext>()

    fun dequeue(): ModuleNetWebContext? {
        return queue.poll()
    }

    fun hasRequest(): Boolean {
        return !queue.isEmpty()
    }

    fun peekRequest(): RuntimeMap? {
        val ctx = queue.peek() ?: return null
        return ctx.reqHeader
    }

    override fun run() {
        try {
            val selector = Selector.open()
            val serverSocketChannel = ServerSocketChannel.open()
            val serverSocket = serverSocketChannel.socket()
            serverSocket.reuseAddress = true
            serverSocket.bind(InetSocketAddress(port))
            val address = InetAddress.getLocalHost()
            logger.info("Web server ip: " + address.hostAddress)
            logger.info("Web server hostname: " + address.hostName)
            logger.info("Web server port: $port")
            serverSocketChannel.configureBlocking(false)
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT)
            ModuleNet.instance.webServer = this
            while (isRunning) {
                val readyChannels = selector.select(1000)
                if (readyChannels == 0)
                    continue
                val keys = selector.selectedKeys()
                val iterator = keys.iterator()
                while (iterator.hasNext()) {
                    val key = iterator.next()
                    var socketChannel: SocketChannel? = null
                    try {
                        if (key.isAcceptable) {
                            val server = key.channel() as ServerSocketChannel
                            socketChannel = server.accept()
                            if (socketChannel != null) {
                                socketChannel.configureBlocking(false)
                                socketChannel.register(selector, SelectionKey.OP_READ)
                            }
                        } else if (key.isReadable) {
                            socketChannel = key.channel() as SocketChannel
                            val requestHeader = handleHeader(socketChannel)
                            if (requestHeader != null) {
                                val ctx = ModuleNetWebContext(key)
                                ctx.setReqHeader(requestHeader)
                                logger.info(String.format("From: %s, Url: %s",
                                        (socketChannel.remoteAddress as InetSocketAddress).hostString,
                                        ctx.url))
                                Thread(ModuleNetWebHandler(ctx)).start()
                                queue.add(ctx)
                            } else {
                                socketChannel.close()
                            }
                        } else if (key.isWritable) {
                            socketChannel = key.channel() as SocketChannel
                            if (socketChannel.getOption(StandardSocketOptions.SO_KEEPALIVE)) {
                                socketChannel.register(selector, SelectionKey.OP_READ)
                            } else {
                                socketChannel.close()
                            }
                        }
                    } catch (e: IOException) {
                        socketChannel?.close()
                        e.printStackTrace()
                    } finally {
                        iterator.remove()
                    }
                }
            }
            serverSocket.close()
            serverSocketChannel.close()
            selector.close()
            logger.info("Web server exit")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        ModuleNet.instance.webServer = null
    }

    @Throws(IOException::class)
    private fun handleHeader(socketChannel: SocketChannel): String? {
        val buffer = ByteBuffer.allocate(1024)
        var bytes: ByteArray
        val baos = ByteArrayOutputStream()
        var size = socketChannel.read(buffer)
        while (size > 0) {
            bytes = ByteArray(size)
            buffer.flip()
            buffer.get(bytes)
            baos.write(bytes)
            buffer.clear()
            size = socketChannel.read(buffer)
        }
        bytes = baos.toByteArray()
        return if (bytes.size == 0) null else String(bytes, UTF_8)
    }

    companion object {

        private val logger = Logger.getLogger("web")
    }
}
