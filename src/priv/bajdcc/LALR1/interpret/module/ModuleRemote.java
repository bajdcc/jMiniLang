package priv.bajdcc.LALR1.interpret.module;

import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.*;
import priv.bajdcc.LALR1.ui.UIRemoteWindow;
import priv.bajdcc.LALR1.ui.drawing.UIRemoteGraphics;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 【模块】远程用户界面
 *
 * @author bajdcc
 */
public class ModuleRemote implements IInterpreterModule {

	private static ModuleRemote instance = new ModuleRemote();
	private UIRemoteWindow remote;
	private UIRemoteGraphics graphics;
	private Queue<Character> queue = new LinkedBlockingDeque<>(1024);
	private Queue<Character> queueDisplay = new ArrayDeque<>();
	private StringBuilder sb = new StringBuilder();

	public static final int UI_NUM = 8;

	public void setGraphics() {
		if (remote == null) {
			remote = new UIRemoteWindow();
			graphics = remote.getUIGraphics();
		}
	}

	public void addInputChar(char c) {
		queue.add(c);
	}

	public void addDisplayChar(char c) {
		queueDisplay.add(c);
	}

	public static ModuleRemote getInstance() {
		return instance;
	}

	@Override
	public String getModuleName() {
		return "sys.remote";
	}

	@Override
	public RuntimeCodePage getCodePage() throws Exception {
		String base = "import \"sys.base\";\n" +
				"import \"sys.list\";\n" +
				"import \"sys.proc\";\n" +
				"import \"sys.string\";\n" +
				"var g_remote_print = func ~(str) {\n" +
				"    var remote_int = call g_create_pipe(\"int#0\");\n" +
				"    foreach (var c : call g_range_string(str)) {\n" +
				"        call g_write_pipe(remote_int, c);\n" +
				"    }\n" +
				"};\n" +
				"export \"g_remote_print\";\n" +
				"var g_remote_printn = func ~(str) {\n" +
				"    call g_remote_print(str);\n" +
				"    call g_remote_println();\n" +
				"};\n" +
				"export \"g_remote_printn\";\n" +
				"var g_remote_println = func ~() {\n" +
				"    call g_remote_print(g_endl);\n" +
				"};\n" +
				"export \"g_remote_println\";\n" +
				"call g_remote_init();\n" +
				"\n";

		Grammar grammar = new Grammar(base);
		RuntimeCodePage page = grammar.getCodePage();
		IRuntimeDebugInfo info = page.getInfo();
		buildRemoteMethods(info);

		return page;
	}

	private void buildRemoteMethods(IRuntimeDebugInfo info) {
		info.addExternalFunc("g_remote_init", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "显示初始化";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				if (graphics == null)
					setGraphics();
				return null;
			}
		});
		info.addExternalFunc("g_remote_print_internal", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "显示输出";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kChar};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				graphics.drawText((char) args.get(0).getObj());
				return null;
			}
		});
	}
}
