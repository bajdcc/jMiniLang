package priv.bajdcc.LALR1.interpret.module.net;

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
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.URI;
import java.net.URISyntaxException;
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
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new StringEncoder());
                            ch.pipeline().addLast(new ModuleNetClientHandler(msgQueue));
                        }
                    });
            ChannelFuture f = b.connect(uri.getHost(), uri.getPort());
            CHANNEL_GROUP.add(f.channel());
            f = f.sync();
            if (f.isDone()) {
                status = Status.RUNNING;
            }
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
