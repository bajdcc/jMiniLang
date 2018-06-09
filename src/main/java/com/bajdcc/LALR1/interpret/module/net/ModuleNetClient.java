package com.bajdcc.LALR1.interpret.module.net;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 【模块】通讯客户端
 *
 * @author bajdcc
 */
public class ModuleNetClient extends Thread {

	public static final ChannelGroup CHANNEL_GROUP =
			new DefaultChannelGroup("ClientChannelGroups", GlobalEventExecutor.INSTANCE);
	private String addr;
	private volatile String error = "";
	private volatile Status status = Status.CREATED;
	private Queue<String> msgQueue = new LinkedBlockingQueue<>();

	public ModuleNetClient(String addr) {
		this.addr = addr;
	}

	public String getAddr() {
		return addr;
	}

	public String getError() {
		return error;
	}

	public Status getStatus() {
		return status;
	}

	public String getMessage() {
		if (msgQueue.isEmpty()) {
			return null;
		}
		return msgQueue.poll();
	}

	public void send(String msg) {
		CHANNEL_GROUP.forEach(channel -> channel.writeAndFlush(String.format("{ \"addr\": \"%s\", \"type\": \"MSG \", \"content\": %s }\r\n",
				channel.localAddress().toString(), JSON.toJSONString(msg))));
	}

	public void send(String msg, String address) {
		CHANNEL_GROUP.forEach(channel -> {
			channel.writeAndFlush(String.format("{ \"origin\": \"%s\", \"addr\": \"%s\", \"type\": \"MSG \", \"content\": %s }\r\n",
					address, channel.localAddress().toString(), JSON.toJSONString(msg)));
		});
	}

	public void exit() {
		CHANNEL_GROUP.close().awaitUninterruptibly();
	}

	public void run() {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			if (addr.startsWith(":")) {
				addr = "127.0.0.1" + addr;
			}
			URI uri = new URI("http://" + addr);
			if (uri.getPort() < 0)
				throw new URISyntaxException(addr, "invalid address");
			Bootstrap b = new Bootstrap();
			b.group(workerGroup)
					.channel(NioSocketChannel.class)
					.option(ChannelOption.SO_KEEPALIVE, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) {
							ch.pipeline()
									.addLast("decoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
									.addLast("encoder", new LengthFieldPrepender(4, false))
									.addLast(new StringDecoder(Charset.forName("utf-8")))
									.addLast(new StringEncoder(Charset.forName("utf-8")))
									.addLast(new ModuleNetClientHandler(msgQueue));
						}
					});
			ChannelFuture f = b.connect(uri.getHost(), uri.getPort());
			CHANNEL_GROUP.add(f.channel());
			f = f.sync();
			if (f.isDone()) {
				status = Status.RUNNING;
			}
			this.addr = f.channel().localAddress().toString();
			f.channel().closeFuture().sync();
			status = Status.ERROR;
			error = "Client closed.";
		} catch (Exception e) {
			status = Status.ERROR;
			error = "Error: " + e.getMessage();
			if (error == null)
				error = e.getClass().getSimpleName();
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}

	public enum Status {
		NULL,
		CREATED,
		RUNNING,
		ERROR
	}
}
