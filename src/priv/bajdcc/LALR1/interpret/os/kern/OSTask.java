package priv.bajdcc.LALR1.interpret.os.kern;

import priv.bajdcc.LALR1.interpret.module.ModuleTask;
import priv.bajdcc.LALR1.interpret.os.IOSCodePage;

/**
 * 【内核】服务
 *
 * @author bajdcc
 */
public class OSTask implements IOSCodePage {
	@Override
	public String getName() {
		return "/kern/task";
	}

	@Override
	public String getCode() {
		return "// KERNEL ENTRY BY BAJDCC\n" +
				"import \"sys.base\";\n" +
				"import \"sys.list\";\n" +
				"import \"sys.proc\";\n" +
				"" +
				"call g_set_process_desc(\"task host\");\n" +
				"var task_num = " + ModuleTask.TASK_NUM + ";\n" +
				"var task_name_table = [];\n" +
				"call g_start_share(\"TASK#NAMELIST\", task_name_table);\n" +
				"\n" +
				"foreach (var i : call g_range(0, task_num - 1)) {\n" +
				"    call g_array_add(task_name_table, g_null);\n" +
				"}\n" +
				"\n" +
				"call g_array_set(task_name_table, 1, \"system\");\n" +
				"call g_array_set(task_name_table, 2, \"util\");\n" +
				"call g_array_set(task_name_table, 3, \"ui\");\n" +
				"call g_array_set(task_name_table, 4, \"net\");\n" +
				"\n" +
				"foreach (var j : call g_range(0, task_num - 1)) {\n" +
				"    var t = call g_array_get(task_name_table, j);\n" +
				"    if (!call g_is_null(t)) {\n" +
				"        call g_load_x(\"/task/\" + t);\n" +
				"    }\n" +
				"}\n" +
				"";
	}
}
