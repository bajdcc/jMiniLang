package com.bajdcc.LALR1.interpret.module.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.Queue;

import static com.bajdcc.LALR1.interpret.module.net.ModuleNetServer.CHANNEL_GROUP;

/**
 * 【模块】通讯服务端处理
 *
 * @author bajdcc
 */
public class ModuleNetServerHandler extends ChannelInboundHandlerAdapter {

	private Queue<String> msgQueue;

	public ModuleNetServerHandler(Queue<String> msgQueue) {
		this.msgQueue = msgQueue;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		Channel ch = ctx.channel();
		CHANNEL_GROUP.add(ch);
		if (CHANNEL_GROUP.size() > 0) {
			CHANNEL_GROUP.writeAndFlush(
					String.format("{ \"origin\": \"%s\", \"addr\": \"%s\", \"type\": \"INFO\", \"content\": \"Hello, client!\" }\r\n",
							ch.remoteAddress().toString(), ch.localAddress().toString()));
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		Channel ch = ctx.channel();
		msgQueue.add(String.valueOf(String.format("{ \"addr\": \"%s\", \"type\": \"INFO\", \"content\": \"Bye, client!\" }\r\n",
				ch.remoteAddress().toString())));
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		msgQueue.add(String.valueOf(msg));
		ReferenceCountUtil.release(msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) {
		ctx.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		CHANNEL_GROUP.remove(ctx.channel());
		ctx.close();
	}
}
