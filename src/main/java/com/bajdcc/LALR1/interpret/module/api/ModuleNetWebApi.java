package com.bajdcc.LALR1.interpret.module.api;

import com.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import com.bajdcc.LALR1.grammar.runtime.RuntimeProcess;
import org.apache.log4j.Logger;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 【模块】网页接口服务
 *
 * @author bajdcc
 */
public class ModuleNetWebApi {

	private static Logger logger = Logger.getLogger("api");

	private Deque<ModuleNetWebApiContext> queue = new ConcurrentLinkedDeque<>();

	public ModuleNetWebApiContext dequeue() {
		return queue.poll();
	}

	public RuntimeObject peekRequest() {
		ModuleNetWebApiContext ctx = queue.peek();
		if (ctx == null)
			return null;
		return new RuntimeObject(ctx.getReq());
	}

	public Object sendRequest(String route) {
		return sendRequest(route, null);
	}

	public Object sendRequest(String route, Map<String, String> params) {
		ModuleNetWebApiContext ctx = new ModuleNetWebApiContext(route, params);
		queue.add(ctx);
		RuntimeProcess.writePipe("int#3", "A");
		if (ctx.block()) {
			return ModuleNetWebApiHelper.toJsonObject(new RuntimeObject(ctx.getResp()));
		}
		return null;
	}
}
