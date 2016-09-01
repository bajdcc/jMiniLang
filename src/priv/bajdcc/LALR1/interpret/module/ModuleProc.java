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

	@Override
	public String getModuleName() {
		return "sys.proc";
	}

	@Override
	public RuntimeCodePage getCodePage() throws Exception {
		String base = ";";

		Grammar grammar = new Grammar(base);
		RuntimeCodePage page = grammar.getCodePage();
		IRuntimeDebugInfo info = page.getInfo();
		buildMethod(info);

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
				status.createProcess(func);
				return null;
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
	}
}
