package com.bajdcc.LALR1.interpret.module.user;

import com.bajdcc.LALR1.grammar.Grammar;
import com.bajdcc.LALR1.grammar.runtime.*;
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray;
import com.bajdcc.LALR1.interpret.module.*;
import com.bajdcc.util.ResourceLoader;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 【模块】用户态-基类
 *
 * @author bajdcc
 */
public class ModuleUserBase implements IInterpreterModule {

	private static ModuleUserBase instance = new ModuleUserBase();
	private RuntimeCodePage runtimeCodePage;
	private static Logger logger = Logger.getLogger("user");

	public static ModuleUserBase getInstance() {
		return instance;
	}

	@Override
	public String getModuleName() {
		return "user.base";
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

		importFromBase(info, ModuleBase.getInstance().getCodePage().getInfo());
		importFromString(info, ModuleString.getInstance().getCodePage().getInfo());
		importFromTask(info, ModuleTask.getInstance().getCodePage().getInfo());
		importFromList(info, ModuleList.getInstance().getCodePage().getInfo());
		importFromProc(info, ModuleProc.getInstance().getCodePage().getInfo());
		importFromNet(info, ModuleNet.getInstance().getCodePage().getInfo());
		importFromFile(info, ModuleFile.getInstance().getCodePage().getInfo());

		return runtimeCodePage = page;
	}

