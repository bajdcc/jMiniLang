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
				"for (var i = 1;; i++) {\n" +
				"    call g_printn(\"TIME: \" + i);\n" +
				"    call g_sleep(1000);" +
				"}\n";
	}
}
