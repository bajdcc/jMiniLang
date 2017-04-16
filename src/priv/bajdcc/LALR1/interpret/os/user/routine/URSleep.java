package priv.bajdcc.LALR1.interpret.os.user.routine;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;

/**
 * 【用户态】休眠
 *
 * @author bajdcc
 */
public class URSleep implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/sleep";
	}

	@Override
	public String getCode() {
		return "import \"sys.base\";\n" +
				"import \"sys.list\";\n" +
				"import \"sys.proc\";\n" +
				"import \"sys.task\";\n" +
				"import \"sys.string\";\n" +
				"\n" +
				"call g_set_process_desc(\"sleep routinue\");\n" +
				"var pid = call g_get_pid();\n" +
				"var share = call g_wait_share(\"PID#\" + pid);\n" +
				"call g_stop_share(\"PID#\" + pid);\n" +
				"var args = call g_map_get(share, \"args\");\n" +
				"\n" +
				"var in = call g_create_pipe(\"PIPEIN#\" + pid);\n" +
				"var out = call g_create_pipe(\"PIPEOUT#\" + pid);\n" +
				"\n" +
				"var second = call g_array_get(args, 0);\n" +
				"if (call g_is_null(second)) {\n" +
				"    let second = \"0\";\n" +
				"}\n" +
				"let second = call g_string_atoi(second);\n" +
				"\n" +
				"//var get_tick = func ~() -> call g_task_get_fast_arg(\"system\", \"now\", \"timestamp\");\n" +
				"var begin = call g_task_get_timestamp();\n" +
				"var end = begin + second * 1000;\n" +
				"while (begin < end) {\n" +
				"    let begin = call g_task_get_timestamp();\n" +
				"    call g_sleep(50);\n" +
				"}\n" +
				"\n" +
				"var pipe = func [\"PIPE\"] ~(ch, out) {\n" +
				"    call g_write_pipe(out, ch);\n" +
				"};\n" +
				"call g_read_pipe_args(in, pipe, out);\n" +
				"call g_destroy_pipe(out);\n";
	}
}
