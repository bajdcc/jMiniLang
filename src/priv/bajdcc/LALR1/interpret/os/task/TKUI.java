package priv.bajdcc.LALR1.interpret.os.task;

import priv.bajdcc.LALR1.interpret.module.ModuleRemote;
import priv.bajdcc.LALR1.interpret.os.IOSCodePage;

/**
 * 【服务】用户界面
 *
 * @author bajdcc
 */
public class TKUI implements IOSCodePage {
	@Override
	public String getName() {
		return "/task/ui";
	}

	@Override
	public String getCode() {
		return "import \"sys.base\";\n" +
				"import \"sys.list\";\n" +
				"import \"sys.proc\";\n" +
				"import \"sys.task\";\n" +
				"import \"sys.remote\";\n" +
				"\n" +
				"call g_set_process_desc(\"ui service\");\n" +
				"call g_set_process_priority(74);\n" +
				"\n" +
				"var tid = 3;\n" +
				"var handle = call g_create_pipe(\"TASKSEND#\" + tid);\n" +
				"\n" +
				"var time = func ~(msg, caller) {\n" +
				"    var id = call g_map_get(msg, \"id\");\n" +
				"    if (call g_is_null(id)) {\n" +
				"        call g_map_put(msg, \"error\", 1);\n" +
				"        call g_map_put(msg, \"val\", \"invalid task argument - id\");\n" +
				"        return;\n" +
				"    }\n" +
				"    if (id == \"print\") {\n" +
				"        var arg = call g_map_get(msg, \"arg\");\n" +
				"        var str = \"\";\n" +
				"        var len = call g_array_size(arg);\n" +
				"        foreach (var i : call g_range(2, len - 1)) {\n" +
				"            let str = str + call g_array_get(arg, i);\n" +
				"        }\n" +
				"        call g_remote_print(str);\n" +
				"        call g_map_put(msg, \"val\", str);\n" +
				"    } else if (id == \"path\") {\n" +
				"        var arg = call g_map_get(msg, \"arg\");\n" +
				"        var len = call g_array_size(arg);\n" +
				"        var str = \"\";\n" +
				"        foreach (var i : call g_range(2, len - 1)) {\n" +
				"            let str = str + call g_array_get(arg, i) + \" \";\n" +
				"        }\n" +
				"        call g_remote_print(str);\n" +
				"        call g_map_put(msg, \"val\", str);\n" +
				"    }" +
				"};\n" +
				"\n" +
				"var handler = func ~(ch) {\n" +
				"    if (ch == 'E') {\n" +
				"        call g_destroy_pipe(handle);\n" +
				"        var ui_num = " + ModuleRemote.UI_NUM + ";\n" +
				"        var ui_name_table = call g_query_share(\"UI#NAMELIST\");\n" +
				"        \n" +
				"        foreach (var i : call g_range(0, ui_num - 1)) {\n" +
				"            var ui_name = call g_array_get(ui_name_table, i);\n" +
				"            if (!call g_is_null(ui_name)) {\n" +
				"                call g_create_share(\"UI#\" + ui_name, false);\n" +
				"            }\n" +
				"        }\n" +
				"        return;\n" +
				"    }\n" +
				"    var msg = call g_query_share(\"TASKDATA#\" + tid);\n" +
				"    var caller = call g_query_share(\"TASKCALLER#\" + tid);\n" +
				"    call time(msg, caller);\n" +
				"    var handle = call g_create_pipe(\"TASKRECV#\" + tid);\n" +
				"    var f = func ~(ch) {\n" +
				"        if (ch == 'E') { call g_destroy_pipe(handle); }" +
				"    };\n" +
				"    call g_read_pipe(handle, f);\n" +
				"};\n" +
				"\n" +
				"var data = {};\n" +
				"call g_task_add_proc(3, data);\n" +
				"call g_start_share(\"REMOTE#MUTEX\", \"remote mutex\");\n" +
				"call g_load_x(\"/ui/main\");\n" +
				"\n" +
				"call g_read_pipe(handle, handler);\n";
	}
}
