package com.bajdcc.LALR1.interpret.module;

import com.bajdcc.LALR1.grammar.Grammar;
import com.bajdcc.LALR1.grammar.runtime.*;
import com.bajdcc.LALR1.ui.UIMainFrame;
import com.bajdcc.LALR1.ui.UIRemoteWindow;
import com.bajdcc.LALR1.ui.drawing.UIRemoteGraphics;
import com.bajdcc.util.ResourceLoader;

import java.util.List;

/**
 * 【模块】远程用户界面
 *
 * @author bajdcc
 */
public class ModuleRemote implements IInterpreterModule {

	private static ModuleRemote instance = new ModuleRemote();
	private static boolean enable = false;
	private static UIMainFrame mainFrame;
	private UIRemoteWindow remote;
	private UIRemoteGraphics graphics;
	private RuntimeCodePage runtimeCodePage;

	public static void enabled() {
		enable = true;
	}

	public static void setMainFrame(UIMainFrame frame) {
		mainFrame = frame;
	}

	public static void showMainFrame() {
		mainFrame.setVisible(true);
		mainFrame.setFocus();
	}

	public void setGraphics() {
		if (remote == null) {
			remote = new UIRemoteWindow();
			graphics = remote.getUIGraphics();
		}
	}

	public static ModuleRemote getInstance() {
		return instance;
	}

	@Override
	public String getModuleName() {
		return "sys.remote";
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
			                                      IRuntimeStatus status) {
				if (enable && graphics == null)
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
			                                      IRuntimeStatus status) {
				graphics.drawText((char) args.get(0).getObj());
				return null;
			}
		});
	}
}
