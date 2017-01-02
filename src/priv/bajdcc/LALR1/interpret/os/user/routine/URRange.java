package priv.bajdcc.LALR1.interpret.os.user.routine;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;

/**
 * 【用户态】生成序列
 *
 * @author bajdcc
 */
public class URRange implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/range";
	}

	@Override
	public String getCode() {
		return "import \"sys.base\";\n" +
				"import \"sys.list\";\n" +
				"import \"sys.string\";\n" +
				"import \"sys.proc\";\n" +
				"var pid = call g_get_pid();\n" +
				"var share = call g_wait_share(\"PID#\" + pid);\n" +
				"call g_stop_share(\"PID#\" + pid);\n" +
				"var args = call g_map_get(share, \"args\");\n" +
				"\n" +
				"var in = call g_create_pipe(\"PIPEIN#\" + pid);\n" +
				"var out = call g_create_pipe(\"PIPEOUT#\" + pid);\n" +
				"\n" +
				"var lower = 0 + call g_array_get(args, 0);\n" +
				"if (call g_is_null(lower)) {\n" +
				"    let lower = 1;\n" +
				"}\n" +
				"var upper = 0 + call g_array_get(args, 1);\n" +
				"if (call g_is_null(upper)) {\n" +
				"    let upper = lower;\n" +
				"    let lower = 1;\n" +
				"}\n" +
				"foreach (var i : call g_range(lower, upper)) {\n" +
				"    foreach (var j : call g_range_string(call g_to_string(i))) {\n" +
				"        call g_write_pipe(out, j);\n" +
				"    }\n" +
				"    call g_write_pipe(out, '\\n');\n" +
				"}\n" +
				"\n" +
				"call g_destroy_pipe(out);\n" +
				"call g_destroy_pipe(in);\n";
	}
}
