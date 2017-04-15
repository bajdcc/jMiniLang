package priv.bajdcc.LALR1.interpret.os.kern;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;

/**
 * 【内核】服务
 *
 * @author bajdcc
 */
public class OSTask implements IOSCodePage {
	@Override
	public String getName() {
		return "/kern/task";
	}

	@Override
	public String getCode() {
		return "// KERNEL ENTRY BY BAJDCC\n" +
				"import \"sys.base\";\n" +
				"import \"sys.proc\";\n" +
				"call g_load_x(\"/task/time\");\n" +
				"";
	}
}
