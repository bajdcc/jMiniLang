package priv.bajdcc.LALR1.interpret.module;

import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.*;
import priv.bajdcc.LALR1.ui.UIPanel;
import priv.bajdcc.LALR1.ui.drawing.UIGraphics;
import priv.bajdcc.util.ResourceLoader;

import javax.swing.*;
import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 【模块】界面
 *
 * @author bajdcc
 */
public class ModuleUI implements IInterpreterModule {

	private static final int INPUT_TIME = 10;
	private static ModuleUI instance = new ModuleUI();
	private UIGraphics graphics;
	private JPanel panel;
	private Queue<Character> queue = new LinkedBlockingDeque<>(10000);
	private Queue<Character> queueDisplay = new ArrayDeque<>();
	private StringBuilder sb = new StringBuilder();
	private RuntimeCodePage runtimeCodePage;
	private static final int PRINT_BLOCK_TIME = 5;

	public void setGraphics(UIGraphics graphics) {
		this.graphics = graphics;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public void addInputChar(char c) {
		queue.add(c);
	}

	public void addDisplayChar(char c) {
		queueDisplay.add(c);
	}

	public static ModuleUI getInstance() {
		return instance;
	}

	@Override
	public String getModuleName() {
		return "sys.ui";
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
		buildUIMethods(info);

		return runtimeCodePage = page;
	}

	private void buildUIMethods(IRuntimeDebugInfo info) {
		info.addExternalFunc("g_ui_print_internal_block", new IRuntimeDebugExec() {
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
				boolean failure = graphics.drawText((char) args.get(0).getObj());
				if (failure) {
					status.getService().getProcessService().waitForUI();
					status.getService().getProcessService().sleep(status.getPid(), PRINT_BLOCK_TIME);
				}
				return new RuntimeObject(failure);
			}
		});
		info.addExternalFunc("g_ui_input_internal", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "显示输入";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				status.getService().getProcessService().sleep(status.getPid(), INPUT_TIME);
				Character c = queue.poll();
				if (c == null) {
					return null;
				}
				if (c.equals('\n')) {
					String str = sb.toString();
					sb = new StringBuilder();
					queueDisplay.clear();
					return new RuntimeObject(str);
				} else if (c.equals('\b')) {
					if (sb.length() > 0)
						sb.deleteCharAt(sb.length() - 1);
					graphics.drawText('\b');
					return null;
				} else {
					if (c < '\ufff0') {
						sb.append(c);
					}
					queueDisplay.add(c);
				}
				return null;
			}
		});
		info.addExternalFunc("g_ui_input_im", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "立即显示输入";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				status.getService().getProcessService().sleep(status.getPid(), INPUT_TIME);
				String str = sb.toString();
				sb = new StringBuilder();
				queueDisplay.clear();
				return new RuntimeObject(str);
			}
		});
		info.addExternalFunc("g_ui_input_queue", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "显示输入缓冲";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				status.getService().getProcessService().sleep(status.getPid(), INPUT_TIME);
				String str = String.valueOf(args.get(0).getObj());
				sb.append(str);
				for (char c : str.toCharArray()) {
					queueDisplay.add(c);
				}
				return null;
			}
		});
		info.addExternalFunc("g_ui_print_input", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "实时显示输入";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				status.getService().getProcessService().sleep(status.getPid(), INPUT_TIME);
				return new RuntimeObject(queueDisplay.poll());
			}
		});
		info.addExternalFunc("g_ui_caret", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "设置光标闪烁";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kBool};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				boolean caret = (boolean) args.get(0).getObj();
				if (caret) {
					graphics.setCaret(true);
					return null;
				} else {
					graphics.setCaret(false);
					status.getService().getProcessService().sleep(status.getPid(), INPUT_TIME);
					return new RuntimeObject(graphics.isHideCaret());
				}
			}
		});
		info.addExternalFunc("g_ui_fallback", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "撤销上次输入";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				sb = new StringBuilder();
				graphics.fallback();
				return null;
			}
		});
		info.addExternalFunc("g_ui_cols", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "多少列";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(BigInteger.valueOf(graphics.getCols()));
			}
		});
		info.addExternalFunc("g_ui_rows", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "多少行";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return null;
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				return new RuntimeObject(BigInteger.valueOf(graphics.getRows()));
			}
		});
		info.addExternalFunc("g_ui_text_length", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "一段文字宽度";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				String str = String.valueOf(args.get(0).getObj());
				return new RuntimeObject(BigInteger.valueOf(graphics.calcWidth(str)));
			}
		});
		info.addExternalFunc("g_ui_create_dialog_internal", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "创建对话框";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kString, RuntimeObjectType.kString, RuntimeObjectType.kInt};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				String caption = String.valueOf(args.get(0).getObj());
				String text = String.valueOf(args.get(1).getObj());
				int mode = ((BigInteger)args.get(2).getObj()).intValue();
				return new RuntimeObject(status.getService().getDialogService().create(caption, text, mode, panel));
			}
		});
		info.addExternalFunc("g_ui_show_dialog_internal", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "弹出对话框";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[]{RuntimeObjectType.kPtr};
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) {
				int handle = (int) args.get(0).getObj();
				return new RuntimeObject(status.getService().getDialogService().show(handle));
			}
		});
	}
}