	private static void importFromBase(IRuntimeDebugInfo info, IRuntimeDebugInfo refer) {
		String[] importValue = new String[]{
				"g_null", "g_minus_1", "g_true", "g_false", "g_endl", "g_nullptr"
		};
		for (String key : importValue) {
			info.addExternalValue(key, refer.getValueCallByName(key));
		}

		String[] importFunc = new String[]{
				"g_is_null", "g_set_debug", "g_not_null",
				"g_to_string", "g_new", "g_doc", "g_get_type", "g_get_type_ordinal", "g_type",
				"g_args_count", "g_args_index", "g_get_timestamp"
		};
		for (String key : importFunc) {
			info.addExternalFunc(key, refer.getExecCallByName(key));
		}

		info.addExternalFunc("g_print", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "标准输出";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kObject};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				logger.info(args.get(0).getObj());
				return null;
			}
		});
		info.addExternalFunc("g_print_info", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "标准输出";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kObject};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				logger.info(args.get(0));
				return null;
			}
		});
		info.addExternalFunc("g_printn", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "标准输出并换行";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kObject};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				Object[] info = status.getProcInfo();
				logger.info(String.format("#%03d [%s] %s", status.getPid(), info[3], args.get(0).getObj()));
				return null;
			}
		});
		info.addExternalFunc("g_printdn", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "调试输出并换行";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kObject};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				Object[] info = status.getProcInfo();
				logger.debug(String.format("#%03d [%s] %s", status.getPid(), info[3], args.get(0).getObj()));
				return null;
			}
		});
		info.addExternalFunc("g_print_err", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "错误输出";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kObject};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				logger.error(args.get(0).getObj());
				return null;
			}
		});
		info.addExternalFunc("g_put", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "流输出";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				String text = String.valueOf(args.get(0).getObj());
				status.getRing3().put(text);
				return null;
			}
		});
		info.addExternalFunc("g_sleep", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "进程睡眠";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kInt};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				BigInteger turn = (BigInteger) args.get(0).getObj();
				int time = turn.intValue();
				return new RuntimeObject(BigInteger.valueOf(
						status.getService().getProcessService().sleep(status.getPid(), time > 0 ? time : 0)));
			}
		});
		info.addExternalFunc("g_env_get", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取系统变量";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(String.valueOf(System.getProperty(String.valueOf(args.get(0).getObj()))));
			}
		});
		info.addExternalFunc("g_load_resource", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "读资源文件";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				String filename = String.valueOf(args.get(0).getObj());
				InputStream is = getClass().getResourceAsStream(filename);
				if (is == null)
					return null;
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, UTF_8));
				String content = reader.lines().collect(Collectors.joining(System.lineSeparator()));
				return new RuntimeObject(content);
			}
		});
		info.addExternalFunc("g_disable_result", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "禁用输出结果";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				status.disableResult();
				return null;
			}
		});
		info.addExternalFunc("g_info_get_doc", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取所有文档";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(status.getAllDocs());
			}
		});
	}

	private static void importFromString(IRuntimeDebugInfo info, IRuntimeDebugInfo refer) {
		String[] importFunc = new String[]{
				"g_string_replace","g_string_split","g_string_splitn","g_string_trim","g_string_length",
				"g_string_empty","g_string_get","g_string_regex","g_string_build","g_string_atoi",
				"g_string_atoi_s","g_string_join_array","g_string_toupper","g_string_tolower",
				"g_string_rep","g_string_to_number","g_string_equal","g_string_not_equal","g_string_start_with",
				"g_string_end_with","g_string_substr","g_string_left","g_string_right"
		};
		for (String key : importFunc) {
			info.addExternalFunc(key, refer.getExecCallByName(key));
		}
	}

	private static void importFromTask(IRuntimeDebugInfo info, IRuntimeDebugInfo refer) {
		info.addExternalFunc("g_env_get_guid", refer.getExecCallByName("g_task_get_guid"));
		info.addExternalFunc("g_res_get_speed", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "管道列表";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				long speed = BigDecimal.valueOf(status.getService().getProcessService().getSpeed()).longValue();
				if (speed > 1000000L) {
					return new RuntimeObject(String.valueOf(speed / 1000000L) + "M");
				} else if (speed > 1000L) {
					return new RuntimeObject(String.valueOf(speed / 1000L) + "K");
				}
				return new RuntimeObject(String.valueOf(speed));
			}
		});
		info.addExternalFunc("g_res_get_pipe", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "管道列表";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(status.getService().getPipeService().stat(true));
			}
		});
		info.addExternalFunc("g_res_get_pipe_size", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "管道列表数量";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(BigInteger.valueOf(status.getService().getPipeService().size()));
			}
		});
		info.addExternalFunc("g_res_get_share", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "共享列表";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(status.getService().getShareService().stat(true));
			}
		});
		info.addExternalFunc("g_res_get_share_size", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "共享列表数量";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(BigInteger.valueOf(status.getService().getShareService().size()));
			}
		});
	}

	private static void importFromList(IRuntimeDebugInfo info, IRuntimeDebugInfo refer) {
		String[] importFunc = new String[]{
				"g_array_add", "g_array_contains", "g_array_append", "g_array_insert",
				"g_array_set", "g_array_pop", "g_array_clear", "g_array_reverse", "g_array_get",
				"g_array_get_ex", "g_array_size", "g_array_remove", "g_array_delete", "g_array_empty",
				"g_array_fill", "g_map_keys", "g_map_values", "g_map_put", "g_map_contains",
				"g_map_get", "g_map_size", "g_map_remove", "g_map_clear", "g_map_empty",
				"g_array_range"
		};
		for (String key : importFunc) {
			info.addExternalFunc(key, refer.getExecCallByName(key));
		}
	}

	private static void importFromProc(IRuntimeDebugInfo info, IRuntimeDebugInfo refer) {
		info.addExternalFunc("g_res_get_proc", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "进程列表";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				List<Object[]> info = status.getService().getProcessService().getProcInfoCache();
				RuntimeArray array = new RuntimeArray();
				for (Object[] i : info) {
					RuntimeArray item = new RuntimeArray();
					for (int j = 0; j < i.length; j++) {
						item.add(new RuntimeObject(i[j]));
					}
					array.add(new RuntimeObject(item));
				}
				return new RuntimeObject(array);
			}
		});
		info.addExternalFunc("g_res_get_proc_size", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "进程列表数量";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(BigInteger.valueOf(status.getService().getProcessService().getProcInfoCache().size()));
			}
		});
	}

	private static void importFromNet(IRuntimeDebugInfo info, IRuntimeDebugInfo refer) {
		info.addExternalFunc("g_info_get_ip", refer.getExecCallByName("g_web_get_ip"));
		info.addExternalFunc("g_info_get_hostname", refer.getExecCallByName("g_web_get_hostname"));
	}

	private static void importFromFile(IRuntimeDebugInfo info, IRuntimeDebugInfo refer) {
		info.addExternalFunc("g_res_get_file", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "文件列表";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(status.getService().getFileService().stat(true));
			}
		});
		info.addExternalFunc("g_res_get_file_size", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "文件列表数量";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(status.getService().getFileService().size());
			}
		});
		info.addExternalFunc("g_res_get_vfs", refer.getExecCallByName("g_read_file_vfs_utf8"));
		info.addExternalFunc("g_res_get_vfs_list", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取VFS列表";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(status.getService().getFileService().getVfsList(true));
			}
		});
		info.addExternalFunc("g_res_get_vfs_size", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "VFS列表数量";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(BigInteger.valueOf(status.getService().getFileService().getVfsListSize()));
			}
		});
	}
}