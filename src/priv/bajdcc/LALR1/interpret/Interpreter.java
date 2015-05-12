package priv.bajdcc.LALR1.interpret;

import java.util.ArrayList;
import java.util.List;

import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeDebugExec;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeDebugInfo;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeDebugValue;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeMachine;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeObjectType;

/**
 * 【运行时】扩展/内建虚拟机
 *
 * @author bajdcc
 */
public class Interpreter extends RuntimeMachine {

	public Interpreter() throws Exception {
		builtin();
	}

	private void builtin() throws Exception {
		buildBase();
	}

	private void buildBase() throws Exception {
		String base = "var aythor = \"bajdcc\";";

		Grammar grammar = new Grammar(base);
		RuntimeCodePage page = grammar.getCodePage();
		IRuntimeDebugInfo info = page.getInfo();
		info.addExternalValue("g_null", new IRuntimeDebugValue() {
			@Override
			public RuntimeObject getRuntimeObject() {
				return new RuntimeObject(null);
			}
		});
		info.addExternalFunc("g_is_null", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "判断是否为空";
			}

			@Override
			public List<RuntimeObjectType> getArgsType() {
				final ArrayList<RuntimeObjectType> types = new ArrayList<RuntimeObjectType>();
				types.add(RuntimeObjectType.kObject);
				return types;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args) {
				return new RuntimeObject(args.get(0).getObj() == null);
			}
		});
		info.addExternalFunc("g_print", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "输出";
			}

			@Override
			public List<RuntimeObjectType> getArgsType() {
				final ArrayList<RuntimeObjectType> types = new ArrayList<RuntimeObjectType>();
				types.add(RuntimeObjectType.kObject);
				return types;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args) {
				System.out.println(args.get(0).getObj());
				return null;
			}
		});

		run("base", page);
	}
}
