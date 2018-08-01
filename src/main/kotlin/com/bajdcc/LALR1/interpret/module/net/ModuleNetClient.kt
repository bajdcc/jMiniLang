package com.bajdcc.LALR1.interpret.module.net

import com.alibaba.fastjson.JSON
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.util.concurrent.GlobalEventExecutor
import java.net.URI
import java.net.URISyntaxException
import java.nio.charset.StandardCharsets.UTF_8
import java.util.concurrent.LinkedBlockingQueue

/**
 * 【模块】通讯客户端
 *
 * @author bajdcc
 */
class ModuleNetClient(addr: String) : Thread() {
    var addr: String? = null
        private set
    @Volatile
    var error: String = ""
        private set
    @Volatile
    var status = Status.CREATED
        private set
    private val msgQueue = LinkedBlockingQueue<String>()

    val message: String?
        get() = if (msgQueue.isEmpty()) {
            null
        } else msgQueue.poll()

    init {
        this.addr = addr
    }

    fun send(msg: String) {
        CHANNEL_GROUP.forEach { channel ->
            channel.writeAndFlush(String.format("{ \"addr\": \"%s\", \"type\": \"MSG \", \"content\": %s }\r\n",
                    channel.localAddress().toString(), JSON.toJSONString(msg)))
        }
    }

    fun send(msg: String, address: String) {
        CHANNEL_GROUP.forEach { channel ->
            channel.writeAndFlush(String.format("{ \"origin\": \"%s\", \"addr\": \"%s\", \"type\": \"MSG \", \"content\": %s }\r\n",
                    address, channel.localAddress().toString(), JSON.toJSONString(msg)))
        }
    }

    fun exit() {
        CHANNEL_GROUP.close().awaitUninterruptibly()
    }

    override fun run() {
        val workerGroup = NioEventLoopGroup()
        try {
            if (addr!!.startsWith(":")) {
                addr = "127.0.0.1" + addr!!
            }
            val uri = URI("http://" + addr!!)
            if (uri.port < 0)
                throw URISyntaxException(addr!!, "invalid address")
            val b = Bootstrap()
            b.group(workerGroup)
                    .channel(NioSocketChannel::class.java)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(object : ChannelInitializer<SocketChannel>() {
                        public override fun initChannel(ch: SocketChannel) {
                            ch.pipeline()
                                    .addLast("decoder", LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                                    .addLast("encoder", LengthFieldPrepender(4, false))
                                    .addLast(StringDecoder(UTF_8))
                                    .addLast(StringEncoder(UTF_8))
                                    .addLast(ModuleNetClientHandler(msgQueue))
                        }
                    })
            var f = b.connect(uri.host, uri.port)
            CHANNEL_GROUP.add(f.channel())
            f = f.sync()
            if (f.isDone) {
                status = Status.RUNNING
            }
            this.addr = f.channel().localAddress().toString()
            f.channel().closeFuture().sync()
            status = Status.ERROR
            error = "Client closed."
        } catch (e: Exception) {
            status = Status.ERROR
            error = "Error: " + e.message
            e.printStackTrace()
        } finally {
            workerGroup.shutdownGracefully()
        }
    }

    enum class Status {
        NULL,
        CREATED,
        RUNNING,
        ERROR
    }

    companion object {

        val CHANNEL_GROUP: ChannelGroup = DefaultChannelGroup("ClientChannelGroups", GlobalEventExecutor.INSTANCE)
    }
}
