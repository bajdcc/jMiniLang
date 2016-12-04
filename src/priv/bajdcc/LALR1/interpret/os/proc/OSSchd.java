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
				"var state = [];\n" +
				"call g_array_add(state, true);\n" +
				"call g_start_share(\"SCHD#ON\", state);\n" +
				"for (;;) {\n" +
				"    foreach (var i : call g_range_array(call g_get_user_procs())) {\n" +
				"        call g_run_user(i);\n" +
				"        call g_sleep(1);\n" +
				"    }\n" +
				"    var _state_ = call g_query_share(\"SCHD#ON\");\n" +
				"    var on = call g_array_get(_state_, 0);\n" +
				"    if (!on) { break; }\n" +
				"}\n" +
				"for (;;) {\n" +
				"    var procs = call g_get_user_procs();\n" +
				"    if (call g_array_size(procs) == 0) { break; }\n" +
				"    foreach (var i : call g_range_array(procs)) {\n" +
				"        call g_run_user(i);\n" +
				"        call g_sleep(1);\n" +
				"    }\n" +
				"}\n" +
				"//call g_printn(\"schd exit\");\n" +
				"";
	}
}
