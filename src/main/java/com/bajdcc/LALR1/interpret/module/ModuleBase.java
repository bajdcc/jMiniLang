package com.bajdcc.LALR1.interpret.module;

import com.bajdcc.LALR1.grammar.Grammar;
import com.bajdcc.LALR1.grammar.runtime.*;
import com.bajdcc.LALR1.grammar.runtime.RuntimeException;
import com.bajdcc.LALR1.grammar.runtime.RuntimeException.RuntimeError;
import com.bajdcc.util.ResourceLoader;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

/**
 * 【模块】基本模块
 *
 * @author bajdcc
 */
public class ModuleBase implements IInterpreterModule {

	private static ModuleBase instance = new ModuleBase();
	private static Logger logger = Logger.getLogger("console");
	private RuntimeCodePage runtimeCodePage;

	public static ModuleBase getInstance() {
		return instance;
	}

	@Override
	public String getModuleName() {
		return "sys.base";
	}

	@Override
	public String getModuleCode() {
		return ResourceLoader.INSTANCE.load(getClass());
	}

	@Override
	public RuntimeCodePage getCodePage() throws Exception {
		if (runtimeCodePage != null)
			return runtimeCodePage;

		String base = ResourceLoader.INSTANCE.load(getClass());

		Grammar grammar = new Grammar(base);
		RuntimeCodePage page = grammar.getCodePage();
		IRuntimeDebugInfo info = page.getInfo();
		info.addExternalValue("g_null", () -> new RuntimeObject(null));
		info.addExternalValue("g_true", () -> new RuntimeObject(true));
		info.addExternalValue("g_false", () -> new RuntimeObject(false));
		final String NEWLINE = System.lineSeparator();
		info.addExternalValue("g_endl", () -> new RuntimeObject(NEWLINE));
		info.addExternalValue("g_nullptr", () -> new RuntimeObject(-1));
		info.addExternalFunc("g_is_null", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "判断是否为空";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kObject};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(args.get(0).getObj() == null);
			}
		});
		info.addExternalFunc("g_is_valid_handle", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "判断句柄合法";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kPtr};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject((args.get(0).getInt()) >= 0);
			}
		});
		info.addExternalFunc("g_set_flag", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "设置对象属性";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kObject, RuntimeObjectType.kInt};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				long flag = args.get(1).getLong();
				args.get(0).setFlag(flag);
				return args.get(0);
			}
		});
		info.addExternalFunc("g_get_flag", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取对象属性";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kObject};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(args.get(0).getFlag());
			}
		});
		info.addExternalFunc("g_is_flag", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "判断对象属性";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kObject, RuntimeObjectType.kInt};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				long flag = args.get(1).getLong();
				return new RuntimeObject(args.get(0).getFlag() == flag);
			}
		});
		info.addExternalFunc("g_set_debug", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "输出调试信息";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kBool};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				boolean debug = args.get(0).getBool();
				status.getService().getProcessService().setDebug(status.getPid(), debug);
				return null;
			}
		});
		info.addExternalFunc("g_set_rapid", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "设置高速模式";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kBool};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				boolean mode = args.get(0).getBool();
				status.getService().getProcessService().setHighSpeed(mode);
				return null;
			}
		});
		info.addExternalFunc("g_not_null", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "判断是否有效";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kObject};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(args.get(0).getObj() != null);
			}
		});
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
		info.addExternalFunc("g_to_string", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "将对象转换成字符串";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kObject};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				RuntimeObject obj = args.get(0);
				if (obj == null) {
					return new RuntimeObject(null);
				}
				return new RuntimeObject(String.valueOf(args.get(0).getObj()));
			}
		});
		info.addExternalFunc("g_new", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "深拷贝";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kObject};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return args.get(0).clone();
			}
		});
		info.addExternalFunc("g_doc", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "文档";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(status.getHelpString(args.get(0)
						.getString()));
			}
		});
		info.addExternalFunc("g_get_type", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取类型";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kObject};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(args.get(0).getTypeName());
			}
		});
		info.addExternalFunc("g_get_type_ordinal", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取类型(索引)";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kObject};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(args.get(0).getTypeIndex());
			}
		});
		info.addExternalFunc("g_type", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取类型";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kObject};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(args.get(0).getTypeString());
			}
		});
		info.addExternalFunc("g_hash", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取哈希";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kObject};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				if (args.get(0).getObj() == null)
					return new RuntimeObject("NULL");
				return new RuntimeObject(String.valueOf(args.get(0).getObj().hashCode()));
			}
		});
		info.addExternalFunc("g_exit", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "程序退出";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				throw new RuntimeException(RuntimeError.EXIT, -1, "用户自行退出");
			}
		});
		info.addExternalFunc("g_load", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "载入并运行程序";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				return new RuntimeObject((long) status.runProcess(args.get(0).getString()));
			}
		});
		info.addExternalFunc("g_load_x", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "载入并运行程序";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				return new RuntimeObject((long) status.runProcessX(args.get(0).getString()));
			}
		});
		info.addExternalFunc("g_load_user", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "载入并运行用户态程序";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				return new RuntimeObject((long) status.runUsrProcess(args.get(0).getString()));
			}
		});
		info.addExternalFunc("g_load_user_x", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "载入并运行用户态程序";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				return new RuntimeObject((long) status.runUsrProcessX(args.get(0).getString()));
			}
		});
		info.addExternalFunc("g_print_file", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "载入并运行程序";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(args.get(0).getString()));
					String line;
					while ((line = br.readLine()) != null) {
						System.out.println(line);
					}
					br.close();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
				return null;
			}
		});
		info.addExternalFunc("g_args_count", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "取得函数参数数量";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject((long) status.getFuncArgsCount());
			}
		});
		info.addExternalFunc("g_args_index", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "取得函数参数";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kInt};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				long index = args.get(0).getLong();
				return status.getFuncArgs((int) index);
			}
		});
		info.addExternalFunc("g_get_timestamp", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取当前时间戳";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(System.currentTimeMillis());
			}
		});
		info.addExternalFunc("g_doc_grammar", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "语法参考";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(Grammar.npaDesc);
			}
		});
		buildIORead(info);

		return runtimeCodePage = page;
	}

	private void buildIORead(IRuntimeDebugInfo info) {
		info.addExternalFunc("g_stdin_read", new ModuleBaseIORead("标准输入读取字符",
				ModuleBaseIORead.ModuleBaseIOReadType.kNextChar));
		info.addExternalFunc("g_stdin_read_int", new ModuleBaseIORead(
				"标准输入读取整数",
				ModuleBaseIORead.ModuleBaseIOReadType.kNextLong));
		info.addExternalFunc("g_stdin_read_real", new ModuleBaseIORead(
				"标准输入读取实数",
				ModuleBaseIORead.ModuleBaseIOReadType.kNextDouble));
		info.addExternalFunc("g_stdin_read_bool",
				new ModuleBaseIORead("标准输入读取布尔值",
						ModuleBaseIORead.ModuleBaseIOReadType.kNextBoolean));
		info.addExternalFunc("g_stdin_read_line", new ModuleBaseIORead(
				"标准输入读取行", ModuleBaseIORead.ModuleBaseIOReadType.kNextLine));
		info.addExternalFunc("g_stdin_read_has_int", new ModuleBaseIORead(
				"标准输入是否匹配整数",
				ModuleBaseIORead.ModuleBaseIOReadType.kHasNextLong));
		info.addExternalFunc("g_stdin_read_has_real", new ModuleBaseIORead(
				"标准输入是否匹配实数",
				ModuleBaseIORead.ModuleBaseIOReadType.kHasNextDouble));
		info.addExternalFunc("g_stdin_read_has_bool", new ModuleBaseIORead(
				"标准输入是否匹配布尔值",
				ModuleBaseIORead.ModuleBaseIOReadType.kHasNextBoolean));
		info.addExternalFunc("g_stdin_read_has_line",
				new ModuleBaseIORead("标准输入是否匹配行",
						ModuleBaseIORead.ModuleBaseIOReadType.kHasNextLine));
	}
}