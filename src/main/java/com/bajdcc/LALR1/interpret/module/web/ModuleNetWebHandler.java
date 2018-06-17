package com.bajdcc.LALR1.interpret.module.web;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

import static com.bajdcc.LALR1.interpret.module.web.ModuleNetWebHelper.getStatusCodeText;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 【模块】请求处理
 *
 * @author bajdcc
 */
public class ModuleNetWebHandler implements Runnable {

	private static String defaultTitle = "jMiniLang Web Server";
	private ModuleNetWebContext ctx;

	public ModuleNetWebHandler(ModuleNetWebContext context) {
		this.ctx = context;
	}

	private static String getHtml(String title, String notice, String message) {
		return "<html><head><title>" + title + "</title></head><body><h1>" + notice + "</h1><h2><pre>"
				+ message + "</pre></h2></body></html>";
	}

	@Override
	public void run() {
		final String endl = "\r\n";
		try {
			Semaphore sem = ctx.block();
			if (!sem.tryAcquire(3, TimeUnit.SECONDS)) {
				throw new InterruptedException("Timed out.");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			ctx.setCode(500);
			ctx.setResponse(getHtml(defaultTitle, "Internal Server Error", e.getMessage()));
		}
		String html = ctx.getResponse();
		StringBuilder sb = new StringBuilder();
		sb.append("HTTP/1.1 ").append(ctx.getCode() + " ").append(getStatusCodeText(ctx.getCode())).append(endl);
		sb.append("Server: jMiniLang Web Server").append(endl);
		sb.append("Content-Type: text/html;charset=utf-8").append(endl);
		sb.append("Content-Length: ").append(html.getBytes(UTF_8).length);
		sb.append(endl).append(endl);
		sb.append(html);
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.put(sb.toString().getBytes(UTF_8));
		buffer.flip();
		Selector selector = ctx.getKey().selector();
		SocketChannel channel = (SocketChannel) ctx.getKey().channel();
		try {
			channel.register(selector, SelectionKey.OP_WRITE);
			channel.write(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
