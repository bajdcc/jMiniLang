package priv.bajdcc.LALR1.interpret.os.user.routine;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;

/**
 * 【用户态】管道
 *
 * @author bajdcc
 */
public class URPipe implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/pipe";
	}

	@Override
	public String getCode() {
		return "import \"sys.base\";\n" +
				"import \"sys.proc\";\n" +
				"\n" +
				"var pipe = func [\"PIPE\"] ~(ch, out) {\n" +
				"    call g_write_pipe(out, ch);\n" +
				"};\n" +
				"\n" +
				"var pid = call g_get_pid();\n" +
				"var in = call g_create_pipe(\"PIPEIN#\" + pid);\n" +
				"var out = call g_create_pipe(\"PIPEOUT#\" + pid);\n" +
				"call g_read_pipe_args(in, pipe, out);\n" +
				"call g_destroy_pipe(out);\n";
	}
}
