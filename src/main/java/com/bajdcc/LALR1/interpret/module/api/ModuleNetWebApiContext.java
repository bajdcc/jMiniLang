package com.bajdcc.LALR1.interpret.module.api;

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeMap;
import com.bajdcc.LALR1.interpret.module.web.ModuleNetWebHelper;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 【模块】API请求上下文
 *
 * @author bajdcc
 */
public class ModuleNetWebApiContext {

	private Semaphore sem; // 多线程同步只能用信号量

	private RuntimeMap req = new RuntimeMap();
	private RuntimeObject resp = new RuntimeObject(null);

	public ModuleNetWebApiContext() {
		req.put("__ctx__", new RuntimeObject(this));
		req.put("resp", new RuntimeObject(null));
	}

	public ModuleNetWebApiContext(String route) {
		this();
		req.put("route", new RuntimeObject(route));
	}

	public ModuleNetWebApiContext(String route, Map<String,String> params) {
		this(route);
		if (params != null)
			params.forEach((key, value) -> req.put(key, new RuntimeObject(value)));
	}

	public boolean block() {
		sem = new Semaphore(0);
		try {
			if (!sem.tryAcquire(3, TimeUnit.SECONDS)) {
				throw new TimeoutException("Timed out.");
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void unblock() {
		try {
			while (sem == null) {
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sem.release();
	}

	public RuntimeMap getReq() {
		return req;
	}

	public void setReq(RuntimeMap req) {
		this.req = req;
	}

	public RuntimeObject getResp() {
		return resp;
	}

	public void setResp(RuntimeObject resp) {
		this.resp = resp;
	}
}
