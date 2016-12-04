package priv.bajdcc.LALR1.interpret.os.user;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;

/**
 * 【用户态】主进程
 *
 * @author bajdcc
 */
public class UserMain implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/main";
	}

	@Override
	public String getCode() {
		return "// USER MAIN BY BAJDCC\n" +
				"import \"sys.base\";\n" +
				"import \"sys.proc\";\n" +
				"call g_load_user_x(\"/usr/p/sh\");\n";
	}
}
