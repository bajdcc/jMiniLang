package priv.bajdcc.LALR1.interpret.module;

import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 【模块】服务模块
 *
 * @author bajdcc
 */
public class ModuleTask implements IInterpreterModule {

	private static ModuleTask instance = new ModuleTask();

	public static ModuleTask getInstance() {
		return instance;
	}

	public static final int TASK_NUM = 16;

	@Override
	public String getModuleName() {
		return "sys.task";
	}

	@Override
	public RuntimeCodePage getCodePage() throws Exception {
		String base = "import \"sys.base\";\n" +
				"import \"sys.list\";\n" +
				"import \"sys.proc\";\n" +
				"\n" +
				"var task_num = " + TASK_NUM + ";\n" +
				"var g_task_init = func ~() {\n" +
				"    var task_table = [];\n" +
				"    call g_start_share(\"TASK#TABLE\", task_table);\n" +
				"    foreach (var i : call g_range(0, task_num - 1)) {\n" +
				"        call g_array_add(task_table, g_null);\n" +
				"    }\n" +
				"    var waiting_list = [];\n" +
				"    call g_start_share(\"TASK#LIST\", waiting_list);\n" +
				"};\n" +
				"export \"g_task_init\";\n" +
				"\n" +
				"var g_task_add_proc = func ~(no, data) {\n" +
				"    var task_table = call g_query_share(\"TASK#TABLE\");\n" +
				"    call g_array_set(task_table, no, data);\n" +
				"    call g_printn(\"Task #\" + no + \" created\");\n" +
				"};\n" +
				"export \"g_task_add_proc\";\n" +
				"\n" +
				"var g_task_get = func ~(tid, msg) {\n" +
				"    var waiting_list = call g_query_share(\"TASK#LIST\");\n" +
				"    var pid = call g_get_pid();\n" +
				"    var m = {};\n" +
				"    call g_map_put(m, \"pid\", pid);\n" +
				"    call g_map_put(m, \"tid\", tid);\n" +
				"    call g_map_put(m, \"msg\", msg);\n" +
				"    call g_start_share(\"MSG#\" + pid, m);\n" +
				"    call g_lock_share(\"TASK#LIST\");\n" +
				"    call g_array_add(waiting_list, pid);\n" +
				"    call g_unlock_share(\"TASK#LIST\");\n" +
				"    var handle = call g_create_pipe(\"int#1\");\n" +
				"    call g_write_pipe(handle, '@');\n" +
				"    var h = call g_wait_pipe(\"IPC#\" + pid);\n" +
				"    var f = func ~(ch) {" +
				"        if (ch == 'E') { call g_destroy_pipe(h); }\n" +
				"    };\n" +
				"    call g_read_pipe(h, f);\n" +
				"    call g_stop_share(\"MSG#\" + pid);\n" +
				"};\n" +
				"export \"g_task_get\";\n" +
				"\n" +
				"var task_handler = func ~(ch) {\n" +
				"    var waiting_list = call g_query_share(\"TASK#LIST\");\n" +
				"    var task_table = call g_query_share(\"TASK#TABLE\");\n" +
				"    call g_lock_share(\"TASK#LIST\");\n" +
				"    var pid = call g_array_get(waiting_list, 0);\n" +
				"    call g_array_remove(waiting_list, 0);\n" +
				"    call g_unlock_share(\"TASK#LIST\");\n" +
				"    var m = call g_query_share(\"MSG#\" + pid);\n" +
				"    var tid = call g_map_get(m, \"tid\");\n" +
				"    let tid = call g_task_get_id_by_name(tid);\n" +
				"    var msg = call g_map_get(m, \"msg\");\n" +
				"    var data = call g_array_get(task_table, tid);\n" +
				"    if (call g_is_null(data)) {\n" +
				"        call g_map_put(msg, \"error\", 1);\n" +
				"        call g_map_put(msg, \"val\", \"invalid task name\");\n" +
				"    } else {\n" +
				"        call g_map_put(msg, \"error\", 0);\n" +
				"        call g_start_share(\"TASKDATA#\" + tid, msg);\n" +
				"        call g_start_share(\"TASKCALLER#\" + tid, pid);\n" +
				"        var h = call g_create_pipe(\"TASKSEND#\" + tid);\n" +
				"        call g_write_pipe(h, ch);\n" +
				"        var h2 = call g_wait_pipe(\"TASKRECV#\" + tid);\n" +
				"        call g_write_pipe(h2, 'E');\n" +
				"        call g_stop_share(\"TASKDATA#\" + tid);\n" +
				"        call g_stop_share(\"TASKCALLER#\" + tid);\n" +
				"    }\n" +
				"    var handle = call g_create_pipe(\"IPC#\" + pid);\n" +
				"    call g_write_pipe(handle, 'E');\n" +
				"};\n" +
				"\n" +
				"var g_task_handler = func ~(ch) -> call task_handler(ch);\n" +
				"export \"g_task_handler\";\n" +
				"" +
				"var g_task_get_id_by_name = func ~(name) {\n" +
				"    var task_name_table = call g_query_share(\"TASK#NAMELIST\");\n" +
				"    foreach (var i : call g_range(0, " + TASK_NUM + " - 1)) {\n" +
				"        var t = call g_array_get(task_name_table, i);\n" +
				"        if (!call g_is_null(t) && t == name) {\n" +
				"            return i;\n" +
				"        }\n" +
				"    }\n" +
				"    return g_null;\n" +
				"};\n" +
				"export \"g_task_get_id_by_name\";\n";

		Grammar grammar = new Grammar(base);
		RuntimeCodePage page = grammar.getCodePage();
		IRuntimeDebugInfo info = page.getInfo();
		buildMethod(info);

		return page;
	}

	private void buildMethod(IRuntimeDebugInfo info) {
		info.addExternalFunc("g_task_get_time", new IRuntimeDebugExec() {
			@Override
			public String getDoc() {
				return "获取当前时间";
			}

			@Override
			public RuntimeObjectType[] getArgsType() {
				return new RuntimeObjectType[] { RuntimeObjectType.kString };
			}

			@Override
			public RuntimeObject ExternalProcCall(List<RuntimeObject> args,
			                                      IRuntimeStatus status) throws Exception {
				String format = String.valueOf(args.get(0).getObj());
				return new RuntimeObject(new SimpleDateFormat(format).format(new Date()));
			}
		});
	}
}
