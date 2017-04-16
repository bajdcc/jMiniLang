package priv.bajdcc.LALR1.interpret.os.user.routine;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;

/**
 * 【用户态】过滤流
 *
 * @author bajdcc
 */
public class URGrep implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/grep";
	}

	@Override
	public String getCode() {
		return "import \"sys.base\";\n" +
				"import \"sys.list\";\n" +
				"import \"sys.proc\";\n" +
				"import \"sys.string\";\n" +
				"\n" +
				"call g_set_process_desc(\"grep routinue\");\n" +
				"// USE KMP ALGORITHM.\n" +
				"var pid = call g_get_pid();\n" +
				"var share = call g_wait_share(\"PID#\" + pid);\n" +
				"call g_stop_share(\"PID#\" + pid);\n" +
				"var args = call g_map_get(share, \"args\");\n" +
				"var pat = call g_array_get(args, 0);\n" +
				"if (call g_is_null(pat)) {\n" +
				"    var _pipe = func [\"PIPE\"] ~(ch, out) {\n" +
				"        call g_write_pipe(out, ch);\n" +
				"    };\n" +
				"\n" +
				"    var _in = call g_create_pipe(\"PIPEIN#\" + pid);\n" +
				"    var _out = call g_create_pipe(\"PIPEOUT#\" + pid);\n" +
				"    call g_read_pipe_args(_in, _pipe, _out);\n" +
				"    call g_destroy_pipe(_out);\n" +
				"    return;\n" +
				"}\n" +
				"\n" +
				"var patlen = call g_string_length(pat);\n" +
				"var m1 = g_minus_1;\n" +
				"\n" +
				"var next = func [\"CALC NEXT\"] ~(str) {\n" +
				"    var n = [];\n" +
				"    var u = 0;\n" +
				"    var v = g_minus_1;\n" +
				"    call g_array_add(n, g_minus_1);\n" +
				"    for (var t = 1; t < patlen; t++) {\n" +
				"        call g_array_add(n, g_minus_1);\n" +
				"    }\n" +
				"    while (u < patlen - 1)\n" +
				"    {\n" +
				"        if (v == m1 || (call g_array_get(str, u) == call g_array_get(str, v)))\n" +
				"        {\n" +
				"            u++;\n" +
				"            v++;\n" +
				"            call g_array_set(n, u, v);\n" +
				"        }\n" +
				"        else\n" +
				"        {\n" +
				"            let v = call g_array_get(n, v);\n" +
				"        }\n" +
				"    }\n" +
				"    return n;\n" +
				"};\n" +
				"\n" +
				"var find = func [\"FIND WITH NEXT\"] ~(str, n) {\n" +
				"    var i = 0;\n" +
				"    var j = 0;\n" +
				"    var slen = call g_string_length(str);\n" +
				"    while (i < slen && j < patlen)\n" +
				"    {\n" +
				"        if (j == m1 || (call g_string_get(str, i) == call g_string_get(pat, j)))\n" +
				"        {\n" +
				"            i++; j++;\n" +
				"        }\n" +
				"        else\n" +
				"        {\n" +
				"            let j = call g_array_get(n, j);\n" +
				"        }\n" +
				"    }\n" +
				"    if (j == patlen)\n" +
				"    {\n" +
				"        return i - j;\n" +
				"    }\n" +
				"    return m1;\n" +
				"};\n" +
				"\n" +
				"var nextarr = call next(pat);\n" +
				"\n" +
				"var buf = [];\n" +
				"var pipe = func [\"PIPE\"] ~(ch, out) {\n" +
				"    if (ch == '\\n') {\n" +
				"        var str = call g_string_build(buf);\n" +
				"        var idx = call find(str, nextarr);\n" +
				"        if (idx != m1) {\n" +
				"            foreach (var i : call g_range_array(buf)) {\n" +
				"                call g_write_pipe(out, i);\n" +
				"            }\n" +
				"            call g_write_pipe(out, \"\\n\");" +
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
