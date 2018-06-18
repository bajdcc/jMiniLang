package com.bajdcc.LALR1.interpret.module.web;

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeMap;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 【模块】请求上下文
 *
 * @author bajdcc
 */
public class ModuleNetWebContext {

	private Semaphore sem; // 多线程同步只能用信号量
	private SelectionKey selectionKey;
	private String header;
	private String response = "";
	private String MIME = "application/octet-stream";
	private int code = 200;
	private int content_type = 0;

	private String method, uri, protocol, version;
	private Map<String, String> mapReqHeaders = new HashMap<>();
	private Map<String, String> mapRespHeaders = new HashMap<>();

	public ModuleNetWebContext(SelectionKey selectionKey) {
		this.selectionKey = selectionKey;
	}

	/**
	 * 请求的结构：
	 * headers(map), method, uri, protocol, version
	 * @return HTTP请求
	 */
	public RuntimeMap getReqHeader() {
		RuntimeMap req = new RuntimeMap();
		RuntimeMap headers = new RuntimeMap();
		mapReqHeaders.forEach((key, value) -> headers.put(key, new RuntimeObject(value)));
		req.put("headers", new RuntimeObject(headers));
		req.put("method", new RuntimeObject(method));
		req.put("uri", new RuntimeObject(uri));
		req.put("version", new RuntimeObject(version));
		req.put("protocol", new RuntimeObject(protocol));
		String url = String.format("%s://%s%s", protocol, mapReqHeaders.get("Host"), uri);
		req.put("url", new RuntimeObject(url));
		try {
			URL u = new URL(url);
			req.put("port", new RuntimeObject(BigInteger.valueOf(u.getPort())));
			req.put("host", new RuntimeObject(u.getHost()));
			req.put("path", new RuntimeObject(u.getPath()));
			req.put("query", new RuntimeObject(u.getQuery()));
			req.put("authority", new RuntimeObject(u.getAuthority()));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return req;
	}

	public void setReqHeader(String header) {
		this.header = header;
		initHeader();
	}

	public RuntimeMap getRespHeader() {
		RuntimeMap resp = new RuntimeMap();
		mapRespHeaders.forEach((key, value) -> resp.put(key, new RuntimeObject(value)));
		return resp;
	}

	public Map<String, String> getRespHeaderMap() {
		return mapRespHeaders;
	}

	public void setRespHeader(RuntimeMap map) {
		map.forEach((key, value) -> mapRespHeaders.put(key, String.valueOf(value.getObj())));
	}

	private void initHeader() {
		final Pattern re1 = Pattern.compile("([A-Z]+) ([^ ]+) ([A-Z]+)/(\\d\\.\\d)");
		String[] headers = header.split("\r\n");
		Matcher m = re1.matcher(headers[0]);
		if (m.find()) {
			method = m.group(1).trim();
			uri = m.group(2).trim();
			protocol = m.group(3).trim().toLowerCase();
			version = m.group(4).trim();
		}
		for (int i = 1; i < headers.length; i++) {
			int colon = headers[i].indexOf(':');
			if (colon != -1) {
				String key = headers[i].substring(0, colon);
				String value = headers[i].substring(colon + 1);
				mapReqHeaders.put(key.trim(), value.trim());
			}
		}
		mapRespHeaders.put("Server", "jMiniLang Server");
	}

	public String getRespText() {
		return String.format("%s/%s %d %s", protocol, version, code, ModuleNetWebHelper.getStatusCodeText(code));
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getMIME() {
		return MIME;
	}

	public void setMIME(String MIME) {
		this.MIME = MIME;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getContentType() {
		return content_type;
	}

	public void setContentType(int type) {
		this.content_type = type;
	}

	public SelectionKey getKey() {
		return selectionKey;
	}

	public Semaphore block() {
		sem = new Semaphore(0);
		return sem;
	}

	public void unblock() {
		sem.release();
	}
}
