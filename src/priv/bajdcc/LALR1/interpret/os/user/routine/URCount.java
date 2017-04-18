package priv.bajdcc.LALR1.interpret.os.user.routine;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;

/**
 * 【用户态】计数
 *
 * @author bajdcc
 */
public class URCount implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/count";
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
				"\n" +
				"var count = 0;\n" +
				"var pipe = func [\"PIPE\"] ~(ch, out) {\n" +
				"    if (ch == '\\n' || ch == '\\r') {\n" +
				"        let count = count + 1;\n" +
				"        call g_write_pipe(out, \"\" + count + \"\\r\");\n" +
				"    }\n" +
				"};\n" +
				"\n" +
				"var in = call g_create_pipe(\"PIPEIN#\" + pid);\n" +
				"var out = call g_create_pipe(\"PIPEOUT#\" + pid);\n" +
				"call g_read_pipe_args(in, pipe, out);\n" +
				"call g_write_pipe(out, \"\" + count + \"\\n\");\n" +
				"call g_destroy_pipe(out);\n" +
				"\n\n";
	}
}
