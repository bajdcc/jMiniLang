package com.bajdcc.LALR1.interpret.module.web;

import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 【模块】请求处理
 *
 * @author bajdcc
 */
public class ModuleNetWebHandler implements Runnable {

	private SelectionKey key;
	private String requestHeader;

	public ModuleNetWebHandler(String requestHeader, SelectionKey key) {
		this.key = key;
		this.requestHeader = requestHeader;
	}

	@Override
	public void run() {
		final String endl = "\r\n";
		String html = "<html><head><title>jMiniLang Web Server</title></head><body><h1>jMiniLang Web Server -- bajdcc</h1><h2><pre>"
				+ requestHeader + "</pre></h2></body></html>";
		StringBuilder sb = new StringBuilder();
		sb.append("HTTP/1.1 200 OK").append(endl);
		sb.append("Server: jMiniLang Web Server").append(endl);
		sb.append("Content-Type: text/html").append(endl);
		sb.append("Content-Length: ").append(html.getBytes(UTF_8).length);
		sb.append(endl).append(endl);
		sb.append(html);
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.put(sb.toString().getBytes(UTF_8));
		buffer.flip();
		Selector selector = key.selector();
		SocketChannel channel = (SocketChannel) key.channel();
		try {
			channel.register(selector, SelectionKey.OP_WRITE);
			channel.write(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
