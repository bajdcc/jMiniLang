package com.bajdcc.LALR1.interpret.module.net

import com.alibaba.fastjson.JSON
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.util.concurrent.GlobalEventExecutor
import java.nio.charset.StandardCharsets.UTF_8
import java.util.concurrent.LinkedBlockingQueue

/**
 * 【模块】通讯服务端
 *
 * @author bajdcc
 */
class ModuleNetServer(val port: Int) : Thread() {
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

    fun exit() {
        CHANNEL_GROUP.close().awaitUninterruptibly()
    }

    override fun run() {
        val bossGroup = NioEventLoopGroup()
        val workerGroup = NioEventLoopGroup()
        try {
            val b = ServerBootstrap()
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel::class.java)
                    .childHandler(object : ChannelInitializer<SocketChannel>() {
                        public override fun initChannel(ch: SocketChannel) {
                            ch.pipeline()
                                    .addLast("decoder", LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                                    .addLast("encoder", LengthFieldPrepender(4, false))
                                    .addLast(StringDecoder(UTF_8))
                                    .addLast(StringEncoder(UTF_8))
                                    .addLast(ModuleNetServerHandler(msgQueue))
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
            var f = b.bind(port)
            CHANNEL_GROUP.add(f.channel())
            f = f.sync()
            if (f.isDone) {
                status = Status.RUNNING
            }
            f.channel().closeFuture().sync()
            status = Status.ERROR
            error = "Server closed."
        } catch (e: Exception) {
            status = Status.ERROR
            error = "Error: " + e.message
            e.printStackTrace()
        } finally {
            workerGroup.shutdownGracefully()
            bossGroup.shutdownGracefully()
        }
    }

    fun send(msg: String) {
        CHANNEL_GROUP.forEach { channel ->
            channel.writeAndFlush(String.format("{ \"addr\": \"%s\", \"type\": \"ECHO\", \"content\": %s }\r\n",
                    channel.localAddress().toString(), JSON.toJSONString(msg)))
        }
    }

    fun send(msg: String, address: String) {
        CHANNEL_GROUP.forEach { channel ->
            channel.writeAndFlush(String.format("{ \"origin\": \"%s\", \"addr\": \"%s\", \"type\": \"ECHO\", \"content\": %s }\r\n",
                    address, channel.localAddress().toString(), JSON.toJSONString(msg)))
        }
    }


    fun send_error(msg: String) {
        CHANNEL_GROUP.forEach { channel ->
            channel.writeAndFlush(String.format("{ \"addr\": \"%s\", \"type\": \"ERR \", \"content\": %s }\r\n",
                    channel.localAddress().toString(), JSON.toJSONString(msg)))
        }
    }

    enum class Status {
        NULL,
        CREATED,
        RUNNING,
        ERROR
    }

    companion object {

        val CHANNEL_GROUP: ChannelGroup = DefaultChannelGroup("ServerChannelGroups", GlobalEventExecutor.INSTANCE)
    }
}
