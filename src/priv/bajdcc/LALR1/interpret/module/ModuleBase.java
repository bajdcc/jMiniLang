package priv.bajdcc.LALR1.interpret.module;

import java.util.List;
import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeDebugExec;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeDebugInfo;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeDebugValue;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeStatus;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeObjectType;

/**
 * 【模块】基本模块
 *
 * @author bajdcc
 */
public class ModuleBase implements IInterpreterModule {

	@Override
	public String getModuleName() {
		return "sys.base";
	}

	@Override
	public RuntimeCodePage getCodePage() throws Exception {
		String base = "var g_author = func ~() -> \"bajdcc\";\n"
				+ "var g_max = func ~(a, b) -> a > b ? a : b;\n"
				+ "var g_min = func ~(a, b) -> a < b ? a : b;\n"
				+ "export \"g_author\";\n" + "export \"g_max\";\n"
				+ "export \"g_min\";\n";

		Grammar grammar = new Grammar(base);
		RuntimeCodePage page = grammar.getCodePage();
		IRuntimeDebugInfo info = page.getInfo();
		info.addExternalValue("g_null", new IRuntimeDebugValue() {
			@Override
			public RuntimeObject getRuntimeObject() {
				return new RuntimeObject(null);
			}
		});
		info.addExternalValue("g_endl", new IRuntimeDebugValue() {
			@Override
			public RuntimeObject getRuntimeObject() {
				return new RuntimeObject("\n");
			}
		});
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
		info.addExternalFunc("g_load_func", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取函数地址";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
					IRuntimeStatus status) throws Exception {
				return new RuntimeObject(status.getFuncAddr(args.get(0)
						.getObj().toString()), RuntimeObjectType.kFunc);
			}
		});
		buildIORead(info);

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
}