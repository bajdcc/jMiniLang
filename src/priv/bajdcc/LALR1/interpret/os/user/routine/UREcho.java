package priv.bajdcc.LALR1.interpret.os.user.routine;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;

/**
 * 【用户态】打印输出
 *
 * @author bajdcc
 */
public class UREcho implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/echo";
	}

	@Override
	public String getCode() {
		return "import \"sys.base\";\n" +
				"import \"sys.list\";\n" +
				"import \"sys.string\";\n" +
				"import \"sys.proc\";\n" +
				"import \"sys.ui\";\n" +
				"\n" +
				"call g_set_process_desc(\"echo routinue\");\n" +
				"var pid = call g_get_pid();\n" +
				"var share = call g_wait_share(\"PID#\" + pid);\n" +
				"call g_stop_share(\"PID#\" + pid);\n" +
				"var args = call g_map_get(share, \"args\");\n" +
				"\n" +
				"var in = call g_create_pipe(\"PIPEIN#\" + pid);\n" +
				"var out = call g_create_pipe(\"PIPEOUT#\" + pid);\n" +
				"\n" +
				"if (call g_array_empty(args)) {\n" +
				"    var pipe = func [\"PIPE\"] ~(ch, out) {\n" +
				"        call g_ui_print(ch);\n" +
				"        call g_write_pipe(out, ch);\n" +
				"    };\n" +
				"    call g_read_pipe_args(in, pipe, out);\n" +
				"} else {\n" +
				"    foreach (var i : call g_range_array(args)) {\n" +
				"        foreach (var j : call g_range_string(i)) {\n" +
				"            call g_write_pipe(out, j);\n" +
				"        }\n" +
				"        call g_write_pipe(out, '\\n');\n" +
				"    }\n" +
				"}\n" +
				"\n" +
				"call g_destroy_pipe(out);\n" +
				"call g_destroy_pipe(in);\n";
	}
}
