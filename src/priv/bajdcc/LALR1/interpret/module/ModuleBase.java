package priv.bajdcc.LALR1.interpret.module;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.List;

import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeDebugExec;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeDebugInfo;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeStatus;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeException;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeException.RuntimeError;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeObjectType;

/**
 * 【模块】基本模块
 *
 * @author bajdcc
 */
public class ModuleBase implements IInterpreterModule {

	private static ModuleBase instance = new ModuleBase();

	public static ModuleBase getInstance() {
		return instance;
	}

	@Override
	public String getModuleName() {
		return "sys.base";
	}

	@Override
	public RuntimeCodePage getCodePage() throws Exception {
		String base = "var g_author = func [\"标准库的作者：bajdcc\"] ~() -> \"bajdcc\";\n"
				+ "export \"g_author\";\n"
				+ "var g_max = func ~(a, b) -> a > b ? a : b;\n"
				+ "export \"g_max\";\n"
				+ "var g_min = func ~(a, b) -> a < b ? a : b;\n"
				+ "export \"g_min\";\n"
				+ "var g_lt = func ~(a, b) -> a < b;\n"
				+ "export \"g_lt\";\n"
				+ "var g_gt = func ~(a, b) -> a > b;\n"
				+ "export \"g_gt\";\n"
				+ "var g_eq = func ~(a, b) -> a == b;\n"
				+ "export \"g_eq\";\n"
				+ "var g_mod = func ~(a, b) -> a % b == 0;\n"
				+ "export \"g_mod\";\n"
				+ "var g_curry_1 = func ~(a, b) {\n"
				+ "    var curry = func ~(c) -> call a(b, c);\n"
				+ "    return curry;"
				+ "};\n"
				+ "export \"g_curry_1\";\n"
				+ "var g_swap = func ~(a) {\n"
				+ "    var swap = func ~(b, c) -> call a(c, b);\n"
				+ "    return swap;"
				+ "};\n"
				+ "export \"g_swap\";\n"
				+ "var g_println = func ~() {\n"
				+ "    call g_print(g_endl);\n"
				+ "};\n"
				+ "export \"g_println\";\n"
				+ "var g_range = yield ~(a, b) {\n"
				+ "    for (var i = a; i <= b; i++) {\n"
				+ "        yield i;\n"
				+ "    }\n"
				+ "};\n"
				+ "export \"g_range\";\n"
				+ "var g_range_string = yield ~(str) {\n"
				+ "    var length = call g_string_length(str);"
				+ "    for (var i = 0; i < length; i++) {\n"
				+ "        yield call g_string_char(str, i);\n"
				+ "    }\n"
				+ "};\n"
				+ "export \"g_range_string\";\n"
				+ "var g_range_foreach = func ~(a, b, c) {\n"
				+ "    foreach (var i : call g_range(a, b)) {\n"
				+ "        call c(i);\n"
				+ "    }\n"
				+ "};\n"
				+ "export \"g_range_foreach\";\n"
				+ "var g_range_any = func ~(a, b, c) {\n"
				+ "    foreach (var i : call g_range(a, b)) {\n"
				+ "        if (call c(i)) {\n"
				+ "            return true;"
				+ "        }\n"
				+ "    }\n"
				+ "    return false;\n"
				+ "};\n"
				+ "export \"g_range_any\";\n" + "\n";

		Grammar grammar = new Grammar(base);
		RuntimeCodePage page = grammar.getCodePage();
		IRuntimeDebugInfo info = page.getInfo();
		info.addExternalValue("g_null", () -> new RuntimeObject(null));
		info.addExternalValue("g_endl", () -> new RuntimeObject("\n"));
		info.addExternalFunc("g_is_null", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "判断是否为空";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kObject };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
					IRuntimeStatus status) throws Exception {
				return new RuntimeObject(args.get(0).getObj() == null);
			}
		});
		info.addExternalFunc("g_print", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "标准输出";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kObject };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
					IRuntimeStatus status) throws Exception {
				System.out.print(args.get(0).getObj());
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
				return new RuntimeObjectType[] { RuntimeObjectType.kObject };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
					IRuntimeStatus status) throws Exception {
				System.out.println(args.get(0).getObj());
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
				return new RuntimeObjectType[] { RuntimeObjectType.kObject };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
					IRuntimeStatus status) throws Exception {
				System.err.print(args.get(0).getObj());
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
				return new RuntimeObjectType[] { RuntimeObjectType.kObject };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
					IRuntimeStatus status) {
				RuntimeObject obj = args.get(0);
				if (obj == null) {
					return new RuntimeObject(null);
				}
				return new RuntimeObject(args.get(0).getObj().toString());
			}
		});
		info.addExternalFunc("g_new", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "拷贝";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kObject };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
					IRuntimeStatus status) throws Exception {
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
				return new RuntimeObjectType[] { RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
					IRuntimeStatus status) throws Exception {
				return new RuntimeObject(status.getHelpString(args.get(0)
						.getObj().toString()));
			}
		});
		info.addExternalFunc("g_get_type", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取类型";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kObject };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
					IRuntimeStatus status) throws Exception {
				return new RuntimeObject(args.get(0).getTypeName());
			}
		});
		info.addExternalFunc("g_type", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取类型";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kObject };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) throws Exception {
				return new RuntimeObject(args.get(0).getTypeString());
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
				return new RuntimeObjectType[] { RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
					IRuntimeStatus status) throws Exception {
				return new RuntimeObject(BigInteger.valueOf(status.runProcess(args.get(0).getObj().toString())));
			}
		});
		info.addExternalFunc("g_load_x", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "载入并运行程序";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				return new RuntimeObject(BigInteger.valueOf(status.runProcessX(args.get(0).getObj().toString())));
			}
		});
		info.addExternalFunc("g_load_user", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "载入并运行用户态程序";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				return new RuntimeObject(BigInteger.valueOf(status.runUsrProcess(args.get(0).getObj().toString())));
			}
		});
		info.addExternalFunc("g_load_user_x", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "载入并运行用户态程序";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				return new RuntimeObject(BigInteger.valueOf(status.runUsrProcessX(args.get(0).getObj().toString())));
			}
		});
		info.addExternalFunc("g_print_file", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "载入并运行程序";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
					IRuntimeStatus status) throws Exception {
				try {
					BufferedReader br = new BufferedReader(new FileReader(args
							.get(0).getObj().toString()));
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
		buildIORead(info);
		buildStringMethod(info);

		return page;
	}

	private void buildIORead(IRuntimeDebugInfo info) {
		info.addExternalFunc("g_stdin_read", new ModuleBaseIORead("标准输入读取字符",
				ModuleBaseIORead.ModuleBaseIOReadType.kNextChar));
		info.addExternalFunc("g_stdin_read_int", new ModuleBaseIORead(
				"标准输入读取整数",
				ModuleBaseIORead.ModuleBaseIOReadType.kNextBigInteger));
		info.addExternalFunc("g_stdin_read_real", new ModuleBaseIORead(
				"标准输入读取实数",
				ModuleBaseIORead.ModuleBaseIOReadType.kNextBigDecimal));
		info.addExternalFunc("g_stdin_read_bool",
				new ModuleBaseIORead("标准输入读取布尔值",
						ModuleBaseIORead.ModuleBaseIOReadType.kNextBoolean));
		info.addExternalFunc("g_stdin_read_line", new ModuleBaseIORead(
				"标准输入读取行", ModuleBaseIORead.ModuleBaseIOReadType.kNextLine));
		info.addExternalFunc("g_stdin_read_has_int", new ModuleBaseIORead(
				"标准输入是否匹配整数",
				ModuleBaseIORead.ModuleBaseIOReadType.kHasNextBigInteger));
		info.addExternalFunc("g_stdin_read_has_real", new ModuleBaseIORead(
				"标准输入是否匹配实数",
				ModuleBaseIORead.ModuleBaseIOReadType.kHasNextBigDecimal));
		info.addExternalFunc("g_stdin_read_has_bool", new ModuleBaseIORead(
				"标准输入是否匹配布尔值",
				ModuleBaseIORead.ModuleBaseIOReadType.kHasNextBoolean));
		info.addExternalFunc("g_stdin_read_has_line",
				new ModuleBaseIORead("标准输入是否匹配行",
						ModuleBaseIORead.ModuleBaseIOReadType.kHasNextLine));
	}

	private void buildStringMethod(IRuntimeDebugInfo info) {
		info.addExternalFunc("g_string_length", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字符串长度";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				return new RuntimeObject(BigInteger.valueOf(args.get(0).getObj().toString().length()));
			}
		});
		info.addExternalFunc("g_string_char", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字符串遍历";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString, RuntimeObjectType.kInt };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				BigInteger index = (BigInteger) args.get(1).getObj();
				return new RuntimeObject(args.get(0).getObj().toString().charAt(index.intValue()));
			}
		});
		info.addExternalFunc("g_string_empty", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "字符串是否为空";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				return new RuntimeObject(args.get(0).getObj().toString().isEmpty());
			}
		});
	}
}