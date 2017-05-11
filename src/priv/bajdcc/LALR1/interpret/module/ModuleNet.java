package priv.bajdcc.LALR1.interpret.module;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.*;
import priv.bajdcc.LALR1.grammar.runtime.data.RuntimeArray;
import priv.bajdcc.LALR1.grammar.runtime.data.RuntimeMap;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * 【模块】网络
 *
 * @author bajdcc
 */
public class ModuleNet implements IInterpreterModule {

	private static Logger logger = Logger.getLogger("net");
	private static ModuleNet instance = new ModuleNet();

	public static ModuleNet getInstance() {
		return instance;
	}

	@Override
	public String getModuleName() {
		return "sys.net";
	}

	@Override
	public RuntimeCodePage getCodePage() throws Exception {
		String base = "import \"sys.base\";\n" +
				"import \"sys.list\";\n" +
				"import \"sys.proc\";\n" +
				"import \"sys.string\";\n" +
				"\n";

		Grammar grammar = new Grammar(base);
		RuntimeCodePage page = grammar.getCodePage();
		IRuntimeDebugInfo info = page.getInfo();
		buildRemoteMethods(info);

		return page;
	}

	private void buildRemoteMethods(IRuntimeDebugInfo info) {
		info.addExternalFunc("g_net_get", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "HTTP GET";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				String text = "";
				String txt = String.valueOf(args.get(0).getObj());
				logger.debug("Request url: " + txt);
				try {
					URL url = new URL(txt);
					URLConnection urlConnection = url.openConnection(); // 打开连接
					BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8")); // 获取输入流
					String line = null;
					StringBuilder sb = new StringBuilder();
					while ((line = br.readLine()) != null) {
						sb.append(line).append("\n");
					}
					text = sb.toString();
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return new RuntimeObject(text);
			}
		});
		info.addExternalFunc("g_net_get_json", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "HTTP GET - json";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				String text = "";
				String txt = String.valueOf(args.get(0).getObj());
				logger.debug("Request url(json): " + txt);
				try {
					URL url = new URL(txt);
					URLConnection urlConnection = url.openConnection(); // 打开连接
					BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8")); // 获取输入流
					String line = null;
					StringBuilder sb = new StringBuilder();
					while ((line = br.readLine()) != null) {
						sb.append(line).append("\n");
					}
					text = sb.toString();
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return parseJson(text);
			}
		});
	}

	private static RuntimeObject parseJson(String text) {
		try {
			Object o = JSON.parseObject(text);
			return parseInternal(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RuntimeObject("json - parse error");
	}

	private static RuntimeObject parseInternal(Object o) {
		if (o instanceof JSONObject) {
			JSONObject obj = (JSONObject) o;
			RuntimeMap map = new RuntimeMap();
			obj.forEach((key, value) -> {
				map.put(key, parseInternal(value));
			});
			return new RuntimeObject(map);
		} else if (o instanceof JSONArray) {
			JSONArray obj = (JSONArray) o;
			RuntimeArray arr = new RuntimeArray();
			obj.forEach((key) -> {
				arr.add(parseInternal(key));
			});
			return new RuntimeObject(arr);
		} else {
			return new RuntimeObject(o);
		}
	}
}
