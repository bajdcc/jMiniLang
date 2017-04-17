package priv.bajdcc.LALR1.interpret.os.user.routine;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;

/**
 * 【用户态】时间
 *
 * @author bajdcc
 */
public class URTime implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/time";
	}

	@Override
	public String getCode() {
		return "import \"sys.base\";\n" +
				"import \"sys.list\";\n" +
				"import \"sys.proc\";\n" +
				"import \"sys.task\";\n" +
				"import \"sys.string\";\n" +
				"\n" +
				"call g_set_process_desc(\"time routinue\");\n" +
				"var pid = call g_get_pid();\n" +
				"var share = call g_wait_share(\"PID#\" + pid);\n" +
				"call g_stop_share(\"PID#\" + pid);\n" +
				"var args = call g_map_get(share, \"args\");\n" +
				"\n" +
				"var in = call g_create_pipe(\"PIPEIN#\" + pid);\n" +
				"var out = call g_create_pipe(\"PIPEOUT#\" + pid);\n" +
				"\n" +
				"var signal = \"PIDSIG#\" + pid;\n" +
				"call g_start_share(signal, true);\n" +
				"\n" +
				"var get_time = func ~() -> call g_task_get_fast(\"system\", \"now\");\n" +
				"for (; call g_query_share(signal);) {\n" +
				"    call g_write_pipe(out, \"\" + call get_time() + \"\\r\");\n" +
				"    call g_task_sleep(1);\n" +
				"}\n" +
				"call g_write_pipe(out, \"\\n\");\n" +
				"\n" +
				"call g_stop_share(signal);\n" +
				"\n" +
				"call g_destroy_pipe(out);\n" +
				"call g_destroy_pipe(in);\n";
	}
}
