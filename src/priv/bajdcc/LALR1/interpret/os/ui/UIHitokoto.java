package priv.bajdcc.LALR1.interpret.os.ui;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;

/**
 * 【界面】一言
 *
 * @author bajdcc
 */
public class UIHitokoto implements IOSCodePage {
	@Override
	public String getName() {
		return "/ui/hitokoto";
	}

	@Override
	public String getCode() {
		return "import \"sys.base\";\n" +
				"import \"sys.list\";\n" +
				"import \"sys.proc\";\n" +
				"import \"sys.task\";\n" +
				"import \"sys.remote\";\n" +
				"\n" +
				"call g_set_process_desc(\"hitokoto ui\");\n" +
				"call g_set_process_priority(81);\n" +
				"\n" +
				"var init = func ~() {\n" +
				"    call g_task_get_fast_arg(\"ui\", \"path\", \"@M 100 200@@l 500 0@@l 0 150@@l -500 0@@l 0 -150@\");\n" +
				"};\n" +
				"\n" +
				"var draw = func ~() {\n" +
				"    var obj = call g_task_get_fast_arg(\"net\", \"get_json\", \"http://api.hitokoto.cn/?c=a&encode=json\");\n" +
				"    if (call g_is_null(obj)) { call g_task_sleep(10); return; }\n" +
				"    var text = \" —— \" + call g_map_get(obj, \"from\") + \" —— \\n\" + call g_map_get(obj, \"hitokoto\");\n;" +
				"    call g_task_get_fast_arg(\"ui\", \"path\", \"@M 101 201@@R 599 349@@M 140 230@@W 450@$\" + text + \"$\");\n" +
				"    call g_task_sleep(8);\n" +
				"};\n" +
				"\n" +
				"call init();\n" +
				"\n" +
				"while (call g_query_share(\"UI#hitokoto\")) {\n" +
				"    call draw();\n" +
				"}\n" +
				"\n";
	}
}
