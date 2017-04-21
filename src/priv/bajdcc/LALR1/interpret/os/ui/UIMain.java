package priv.bajdcc.LALR1.interpret.os.ui;

import priv.bajdcc.LALR1.interpret.module.ModuleRemote;
import priv.bajdcc.LALR1.interpret.os.IOSCodePage;

/**
 * 【界面】入口
 *
 * @author bajdcc
 */
public class UIMain implements IOSCodePage {
	@Override
	public String getName() {
		return "/ui/main";
	}

	@Override
	public String getCode() {
		return "// KERNEL ENTRY BY BAJDCC\n" +
				"import \"sys.base\";\n" +
				"import \"sys.list\";\n" +
				"import \"sys.proc\";\n" +
				"" +
				"call g_set_process_desc(\"ui host\");\n" +
				"var ui_num = " + ModuleRemote.UI_NUM + ";\n" +
				"var ui_name_table = [];\n" +
				"call g_start_share(\"UI#NAMELIST\", ui_name_table);\n" +
				"\n" +
				"foreach (var i : call g_range(0, ui_num - 1)) {\n" +
				"    call g_array_add(ui_name_table, g_null);\n" +
				"}\n" +
				"\n" +
				"call g_array_set(ui_name_table, 1, \"clock\");\n" +
				"\n" +
				"foreach (var j : call g_range(0, ui_num - 1)) {\n" +
				"    var t = call g_array_get(ui_name_table, j);\n" +
				"    if (!call g_is_null(t)) {\n" +
				"        call g_start_share(\"UI#\" + t, true);\n" +
				"        call g_load_x(\"/ui/\" + t);\n" +
				"    }\n" +
				"}\n";
	}
}
