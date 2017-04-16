package priv.bajdcc.LALR1.interpret.os.user.routine;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;

/**
 * 【用户态】复制流
 *
 * @author bajdcc
 */
public class URDup implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/dup";
	}

	@Override
	public String getCode() {
		return "import \"sys.base\";\n" +
				"import \"sys.list\";\n" +
				"import \"sys.proc\";\n" +
				"import \"sys.string\";\n" +
				"\n" +
				"call g_set_process_desc(\"dup routinue\");\n" +
				"var pid = call g_get_pid();\n" +
				"var share = call g_wait_share(\"PID#\" + pid);\n" +
				"call g_stop_share(\"PID#\" + pid);\n" +
				"var args = call g_map_get(share, \"args\");\n" +
				"var count = call g_array_get(args, 0);\n" +
				"if (call g_is_null(count)) {\n" +
				"    let count = \"2\";\n" +
				"}\n" +
				"let count = call g_string_atoi(count);\n" +
				"\n" +
				"var buf = [];\n" +
				"var pipe = func [\"PIPE\"] ~(ch, out) {\n" +
				"    if (ch == '\\n') {\n" +
				"        foreach (var i : call g_range(1, count)) {\n" +
				"            foreach (var j : call g_range_array(buf)) {\n" +
				"                call g_write_pipe(out, j);\n" +
				"            }\n" +
				"            call g_write_pipe(out, '\\n');\n" +
				"        }\n" +
				"        call g_array_clear(buf);\n" +
				"    } else {\n" +
				"        call g_array_add(buf, ch);\n" +
				"    }\n" +
				"};\n" +
				"\n" +
				"var in = call g_create_pipe(\"PIPEIN#\" + pid);\n" +
				"var out = call g_create_pipe(\"PIPEOUT#\" + pid);\n" +
				"call g_read_pipe_args(in, pipe, out);\n" +
				"call g_destroy_pipe(out);\n" +
				"\n\n";
	}
}
