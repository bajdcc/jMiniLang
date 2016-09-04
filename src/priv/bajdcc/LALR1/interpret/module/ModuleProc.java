package priv.bajdcc.LALR1.interpret.module;

import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.*;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeException;
import priv.bajdcc.LALR1.grammar.runtime.data.RuntimeArray;
import priv.bajdcc.LALR1.grammar.runtime.data.RuntimeFuncObject;
import priv.bajdcc.LALR1.grammar.runtime.data.RuntimeMap;

import java.math.BigInteger;
import java.util.List;

/**
 * 【模块】进程模块
 *
 * @author bajdcc
 */
public class ModuleProc implements IInterpreterModule {

	private static final int JOIN_TIME = 20;
	private static final int LOCK_TIME = 20;
	private static final int PIPE_READ_TIME = 10;

	@Override
	public String getModuleName() {
		return "sys.proc";
	}

	@Override
	public RuntimeCodePage getCodePage() throws Exception {
		String base = "import \"sys.base\"; import \"sys.list\";\n" +
				"var g_join_process = func ~(pid) {\n" +
				"    while (call g_join_process_once(pid) != 0) {}\n" +
				"};\n" +
				"export \"g_join_process\";\n" +
				"var g_join_process_array = func ~(pid) {\n" +
				"    var len = call g_array_size(pid) - 1;\n" +
				"    foreach (var i : call g_range(0, len)) {\n" +
				"        call g_join_process(call g_array_get(pid, i));\n" +
				"    }\n" +
				"};\n" +
				"export \"g_join_process_array\";\n" +
				"var g_join_process_time = func ~(pid, time) {\n" +
				"    for (var i = 0; i < time; i++) {\n" +
				"        call g_join_process_once(pid);\n" +
				"    }\n" +
				"};\n" +
				"export \"g_join_process_time\";\n" +
				"var g_lock_share = func ~(name) {\n" +
				"    while (call g_try_lock_share(name)) {}" +
				"};\n" +
				"export \"g_lock_share\";\n" +
				"var g_read_pipe = func ~(handle, callback) {\n" +
				"    var data = '\\0';" +
				"    for (;;) {\n" +
				"        let data = call g_read_pipe_char(handle);\n" +
				"        if (data == '\\uffff') {\n" +
				"            break;\n" +
				"        }\n" +
				"        if (data != '\\ufffe') {\n" +
				"            call callback(data);\n" +
				"        }\n" +
				"    }\n" +
				"};\n" +
				"export \"g_read_pipe\";\n" +
				"var g_write_pipe = func ~(handle, data) {\n" +
				"    foreach (var ch : call g_range_string(data)) {\n" +
				"        call g_write_pipe_char(handle, ch);\n" +
				"    }\n" +
				"};\n" +
				"export \"g_write_pipe\";\n" +
				"var g_load_sync = func ~(fn) -> call g_join_process(call g_load(fn));\n" +
				"export \"g_load_sync\";";

		Grammar grammar = new Grammar(base);
		RuntimeCodePage page = grammar.getCodePage();
		IRuntimeDebugInfo info = page.getInfo();
		buildMethod(info);
		buildPipeMethod(info);
		buildShareMethod(info);

		return page;
	}

