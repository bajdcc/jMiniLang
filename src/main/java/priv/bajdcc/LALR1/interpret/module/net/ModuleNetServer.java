package priv.bajdcc.LALR1.interpret.module.net;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.nio.charset.Charset;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 【模块】通讯服务端
 *
 * @author bajdcc
 */
public class ModuleNetServer extends Thread {

	public static final ChannelGroup CHANNEL_GROUP =
			new DefaultChannelGroup("ServerChannelGroups", GlobalEventExecutor.INSTANCE);
	private int port;
	private volatile String error = "";
	private volatile Status status = Status.CREATED;
	private Queue<String> msgQueue = new LinkedBlockingQueue<>();

	public ModuleNetServer(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
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

	public void exit() {
		CHANNEL_GROUP.close().awaitUninterruptibly();
	}

	public void run() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) {
							ch.pipeline()
									.addLast("decoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
									.addLast("encoder", new LengthFieldPrepender(4, false))
									.addLast(new StringDecoder(Charset.forName("utf-8")))
									.addLast(new StringEncoder(Charset.forName("utf-8")))
									.addLast(new ModuleNetServerHandler(msgQueue));
						}
					})
					.option(ChannelOption.SO_BACKLOG, 1024)
					.childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture f = b.bind(port);
			CHANNEL_GROUP.add(f.channel());
			f = f.sync();
			if (f.isDone()) {
				status = Status.RUNNING;
			}
			f.channel().closeFuture().sync();
			status = Status.ERROR;
			error = "Server closed.";
		} catch (Exception e) {
			status = Status.ERROR;
			error = "Error: " + e.getMessage();
			if (error == null)
				error = e.getClass().getSimpleName();
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public void send(String msg) {
		CHANNEL_GROUP.forEach(channel -> {
			channel.writeAndFlush(String.format("{ \"addr\": \"%s\", \"type\": \"ECHO\", \"content\": %s }\r\n",
					channel.localAddress().toString(), JSON.toJSONString(msg)));
		});
	}

	public void send(String msg, String address) {
		CHANNEL_GROUP.forEach(channel -> {
			channel.writeAndFlush(String.format("{ \"origin\": \"%s\", \"addr\": \"%s\", \"type\": \"ECHO\", \"content\": %s }\r\n",
					address, channel.localAddress().toString(), JSON.toJSONString(msg)));
		});
	}


	public void send_error(String msg) {
		CHANNEL_GROUP.forEach(channel -> {
			channel.writeAndFlush(String.format("{ \"addr\": \"%s\", \"type\": \"ERR \", \"content\": %s }\r\n",
					channel.localAddress().toString(), JSON.toJSONString(msg)));
		});
	}

	public enum Status {
		NULL,
		CREATED,
		RUNNING,
		ERROR
	}
}
