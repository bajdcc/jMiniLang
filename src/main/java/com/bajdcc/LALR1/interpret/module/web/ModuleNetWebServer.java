package com.bajdcc.LALR1.interpret.module.web;

import com.bajdcc.LALR1.grammar.runtime.data.RuntimeMap;
import com.bajdcc.LALR1.interpret.module.ModuleNet;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Deque;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 【模块】网页服务器
 *
 * @author bajdcc
 */
public class ModuleNetWebServer implements Runnable {

	private static Logger logger = Logger.getLogger("web");

	private int port;
	private boolean running = true;
	private Deque<ModuleNetWebContext> queue = new ConcurrentLinkedDeque<>();

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public ModuleNetWebContext dequeue() {
		return queue.poll();
	}

	public boolean hasRequest() {
		return !queue.isEmpty();
	}

	public RuntimeMap peekRequest() {
		ModuleNetWebContext ctx = queue.peek();
		if (ctx == null)
			return null;
		return ctx.getReqHeader();
	}

	public ModuleNetWebServer(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		try {
			Selector selector = Selector.open();
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			ServerSocket serverSocket = serverSocketChannel.socket();
			serverSocket.setReuseAddress(true);
			serverSocket.bind(new InetSocketAddress(port));
			InetAddress address = InetAddress.getLocalHost();
			logger.info("Web server ip: " + address.getHostAddress());
			logger.info("Web server hostname: " + address.getHostName());
			logger.info("Web server port: " + port);
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			ModuleNet.getInstance().setWebServer(this);
			while(running) {
				int readyChannels = selector.select(1000);
				if(readyChannels == 0)
					continue;
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = keys.iterator();
				while(iterator.hasNext()) {
					SelectionKey key = iterator.next();
					SocketChannel socketChannel = null;
					try {
						if(key.isAcceptable()) {
							ServerSocketChannel server = (ServerSocketChannel) key.channel();
							socketChannel = server.accept();
							if(socketChannel != null) {
								socketChannel.configureBlocking(false);
								socketChannel.register(selector, SelectionKey.OP_READ);
							}
						} else if (key.isReadable()) {
							socketChannel = (SocketChannel) key.channel();
							String requestHeader = handleHeader(socketChannel);
							if (requestHeader != null) {
								ModuleNetWebContext ctx = new ModuleNetWebContext(key);
								ctx.setReqHeader(requestHeader);
								logger.info(String.format("From: %s, Url: %s",
										((InetSocketAddress) socketChannel.getRemoteAddress()).getHostString(),
										ctx.getUrl()));
								new Thread(new ModuleNetWebHandler(ctx)).start();
								queue.add(ctx);
							} else {
								socketChannel.close();
							}
						} else if (key.isWritable()) {
							socketChannel = (SocketChannel) key.channel();
							if (socketChannel.getOption(StandardSocketOptions.SO_KEEPALIVE)) {
								socketChannel.register(selector, SelectionKey.OP_READ);
							} else {
								socketChannel.close();
							}
						}
					} catch (IOException e) {
						if (socketChannel != null)
							socketChannel.close();
						e.printStackTrace();
					} finally {
						iterator.remove();
					}
				}
			}
			serverSocket.close();
			serverSocketChannel.close();
			selector.close();
			logger.info("Web server exit");
		} catch (Exception e) {
			e.printStackTrace();
		}
		ModuleNet.getInstance().setWebServer(null);
	}

	private String handleHeader(SocketChannel socketChannel) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte[] bytes;
		int size;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((size = socketChannel.read(buffer)) > 0) {
			bytes = new byte[size];
			buffer.flip();
			buffer.get(bytes);
			baos.write(bytes);
			buffer.clear();
		}
		bytes = baos.toByteArray();
		if (bytes.length == 0)
			return null;
		return new String(bytes, UTF_8);
	}
}
