package com.bajdcc.LALR1.interpret.module.user;

import com.bajdcc.LALR1.grammar.Grammar;
import com.bajdcc.LALR1.grammar.runtime.*;
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeMap;
import com.bajdcc.LALR1.interpret.module.IInterpreterModule;
import com.bajdcc.LALR1.interpret.module.ModuleNet;
import com.bajdcc.LALR1.interpret.module.api.ModuleNetWebApi;
import com.bajdcc.LALR1.interpret.module.api.ModuleNetWebApiContext;
import com.bajdcc.LALR1.interpret.module.web.ModuleNetWebContext;
import com.bajdcc.LALR1.interpret.module.web.ModuleNetWebServer;
import com.bajdcc.util.ResourceLoader;
import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
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
	private HtmlRenderer renderer;
	private Parser parser;

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
						RuntimeMap map = new RuntimeMap();
						map.put("code", new RuntimeObject(BigInteger.valueOf(ctx.getCode())));
						map.put("request", new RuntimeObject(ctx.getReqHeader()));
						map.put("response", new RuntimeObject(ctx.getResponse()));
						map.put("header", new RuntimeObject(ctx.getRespHeader()));
						map.put("mime", new RuntimeObject(ctx.getMime()));
						map.put("content_type", new RuntimeObject(BigInteger.valueOf(ctx.getContentType())));
						map.put("__ctx__", new RuntimeObject(ctx));
						return new RuntimeObject(map);
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
				return new RuntimeObjectType[]{RuntimeObjectType.kMap};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				RuntimeMap map = (RuntimeMap) args.get(0).getObj();
				ModuleNetWebContext ctx = (ModuleNetWebContext) map.get("__ctx__").getObj();
				ctx.setCode(((BigInteger) map.get("code").getObj()).intValue());
				ctx.setResponse(String.valueOf(map.get("response").getObj()));
				ctx.setRespHeader((RuntimeMap) map.get("header").getObj());
				ctx.setMime(String.valueOf(map.get("mime").getObj()));
				ctx.setContentType(((BigInteger) map.get("content_type").getObj()).intValue());
				ctx.unblock();
				return null;
			}
		});
		info.addExternalFunc("g_web_get_api", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取API请求上下文";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				ModuleNetWebApi api = ModuleNet.getInstance().getWebApi();
				if (api != null) {
					ModuleNetWebApiContext ctx = api.dequeue();
					if (ctx != null) {
						return new RuntimeObject(ctx.getReq());
					}
				}
				return null;
			}
		});
		info.addExternalFunc("g_web_set_api", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "设置API请求上下文";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kMap};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				RuntimeMap map = (RuntimeMap) args.get(0).getObj();
				ModuleNetWebApiContext ctx = (ModuleNetWebApiContext) map.get("__ctx__").getObj();
				ctx.setResp(map.get("resp"));
				ctx.unblock();
				return null;
			}
		});
		MutableDataSet options = new MutableDataSet();
		options.setFrom(ParserEmulationProfile.MARKDOWN);
		options.set(Parser.EXTENSIONS, Collections.singletonList(TablesExtension.create()));
		parser = Parser.builder(options).build();
		renderer = HtmlRenderer.builder(options).build();
		info.addExternalFunc("g_web_markdown", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "MD转HTML";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				String md = String.valueOf(args.get(0).getObj());
				Node document = parser.parse(md);
				String html = renderer.render(document);
				return new RuntimeObject(html);
			}
		});

		return runtimeCodePage = page;
	}
}