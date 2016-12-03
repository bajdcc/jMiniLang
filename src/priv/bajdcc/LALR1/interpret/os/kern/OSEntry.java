package priv.bajdcc.LALR1.interpret.os.kern;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;

/**
 * 【内核】入口
 *
 * @author bajdcc
 */
public class OSEntry implements IOSCodePage {
	@Override
	public String getName() {
		return "/kern/entry";
	}

	@Override
	public String getCode() {
		return "// KERNEL ENTRY BY BAJDCC\n" +
				"import \"sys.base\";\n" +
				"import \"sys.proc\";\n" +
				"call g_load_sync_x(\"/kern/irq\");\n" +
				"call g_load_x(\"/proc/schd\");\n" +
				"call g_load_user_x(\"/usr/main\");\n" +
				"";
	}
}
