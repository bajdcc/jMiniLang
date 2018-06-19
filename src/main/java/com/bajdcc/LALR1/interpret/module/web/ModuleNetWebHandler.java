package com.bajdcc.LALR1.interpret.module.web;

import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 【模块】请求处理
 *
 * @author bajdcc
 */
public class ModuleNetWebHandler implements Runnable {

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
				throw new InterruptedException("Timed out.");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			ctx.setCode(500);
			ctx.setResponse(getHtml(DEFAULT_TITLE, "Internal Server Error", e.getMessage()));
		}
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
	}

	private static void handleNotFound(ModuleNetWebContext ctx) {
		String text = String.format("%d %s", ctx.getCode(), ModuleNetWebHelper.getStatusCodeText(ctx.getCode()));
		ctx.setResponse(getHtml(DEFAULT_TITLE, text, ctx.getUrl()));
		ctx.setMime("html-utf8");
	}

	private static void sendString(ModuleNetWebContext ctx) {
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
		try {
			ByteBuffer buffer = ByteBuffer.wrap(sb.toString().getBytes(UTF_8));
			channel.register(selector, SelectionKey.OP_WRITE);
			channel.write(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void sendResource(ModuleNetWebContext ctx) {
		URL url = ctx.getClass().getResource(RESOURCE_PATH + ctx.getResponse());
		if (url == null) {
			ctx.setCode(404);
			sendString(ctx);
		} else {
			try {
				Selector selector = ctx.getKey().selector();
				SocketChannel channel = (SocketChannel) ctx.getKey().channel();
				String filename = url.toURI().getPath();
				RandomAccessFile file = new RandomAccessFile(filename, "r");
				StringBuilder sb = new StringBuilder();
				sb.append(ctx.getRespText()).append(ENDL);
				ctx.getRespHeaderMap().forEach((key, value) -> sb.append(key).append(": ").append(value).append(ENDL));
				sb.append("Content-Type: ").append(ctx.getMimeString()).append(ENDL);
				FileChannel fileChannel = file.getChannel();
				long size = fileChannel.size();
				sb.append("Content-Length: ").append(size);
				sb.append(ENDL).append(ENDL);
				channel.register(selector, SelectionKey.OP_WRITE);
				ByteBuffer buffer = ByteBuffer.wrap(sb.toString().getBytes(UTF_8));
				channel.write(buffer);
				fileChannel.transferTo(0, size, channel);
			} catch (Exception e) {
				e.printStackTrace();
				ctx.setCode(404);
				sendString(ctx);
			}
		}
	}
}
