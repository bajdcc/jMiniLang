package priv.bajdcc.LALR1.interpret.module;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.*;
import priv.bajdcc.LALR1.grammar.runtime.data.RuntimeArray;
import priv.bajdcc.LALR1.grammar.runtime.data.RuntimeMap;
import priv.bajdcc.LALR1.interpret.module.net.ModuleNetClient;
import priv.bajdcc.LALR1.interpret.module.net.ModuleNetServer;
import priv.bajdcc.util.ResourceLoader;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
					urlConnection.setRequestProperty("accept", "*/*");
					urlConnection.setRequestProperty("connection", "Keep-Alive");
					urlConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
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
		info.addExternalFunc("g_net_post_json", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "HTTP POST - json";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString, RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				String text = "";
				String txt = String.valueOf(args.get(0).getObj());
				String data = String.valueOf(args.get(1).getObj());
				logger.debug("Request url(json): " + txt);
				try {
					URL url = new URL(txt);
					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); // 打开连接
					urlConnection.setDoOutput(true);
					urlConnection.setDoInput(true);
					urlConnection.setRequestMethod("POST");
					urlConnection.setRequestProperty("accept", "*/*");
					urlConnection.setRequestProperty("connection", "Keep-Alive");
					urlConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
					PrintWriter out = new PrintWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "utf-8"));
					out.print(data);
					out.flush();
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
		info.addExternalFunc("g_net_msg_client_send_with_origin", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "MSG CLIENT SEND(ORIGIN)";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString, RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				if (getClient() != null) {
					ModuleNetClient.Status s = getClient().getStatus();
					if (s == ModuleNetClient.Status.RUNNING) {
						status.getService().getProcessService().sleep(status.getPid(), MSG_SEND_TIME);
						getClient().send(String.valueOf(args.get(0).getObj()), String.valueOf(args.get(1).getObj()));
						return new RuntimeObject(true);
					}
				}
				return new RuntimeObject(false);
			}
		});
		info.addExternalFunc("g_net_msg_server_send", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "MSG SERVER SEND";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				if (getServer() != null) {
					ModuleNetServer.Status s = getServer().getStatus();
					if (s == ModuleNetServer.Status.RUNNING) {
						status.getService().getProcessService().sleep(status.getPid(), MSG_SEND_TIME);
						getServer().send(String.valueOf(args.get(0).getObj()));
						return new RuntimeObject(true);
					}
				}
				return new RuntimeObject(false);
			}
		});
		info.addExternalFunc("g_net_msg_server_send_error", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "MSG SERVER SEND ERROR";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				if (getServer() != null) {
					ModuleNetServer.Status s = getServer().getStatus();
					if (s == ModuleNetServer.Status.RUNNING) {
						status.getService().getProcessService().sleep(status.getPid(), MSG_SEND_TIME);
						getServer().send_error(String.valueOf(args.get(0).getObj()));
						return new RuntimeObject(true);
					}
				}
				return new RuntimeObject(false);
			}
		});
		info.addExternalFunc("g_net_msg_server_send_with_origin", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "MSG SERVER SEND(ORIGIN)";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString, RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				if (getServer() != null) {
					ModuleNetServer.Status s = getServer().getStatus();
					if (s == ModuleNetServer.Status.RUNNING) {
						status.getService().getProcessService().sleep(status.getPid(), MSG_SEND_TIME);
						getServer().send(String.valueOf(args.get(0).getObj()), String.valueOf(args.get(1).getObj()));
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
				return new RuntimeObject(getServer() == null ? null : getServer().getMessage());
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
				return new RuntimeObject(getClient() == null ? null : getClient().getMessage());
			}
		});
		info.addExternalFunc("g_net_msg_get_client_addr", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "MSG CLIENT GET ADDRESS";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(getClient() == null ? null : getClient().getAddr());
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
		info.addExternalFunc("g_net_get_rss", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "RSS GET(BAIDU)";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@SuppressWarnings("unchecked")
			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				String text = "";
				String txt = String.valueOf(args.get(0).getObj());
				logger.debug("Request url(rss): " + txt);
				RuntimeArray array = new RuntimeArray();
				final Pattern pattern = Pattern.compile("<br>(.*)<br />");
				try {
					SAXReader reader = new SAXReader();
					Document document = reader.read(txt);
					Node title = document.selectSingleNode("//title");
					array.add(new RuntimeObject(title.getText()));
					List<Node> list = document.selectNodes("//item");
					for (Node item : list) {
						String itemTitle = item.valueOf("title");
						array.add(new RuntimeObject(itemTitle));
						String itemDescription = item.valueOf("description");
						Matcher matcher = pattern.matcher(itemDescription);
						if (matcher.find()) {
							array.add(new RuntimeObject(matcher.group(1)));
						} else {
							array.add(new RuntimeObject(itemDescription.replace("<br />", "")));
						}
					}
					return new RuntimeObject(array);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return new RuntimeObject(array);
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
			if (o instanceof Integer) {
				return new RuntimeObject(BigInteger.valueOf((int) o));
			} else if (o instanceof Float) {
				return new RuntimeObject(BigDecimal.valueOf((float) o));
			}
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
