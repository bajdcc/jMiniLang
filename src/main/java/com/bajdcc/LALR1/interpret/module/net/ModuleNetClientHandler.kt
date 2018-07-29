package com.bajdcc.LALR1.interpret.module.net

import com.alibaba.fastjson.JSON
import com.bajdcc.LALR1.interpret.module.net.ModuleNetClient.Companion.CHANNEL_GROUP
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.util.ReferenceCountUtil
import java.util.*

/**
 * 【模块】通讯客户端处理
 *
 * @author bajdcc
 */
class ModuleNetClientHandler(private val msgQueue: Queue<String>) : ChannelInboundHandlerAdapter() {

    override fun channelActive(ctx: ChannelHandlerContext) {
        val ch = ctx.channel()
        ctx.writeAndFlush(String.format("{ \"addr\": \"%s\", \"type\": \"INFO\", \"content\": \"Hello, server!\" }\r\n",
                ch.localAddress().toString()))
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
        msgQueue.add(String.format("{ \"origin\": \"NULL\", \"addr\": \"Error\", \"type\": \"ERR \", \"content\": %s }",
                JSON.toJSONString(cause.message)))
        CHANNEL_GROUP.remove(ctx.channel())
        ctx.close()
    }
}
