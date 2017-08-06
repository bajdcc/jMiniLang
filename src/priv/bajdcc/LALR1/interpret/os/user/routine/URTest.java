package priv.bajdcc.LALR1.interpret.os.user.routine;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;

/**
 * 【用户态】测试
 *
 * @author bajdcc
 */
public class URTest implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/test";
	}

	@Override
	public String getCode() {
		return "import \"sys.base\";\n" +
				"import \"sys.list\";\n" +
				"import \"sys.proc\";\n" +
				"import \"sys.task\";\n" +
				"import \"sys.string\";\n" +
				"\n" +
				"call g_set_process_desc(\"test routinue\");\n" +
				"var pid = call g_get_pid();\n" +
				"var share = call g_wait_share(\"PID#\" + pid);\n" +
				"call g_stop_share(\"PID#\" + pid);\n" +
				"var args = call g_map_get(share, \"args\");\n" +
				"\n" +
				"var in = call g_create_pipe(\"PIPEIN#\" + pid);\n" +
				"var out = call g_create_pipe(\"PIPEOUT#\" + pid);\n" +
				"\n" +
				"var put = func [\"PIPE\"] ~(ch) {\n" +
				"    call g_write_pipe(out, ch);\n" +
				"};\n" +
				"var puts = func [\"PIPE\"] ~(str) {\n" +
				"    foreach (var c : call g_range_string(str)) {\n" +
				"        call g_write_pipe(out, c);\n" +
				"    }\n" +
				"};\n" +
				"var signal = \"PIDSIG#\" + pid;\n" +
				"call g_start_share(signal, true);\n" +
				"\n" +
				"/* 创建场景 */\n" +
				"var create_stage = func ~(f) -> call f();\n" +
				"\n" +
				"/* 重复操作 */\n" +
				"var real_repeat = func ~(_operation, _arg, _start, _end) {\n" +
				"    var repeat = func ~(operation, arg, start, end) {\n" +
				"        var index = start;\n" +
				"        var repeat0 = func ~() {\n" +
				"            if (index >= end) { return; }\n" +
				"            call operation(arg, index);\n" +
				"            return call repeat(operation, arg, ++index, end);\n" +
				"        };\n" +
				"        return repeat0;\n" +
				"    };\n" +
				"    var repear_f = func ~() -> call repeat(_operation, _arg, _start, _end);\n" +
				"    call(func ~(f) {\n" +
				"        while (!(call g_is_null(f)) && (call g_get_type_ordinal(f) == 8)) {\n" +
				"            let f = call f();\n" +
				"        }\n" +
				"    })(repear_f);\n" +
				"};\n" +
				"    \n" +
				"/* 打字效果 */\n" +
				"var word_typewrite = func ~(str, span) {\n" +
				"    var print = func ~(a, n) {\n" +
				"        call put(call g_string_char(a, n));\n" +
				"        call g_sleep(span);\n" +
				"    };\n" +
				"    call real_repeat(print, str, 0, call g_string_length(str));\n" +
				"};\n" +
				"\n" +
				"/* 清屏 */\n" +
				"var stage_clear = func ~() {\n" +
				"    call word_typewrite(\"Are you ready?\", 200);\n" +
				"    call word_typewrite(\"  3!  2!  1!\n\", 1500);\n" +
				"    call word_typewrite(\"Let's go!!!   \n\", 1000);\n" +
				"    call put('\\f');\n" +
				"};\n" +
				"\n" +
				"/* 场景一 */\n" +
				"var stage_1 = func ~() {\n" +
				"    call puts(call g_string_rep(\" \", 31));\n" +
				"    call word_typewrite(\"- Stage 1 -\n\", 2000);\n" +
				"    call word_typewrite(\"* Hello world! 你好！\n\", 300);\n" +
				"    call word_typewrite(\"* This is a test program. 这是一个测试程序。\n\", 400);\n" +
				"    call word_typewrite(\"* Made by bajdcc. 由bajdcc编写。\n\", 1000);\n" +
				"    call word_typewrite(\"* 项目网址在 https://github.com/bajdcc/jMiniLang 上。\n\", 600);\n" +
				"    call word_typewrite(\"* 这是我做的一个脚本操作系统。\n\", 1000);\n" +
				"    call word_typewrite(\"* 支持闭包、进程、管道、互斥等特性。\n\", 1000);\n" +
				"    call word_typewrite(\"* 不过由于设计不足以及Java的使用，脚本运行还是太慢。\n\", 1000);\n" +
				"    call word_typewrite(\"* 最让我兴奋的是语法分析的实现、虚拟机的构建、同步/异步语义的实现。\n\", 1000);\n" +
				"    call word_typewrite(\"* 进程、管道、互斥是这个操作系统的基础。\n\", 1000);\n" +
				"    call word_typewrite(\"\n\n\n\n\", 1000);\n" +
				"    call puts(call g_string_rep(\" \", 31));\n" +
				"    call word_typewrite(\"- @bajdcc -\n\", 2000);\n" +
				"};\n" +
				"\n" +
				"call create_stage(stage_clear);\n" +
				"call create_stage(stage_1);\n" +
				"\n" +
				"call g_stop_share(signal);\n" +
				"call g_destroy_pipe(out);\n" +
				"call g_destroy_pipe(in);\n";
	}
}
