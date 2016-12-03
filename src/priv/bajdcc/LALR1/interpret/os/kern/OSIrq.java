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
				"var interrupt_num = " + INT_NUM + ";\n" +
				"var int_table = [];\n" +
				"foreach (var i : call g_range(0, interrupt_num - 1)) {\n" +
				"    call g_array_add(int_table, g_null);\n" +
				"}\n" +
				"var g_add_int_proc = func ~(no, fn) {\n" +
				"    call g_array_set(int_table, no, fn);" +
				"};\n" +
				"export \"g_add_int_proc\";" +
				"var int_proc = func ~(arg) {\n" +
				"    var no = call g_map_get(arg, \"no\");\n" +
				"    var table = call g_map_get(arg, \"table\");\n" +
				"    call g_set_process_priority(no);\n" +
				"    var handle = call g_create_pipe(\"int#\" + no);\n" +
				"    for (;;) {\n" +
				"        var p = call g_array_get(table, no);\n" +
				"        if (!(call g_is_null(p))) {\n" +
				"            call g_read_pipe(handle, p);" +
				"        }\n" +
				"        call g_sleep(10 + no);\n" +
				"    }\n" +
				"    call g_destroy_pipe(handle);\n" +
				"};\n" +
				"foreach (var j : call g_range(0, interrupt_num - 1)) {\n" +
				"    var args = {};\n" +
				"    call g_map_put(args, \"no\", j);\n" +
				"    call g_map_put(args, \"table\", int_table);\n" +
				"    call g_create_process_args(int_proc, args);\n" +
				"    call g_printn(\"Create int_proc: #\" + j);\n" +
				"}\n";
	}
}
