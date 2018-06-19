package com.bajdcc.LALR1.interpret.module.web;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.StandardSocketOptions;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 【模块】请求处理
 *
 * @author bajdcc
 */
public class ModuleNetWebHandler implements Runnable {

	private static Logger logger = Logger.getLogger("handler");

	private final static String DEFAULT_TITLE = "jMiniLang Web Server";
	private final static String ENDL = "\r\n";
	private final static String RESOURCE_PATH = "/com/bajdcc/www";
	private ModuleNetWebContext ctx;

	public ModuleNetWebHandler(ModuleNetWebContext context) {
		this.ctx = context;
	}

	private static String getHtml(String title, String notice, String message) {
		return "<html><head><link rel=\"shortcut icon\" href=\"/favicon.ico\" /><meta charset=\"UTF-8\"><title>"
				+ title + "</title></head><body><h1>" + notice + "</h1><h2><pre>"
				+ message + "</pre></h2></body></html>";
	}

	@Override
	public void run() {
		try {
			Semaphore sem = ctx.block();
			if (!sem.tryAcquire(3, TimeUnit.SECONDS)) {
				throw new TimeoutException("Timed out.");
			}
			if (ctx.getReqHeaderValue("Connection").equalsIgnoreCase("keep-alive")) {
				SocketChannel channel = (SocketChannel) ctx.getKey().channel();
				channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
			} else {
				SocketChannel channel = (SocketChannel) ctx.getKey().channel();
				channel.setOption(StandardSocketOptions.SO_KEEPALIVE, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ctx.setCode(500);
			ctx.setResponse(getHtml(DEFAULT_TITLE, "Internal Server Error", e.getMessage()));
		}
		try {
			int type = ctx.getContentType();
			if (type == 0) {
				sendString(ctx);
			} else if (type == 1) {
			} else if (type == 2) { // Resource file
				sendResource(ctx);
			} else if (type == 3) {
			} else {
				sendString(ctx);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info(String.format("%s exit", Thread.currentThread().getName()));
	}

	private static void handleNotFound(ModuleNetWebContext ctx) {
		String text = String.format("%d %s", ctx.getCode(), ModuleNetWebHelper.getStatusCodeText(ctx.getCode()));
		ctx.setResponse(getHtml(DEFAULT_TITLE, text, ctx.getUrl()));
		ctx.setMime("html-utf8");
	}

	private static void sendString(ModuleNetWebContext ctx) throws IOException {
		if (ctx.getCode() % 100 == 4) {
			handleNotFound(ctx);
		}
		String html = ctx.getResponse();
		StringBuilder sb = new StringBuilder();
		sb.append(ctx.getRespText()).append(ENDL);
		ctx.getRespHeaderMap().forEach((key, value) -> sb.append(key).append(": ").append(value).append(ENDL));
		sb.append("Content-Type: ").append(ctx.getMimeString()).append(ENDL);
		sb.append("Content-Length: ").append(html.getBytes(UTF_8).length);
		sb.append(ENDL).append(ENDL);
		sb.append(html);
		Selector selector = ctx.getKey().selector();
		SocketChannel channel = (SocketChannel) ctx.getKey().channel();
		// TODO: 断点续传，压缩
		try {
			ByteBuffer buffer = ByteBuffer.wrap(sb.toString().getBytes(UTF_8));
			channel.write(buffer);
			channel.register(selector, SelectionKey.OP_WRITE);
		} catch (Exception e) {
			e.printStackTrace();
			if (channel != null)
				channel.close();
		}
	}

	private static void sendResource(ModuleNetWebContext ctx) throws IOException {
		URL url = ctx.getClass().getResource(RESOURCE_PATH + ctx.getResponse());
		if (url == null) {
			ctx.setCode(404);
			sendString(ctx);
		} else {
			// TODO: 包内路径，时间戳，断点续传
			SocketChannel channel = null;
			FileChannel fileChannel = null;
			try {
				Selector selector = ctx.getKey().selector();
				channel = (SocketChannel) ctx.getKey().channel();
				String filename = url.toURI().getPath().substring(1).replace('/', '\\');
				RandomAccessFile file = new RandomAccessFile(filename, "r");
				StringBuilder sb = new StringBuilder();
				sb.append(ctx.getRespText()).append(ENDL);
				ctx.getRespHeaderMap().forEach((key, value) -> sb.append(key).append(": ").append(value).append(ENDL));
				sb.append("Content-Type: ").append(ctx.getMimeString()).append(ENDL);
				fileChannel = file.getChannel();
				long size = fileChannel.size();
				sb.append("Content-Length: ").append(size);
				sb.append(ENDL).append(ENDL);
				ByteBuffer buffer = ByteBuffer.wrap(sb.toString().getBytes(UTF_8));
				channel.write(buffer);
				logger.info(String.format("Resource: %s, Size: %d", filename, size));
				long position = 0L;
				while (position < size) {
					long count = fileChannel.transferTo(position, size - position, channel);
					if (count > 0) {
						position += count;
					}
				}
				channel.register(selector, SelectionKey.OP_WRITE);
			} catch (Exception e) {
				e.printStackTrace();
				if (channel != null)
					channel.close();
			} finally {
				if (fileChannel != null)
					fileChannel.close();
			}
		}
	}
}