	private void buildMethod(IRuntimeDebugInfo info) {
		info.addExternalFunc("g_create_process", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "创建进程";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kFunc };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				RuntimeFuncObject func = (RuntimeFuncObject) args.get(0).getObj();
				return new RuntimeObject(BigInteger.valueOf(status.createProcess(func)));
			}
		});
		info.addExternalFunc("g_create_process_args", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "创建进程带参数";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kFunc, RuntimeObjectType.kObject };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				RuntimeFuncObject func = (RuntimeFuncObject) args.get(0).getObj();
				RuntimeObject obj = args.get(1);
				return new RuntimeObject(BigInteger.valueOf(status.createProcess(func, obj)));
			}
		});
		info.addExternalFunc("g_get_pid", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取进程ID";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				return new RuntimeObject(BigInteger.valueOf(status.getPid()));
			}
		});
		info.addExternalFunc("g_get_process_priority", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取进程优先级";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				return new RuntimeObject(BigInteger.valueOf(status.getPriority()));
			}
		});
		info.addExternalFunc("g_join_process_once", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "进程等待";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kInt };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				BigInteger pid = (BigInteger) args.get(0).getObj();
				return new RuntimeObject(BigInteger.valueOf(
						status.getService().getProcessService().join(pid.intValue(), status.getPid(), JOIN_TIME)));
			}
		});
		info.addExternalFunc("g_sleep", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "进程睡眠";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kInt };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				BigInteger turn = (BigInteger) args.get(0).getObj();
				int time = turn.intValue();
				return new RuntimeObject(BigInteger.valueOf(
						status.getService().getProcessService().sleep(status.getPid(), time > 0 ? time : 0)));
			}
		});
	}

	private void buildPipeMethod(IRuntimeDebugInfo info) {
		info.addExternalFunc("g_create_pipe", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "创建管道";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) throws Exception {
				String name = args.get(0).getObj().toString();
				int handle = status.getService().getPipeService().create(name);
				if (handle == -1)
					status.err(RuntimeException.RuntimeError.MAX_HANDLE);
				return new RuntimeObject(handle);
			}
		});
		info.addExternalFunc("g_destroy_pipe", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "销毁管道";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kPtr };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) throws Exception {
				int handle = (int) args.get(0).getObj();
				return new RuntimeObject(status.getService().getPipeService().destroy(handle));
			}
		});
		info.addExternalFunc("g_read_pipe_char", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "管道读";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kPtr };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) throws Exception {
				int handle = (int) args.get(0).getObj();
				char ch = status.getService().getPipeService().read(handle);
				if (ch == '\ufffe')
					status.getService().getProcessService().sleep(status.getPid(), PIPE_READ_TIME);
				return new RuntimeObject(ch);
			}
		});
		info.addExternalFunc("g_write_pipe_char", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "管道写";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kPtr, RuntimeObjectType.kChar };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
												  IRuntimeStatus status) throws Exception {
				int handle = (int) args.get(0).getObj();
				char ch = (char) args.get(1).getObj();
				return new RuntimeObject(status.getService().getPipeService().write(handle, ch));
			}
		});
	}

	private void buildShareMethod(IRuntimeDebugInfo info) {
		info.addExternalFunc("g_start_share", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "创建共享";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString, RuntimeObjectType.kObject };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				String name = args.get(0).getObj().toString();
				int result = status.getService().getShareService().startSharing(name, args.get(1));
				if (result == -1)
					status.err(RuntimeException.RuntimeError.MAX_HANDLE);
				if (result == 0)
					status.err(RuntimeException.RuntimeError.DUP_SHARE_NAME);
				return new RuntimeObject(BigInteger.valueOf(result));
			}
		});
		info.addExternalFunc("g_query_share", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "查询共享";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				String name = args.get(0).getObj().toString();
				return status.getService().getShareService().getSharing(name, false);
			}
		});
		info.addExternalFunc("g_reference_share", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "引用共享";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				String name = args.get(0).getObj().toString();
				return status.getService().getShareService().getSharing(name, true);
			}
		});
		info.addExternalFunc("g_stop_share", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "停止共享";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				String name = args.get(0).getObj().toString();
				int result = status.getService().getShareService().stopSharing(name);
				if (result == -1)
					status.err(RuntimeException.RuntimeError.INVALID_SHARE_NAME);
				if (result == 2)
					status.err(RuntimeException.RuntimeError.INVALID_REFERENCE);
				return new RuntimeObject(result == 1);
			}
		});
		info.addExternalFunc("g_try_lock_share", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "尝试锁定共享变量";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				String name = args.get(0).getObj().toString();
				if (status.getService().getShareService().isLocked(name)) {
					status.getService().getProcessService().sleep(status.getPid(), LOCK_TIME);
					return new RuntimeObject(true);
				}
				status.getService().getShareService().setLocked(name, true);
				return new RuntimeObject(false);
			}
		});
		info.addExternalFunc("g_unlock_share", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "解锁共享变量";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				String name = args.get(0).getObj().toString();
				if (status.getService().getShareService().isLocked(name)) {
					status.getService().getShareService().setLocked(name, false);
					return new RuntimeObject(true);
				}
				return new RuntimeObject(false);
			}
		});
	}
}
