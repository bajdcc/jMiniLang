package priv.bajdcc.LALR1.interpret.os.kern;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;

/**
 * 【内核】IRQ中断
 *
 * @author bajdcc
 */
public class OSIrq implements IOSCodePage {

	private static final int INT_NUM = 16;

	@Override
	public String getName() {
		return "/kern/irq";
	}

	@Override
	public String getCode() {
		return "import \"sys.base\";\n" +
				"import \"sys.list\";\n" +
				"import \"sys.proc\";\n" +
				"import \"sys.ui\";\n" +
				"var interrupt_num = " + INT_NUM + ";\n" +
				"var int_table = [];\n" +
				"foreach (var i : call g_range(0, interrupt_num - 1)) {\n" +
				"    call g_array_add(int_table, g_null);\n" +
				"}\n" +
				"var add_int_proc = func ~(no, fn) {\n" +
				"    call g_array_set(int_table, no, fn);" +
				"};\n" +
				"var int_proc = func ~(arg) {\n" +
				"    var no = call g_map_get(arg, \"no\");\n" +
				"    var table = call g_map_get(arg, \"table\");\n" +
				"    call g_set_process_priority(no);\n" +
				"    var state = [];\n" +
				"    call g_array_add(state, true);\n" +
				"    call g_start_share(\"IRQ#ON.\" + no, state);\n" +
				"    var handle = call g_create_pipe(\"int#\" + no);\n" +
				"    for (;;) {\n" +
				"        var p = call g_array_get(table, no);\n" +
				"        if (!(call g_is_null(p))) {\n" +
				"            call g_read_pipe(handle, p);\n" +
				"        }\n" +
				"        call g_sleep(10 + no);\n" +
				"        var _state_ = call g_query_share(\"IRQ#ON.\" + no);\n" +
				"        var on = call g_array_get(_state_, 0);\n" +
				"        if (!on) { break; }\n" +
				"    }\n" +
				"    //call g_printn(\"int_proc: #\" + no + \" exit\");\n" +
				"};\n" +
				"foreach (var j : call g_range(0, interrupt_num - 1)) {\n" +
				"    var args = {};\n" +
				"    call g_map_put(args, \"no\", j);\n" +
				"    call g_map_put(args, \"table\", int_table);\n" +
				"    call g_create_process_args(int_proc, args);\n" +
				"    //call g_printn(\"Create int_proc: #\" + j);\n" +
				"}\n" +
				"\n" +
				"var destroy_int = func ~() {\n" +
				"    foreach (var i : call g_range(0, " + INT_NUM + " - 1)) {\n" +
				"        var _state_ = call g_query_share(\"IRQ#ON.\" + i);\n" +
				"        call g_array_set(_state_, 0, false);\n" +
				"        var handle = call g_create_pipe(\"int#\" + i);\n" +
				"        call g_destroy_pipe(handle);\n" +
				"        call g_sleep(20);\n" +
				"    }\n" +
				"};\n" +
				"var schd_handler = func ~(ch) {\n" +
				"    if (ch == 'E') {\n" +
				"        var _state_ = call g_query_share(\"SCHD#ON\");\n" +
				"        call g_array_set(_state_, 0, false);\n" +
				"        call g_create_process(destroy_int);\n" +
				"    }\n" +
				"};\n" +
				"call add_int_proc(10, schd_handler);\n" +
				"var print_handler = func ~(ch) {\n" +
				"    call g_ui_print_internal(ch);\n" +
				"};\n" +
				"call add_int_proc(12, print_handler);\n" +
				"";
	}
}
