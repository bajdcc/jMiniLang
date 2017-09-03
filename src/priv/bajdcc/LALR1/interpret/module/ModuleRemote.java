package priv.bajdcc.LALR1.interpret.module;

import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.*;
import priv.bajdcc.LALR1.ui.UIRemoteWindow;
import priv.bajdcc.LALR1.ui.drawing.UIRemoteGraphics;
import priv.bajdcc.util.ResourceLoader;

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
	private RuntimeCodePage runtimeCodePage;

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
		if (runtimeCodePage != null)
			return runtimeCodePage;

		String base = ResourceLoader.load(getClass());

		Grammar grammar = new Grammar(base);
		RuntimeCodePage page = grammar.getCodePage();
		IRuntimeDebugInfo info = page.getInfo();
		buildRemoteMethods(info);

		return runtimeCodePage = page;
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
