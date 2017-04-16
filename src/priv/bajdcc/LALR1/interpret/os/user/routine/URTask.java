package priv.bajdcc.LALR1.interpret.os.user.routine;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;

/**
 * 【用户态】服务
 *
 * @author bajdcc
 */
public class URTask implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/task";
	}

	@Override
	public String getCode() {
		return "import \"sys.base\";\n" +
				"import \"sys.list\";\n" +
				"import \"sys.string\";\n" +
				"import \"sys.proc\";\n" +
				"import \"sys.task\";\n" +
				"\n" +
				"call g_set_process_desc(\"task routinue\");\n" +
				"var pid = call g_get_pid();\n" +
				"var share = call g_wait_share(\"PID#\" + pid);\n" +
				"call g_stop_share(\"PID#\" + pid);\n" +
				"var args = call g_map_get(share, \"args\");\n" +
				"\n" +
				"var in = call g_create_pipe(\"PIPEIN#\" + pid);\n" +
				"var out = call g_create_pipe(\"PIPEOUT#\" + pid);\n" +
				"\n" +
				"if (call g_array_size(args) < 2) {\n" +
				"    call g_write_pipe(out, \"Error: missing arguments.\\n\");\n" +
				"    call g_destroy_pipe(out);\n" +
				"    call g_destroy_pipe(in);\n" +
				"    return;\n" +
				"}\n" +
				"\n" +
				"var tid = call g_array_get(args, 0);\n" +
				"var id = call g_array_get(args, 1);\n" +
				"\n" +
				"var msg = {};\n" +
				"call g_map_put(msg, \"id\", id);\n" +
				"call g_map_put(msg, \"arg\", args);\n" +
				"call g_task_get(tid, msg);\n" +
				"\n" +
				"var error = call g_map_get(msg, \"error\");\n" +
				"var val = call g_map_get(msg, \"val\");\n" +
				"if (error == 1) {\n" +
				"    call g_write_pipe(out, \"Error: \" + val);\n" +
				"    call g_write_pipe(out, '\\n');\n" +
				"} else {\n" +
				"    call g_write_pipe(out, val);\n" +
				"    call g_write_pipe(out, '\\n');\n" +
				"}\n" +
				"\n" +
				"call g_destroy_pipe(out);\n" +
				"call g_destroy_pipe(in);\n";
	}
}
