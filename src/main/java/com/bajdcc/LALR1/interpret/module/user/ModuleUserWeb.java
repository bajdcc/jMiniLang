package com.bajdcc.LALR1.interpret.module.user;

import com.bajdcc.LALR1.grammar.Grammar;
import com.bajdcc.LALR1.grammar.runtime.*;
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray;
import com.bajdcc.LALR1.interpret.module.IInterpreterModule;
import com.bajdcc.LALR1.interpret.module.ModuleBase;
import com.bajdcc.LALR1.interpret.module.ModuleNet;
import com.bajdcc.LALR1.interpret.module.web.ModuleNetWebContext;
import com.bajdcc.LALR1.interpret.module.web.ModuleNetWebServer;
import com.bajdcc.util.ResourceLoader;
import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.util.List;

/**
 * 【模块】用户态-网页服务器
 *
 * @author bajdcc
 */
public class ModuleUserWeb implements IInterpreterModule {

	private static ModuleUserWeb instance = new ModuleUserWeb();
	private RuntimeCodePage runtimeCodePage;
	private static Logger logger = Logger.getLogger("web");

	public static ModuleUserWeb getInstance() {
		return instance;
	}

	@Override
	public String getModuleName() {
		return "user.web";
	}

	@Override
	public String getModuleCode() {
		return ResourceLoader.load(getClass());
	}

	@Override
	public RuntimeCodePage getCodePage() throws Exception {
		if (runtimeCodePage != null)
			return runtimeCodePage;

		String base = ResourceLoader.load(getClass());

		Grammar grammar = new Grammar(base);
		RuntimeCodePage page = grammar.getCodePage();
		IRuntimeDebugInfo info = page.getInfo();

		info.addExternalFunc("g_web_get_context", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取请求上下文";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				ModuleNetWebServer server = ModuleNet.getInstance().getWebServer();
				if (server != null) {
					ModuleNetWebContext ctx = server.dequeue();
					if (ctx != null) {
						RuntimeArray array = new RuntimeArray();
						array.add(new RuntimeObject(BigInteger.valueOf(ctx.getCode())));
						array.add(new RuntimeObject(ctx.getHeader()));
						array.add(new RuntimeObject(ctx.getResponse()));
						array.add(new RuntimeObject(ctx));
						return new RuntimeObject(array);
					}
				}
				return null;
			}
		});
		info.addExternalFunc("g_web_set_context", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "设置请求上下文";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kArray};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				RuntimeArray array = (RuntimeArray) args.get(0).getObj();
				ModuleNetWebContext ctx = (ModuleNetWebContext) array.get(3).getObj();
				ctx.setCode(((BigInteger) array.get(0).getObj()).intValue());
				ctx.setHeader(String.valueOf(array.get(1).getObj()));
				ctx.setResponse(String.valueOf(array.get(2).getObj()));
				ctx.unblock();
				return null;
			}
		});

		return runtimeCodePage = page;
	}
}