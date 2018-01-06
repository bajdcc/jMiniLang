package priv.bajdcc.LALR1.interpret.module;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.*;
import priv.bajdcc.LALR1.grammar.runtime.data.RuntimeArray;
import priv.bajdcc.LALR1.grammar.runtime.data.RuntimeMap;
import priv.bajdcc.LALR1.interpret.module.net.ModuleNetClient;
import priv.bajdcc.LALR1.interpret.module.net.ModuleNetServer;
import priv.bajdcc.util.ResourceLoader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
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

	private static final int MSG_QUERY_TIME = 15;
	private static final int MSG_SEND_TIME = 5;
	private RuntimeCodePage runtimeCodePage;
	private ModuleNetServer server;
	private ModuleNetClient client;
	private String lastError = "";

	public static ModuleNet getInstance() {
		return instance;
	}

	@Override
	public String getModuleName() {
		return "sys.net";
	}

	@Override
	public RuntimeCodePage getCodePage() throws Exception {
		if (runtimeCodePage != null)
			return runtimeCodePage;

		String base = ResourceLoader.load(getClass());

		Grammar grammar = new Grammar(base);
		RuntimeCodePage page = grammar.getCodePage();
		IRuntimeDebugInfo info = page.getInfo();
		buildRemoteMethods(info);

		return runtimeCodePage = page;
	}

	private static RuntimeObject parseJsonSafe(String text) {
		try {
			Object o = JSON.parseObject(text);
			return parseInternal(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
		// -----------------------------------
		// server
		info.addExternalFunc("g_net_msg_create_server_internal", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "MSG SERVER";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kInt};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) {
				if (getServer() != null || getClient() != null)
					return new RuntimeObject(false);
				int port = ((BigInteger) args.get(0).getObj()).intValue();
				setServer(new ModuleNetServer(port));
				getServer().start();
				return new RuntimeObject(true);
			}
		});
		info.addExternalFunc("g_net_msg_shutdown_server", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "MSG SERVER SHUTDOWN";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) {
				if (getServer() == null)
					return new RuntimeObject(false);
				getServer().exit();
				return new RuntimeObject(true);
			}
		});
		info.addExternalFunc("g_net_msg_get_server_status", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "MSG SERVER GET STATUS";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) {
				if (getServer() != null) {
					ModuleNetServer.Status s = getServer().getStatus();
					if (s == ModuleNetServer.Status.ERROR) {
						lastError = getServer().getError();
						setServer(null);
					}
					status.getService().getProcessService().sleep(status.getPid(), MSG_QUERY_TIME);
					return new RuntimeObject(BigInteger.valueOf(s.ordinal()));
				}
				return new RuntimeObject(BigInteger.valueOf(ModuleNetServer.Status.NULL.ordinal()));
			}
		});
		// server
		// -----------------------------------
		// client
		info.addExternalFunc("g_net_msg_create_client_internal", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "MSG CLIENT";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) {
				if (getServer() != null || getClient() != null)
					return new RuntimeObject(false);
				String addr = String.valueOf(args.get(0).getObj());
				setClient(new ModuleNetClient(addr));
				getClient().start();
				return new RuntimeObject(true);
			}
		});
		info.addExternalFunc("g_net_msg_shutdown_client", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "MSG CLIENT SHUTDOWN";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) {
				if (getClient() == null)
					return new RuntimeObject(false);
				getClient().exit();
				return new RuntimeObject(true);
			}
		});
		info.addExternalFunc("g_net_msg_get_client_status", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "MSG CLIENT GET STATUS";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) {
				if (getClient() != null) {
					ModuleNetClient.Status s = getClient().getStatus();
					if (s == ModuleNetClient.Status.ERROR) {
						lastError = getClient().getError();
						setClient(null);
					}
					status.getService().getProcessService().sleep(status.getPid(), MSG_QUERY_TIME);
					return new RuntimeObject(BigInteger.valueOf(s.ordinal()));
				}
				return new RuntimeObject(BigInteger.valueOf(ModuleNetClient.Status.NULL.ordinal()));
			}
		});
		info.addExternalFunc("g_net_msg_client_send", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "MSG CLIENT SEND";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) {
				if (getClient() != null) {
					ModuleNetClient.Status s = getClient().getStatus();
					if (s == ModuleNetClient.Status.RUNNING) {
						status.getService().getProcessService().sleep(status.getPid(), MSG_SEND_TIME);
						getClient().send(String.valueOf(args.get(0).getObj()));
						return new RuntimeObject(true);
					}
				}
				return new RuntimeObject(false);
			}
		});
		// client
		// -----------------------------------
		info.addExternalFunc("g_net_msg_get_error", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "MSG SERVER GET ERROR";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) {
				return new RuntimeObject(lastError);
			}
		});
		info.addExternalFunc("g_net_msg_get_server_msg", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "MSG SERVER GET MESSAGE";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) {
				return new RuntimeObject(getServer().getMessage());
			}
		});
		info.addExternalFunc("g_net_msg_get_client_msg", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "MSG CLIENT GET MESSAGE";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) {
				return new RuntimeObject(getClient().getMessage());
			}
		});
		info.addExternalFunc("g_net_parse_json", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "Parse json";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) {
				return parseJsonSafe(String.valueOf(args.get(0).getObj()));
			}
		});
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

	public ModuleNetServer getServer() {
		return server;
	}

	public void setServer(ModuleNetServer server) {
		this.server = server;
	}

	public ModuleNetClient getClient() {
		return client;
	}

	public void setClient(ModuleNetClient client) {
		this.client = client;
	}
}