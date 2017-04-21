package priv.bajdcc.LALR1.interpret.os.ui;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;

/**
 * 【界面】时钟
 *
 * @author bajdcc
 */
public class UIClock implements IOSCodePage {
	@Override
	public String getName() {
		return "/ui/clock";
	}

	@Override
	public String getCode() {
		return "import \"sys.base\";\n" +
				"import \"sys.list\";\n" +
				"import \"sys.proc\";\n" +
				"import \"sys.task\";\n" +
				"import \"sys.remote\";\n" +
				"\n" +
				"call g_set_process_desc(\"clock ui\");\n" +
				"call g_set_process_priority(80);\n" +
				"\n" +
				"var init = func ~() {\n" +
				"    call g_task_get_fast_arg(\"ui\", \"path\", \"@M 100 100@@l 300 0@@l 0 50@@l -300 0@@l 0 -50@\");\n" +
				"};\n" +
				"\n" +
				"var draw = func ~() {\n" +
				"    var time = call g_task_get_fast(\"system\", \"now\");\n" +
				"    call g_task_get_fast_arg(\"ui\", \"path\", \"@M 110 110@@R 390 140@@M 140 130@#\" + time + \"#\");\n" +
				"    call g_task_sleep(1);\n" +
				"};\n" +
				"\n" +
				"call init();\n" +
				"\n" +
				"while (call g_query_share(\"UI#clock\")) {\n" +
				"    call draw();\n" +
				"}\n" +
				"\n";
	}
}
