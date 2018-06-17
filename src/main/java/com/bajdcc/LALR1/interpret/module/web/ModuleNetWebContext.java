package com.bajdcc.LALR1.interpret.module.web;

import java.nio.channels.SelectionKey;
import java.util.concurrent.Semaphore;

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
	private int code = 200;

	public ModuleNetWebContext(SelectionKey selectionKey) {
		this.selectionKey = selectionKey;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
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
