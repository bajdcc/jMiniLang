package com.bajdcc.LALR1.interpret.module.net

import com.bajdcc.LALR1.interpret.module.net.ModuleNetServer.Companion.CHANNEL_GROUP
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.util.ReferenceCountUtil
import java.util.*

/**
 * 【模块】通讯服务端处理
 *
 * @author bajdcc
 */
class ModuleNetServerHandler(private val msgQueue: Queue<String>) : ChannelInboundHandlerAdapter() {

    override fun channelActive(ctx: ChannelHandlerContext) {
        val ch = ctx.channel()
        CHANNEL_GROUP.add(ch)
        if (CHANNEL_GROUP.size > 0) {
            CHANNEL_GROUP.writeAndFlush(
                    String.format("{ \"origin\": \"%s\", \"addr\": \"%s\", \"type\": \"INFO\", \"content\": \"Hello, client!\" }\r\n",
                            ch.remoteAddress().toString(), ch.localAddress().toString()))
        }
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        val ch = ctx.channel()
        msgQueue.add(String.format("{ \"addr\": \"%s\", \"type\": \"INFO\", \"content\": \"Bye, client!\" }\r\n",
                ch.remoteAddress().toString()))
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        msgQueue.add(msg.toString())
        ReferenceCountUtil.release(msg)
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        ctx.flush()
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext?) {
        ctx!!.close()
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        CHANNEL_GROUP.remove(ctx.channel())
        ctx.close()
    }
}
