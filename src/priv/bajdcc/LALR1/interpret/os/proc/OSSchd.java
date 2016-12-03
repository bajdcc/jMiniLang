package priv.bajdcc.LALR1.interpret.os.proc;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;

/**
 * 【进程】多进程调度
 *
 * @author bajdcc
 */
public class OSSchd implements IOSCodePage {
	@Override
	public String getName() {
		return "/proc/schd";
	}

	@Override
	public String getCode() {
		return "import \"sys.base\";\n" +
				"import \"sys.list\";\n" +
				"import \"sys.proc\";\n" +
				"call g_set_process_priority(64);\n" +
				"for (;;) {\n" +
				"    foreach (var i : call g_range_array(call g_get_user_procs())) {\n" +
				"        call g_run_user(i);\n" +
				"        call g_sleep(1);\n" +
				"    }\n" +
				"}\n" +
				"";
	}
}
