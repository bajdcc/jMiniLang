package com.bajdcc.LALR1.interpret.module.web;

import com.bajdcc.LALR1.interpret.module.ModuleNet;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

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

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
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
			logger.info("Web server port: " + port);
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			ModuleNet.getInstance().setWebServer(this);
			while(running) {
				int readyChannels = selector.select();
				if(readyChannels == 0)
					continue;
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = keys.iterator();
				while(iterator.hasNext()) {
					SelectionKey key = iterator.next();
					if(key.isAcceptable()) {
						ServerSocketChannel server = (ServerSocketChannel) key.channel();
						SocketChannel socketChannel = server.accept();
						if(socketChannel != null) {
							logger.info("Request from: " + ((InetSocketAddress)socketChannel.getRemoteAddress()).getHostString());
							socketChannel.configureBlocking(false);
							socketChannel.register(selector, SelectionKey.OP_READ);
						}
					} else if (key.isReadable()) {
						SocketChannel socketChannel = (SocketChannel) key.channel();
						String requestHeader = handleHeader(socketChannel);
						if(requestHeader != null) {
							new Thread(new ModuleNetWebHandler(requestHeader, key)).start();
						}
					} else if (key.isWritable()) {
						SocketChannel socketChannel = (SocketChannel) key.channel();
						socketChannel.shutdownInput();
						socketChannel.close();
					}
					iterator.remove();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ModuleNet.getInstance().setWebServer(null);
	}

	private String handleHeader(SocketChannel socketChannel) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		byte[] bytes;
		int size;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			while ((size = socketChannel.read(buffer)) > 0) {
				bytes = new byte[size];
				buffer.flip();
				buffer.get(bytes);
				baos.write(bytes);
				buffer.clear();
			}
			bytes = baos.toByteArray();
			return new String(bytes, UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
