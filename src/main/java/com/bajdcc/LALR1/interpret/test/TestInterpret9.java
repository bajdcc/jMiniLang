package com.bajdcc.LALR1.interpret.test;

import com.bajdcc.LALR1.grammar.Grammar;
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import com.bajdcc.LALR1.grammar.runtime.RuntimeException;
import com.bajdcc.LALR1.interpret.Interpreter;
import com.bajdcc.LALR1.syntax.handler.SyntaxException;
import com.bajdcc.util.lexer.error.RegexException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@SuppressWarnings("unused")
public class TestInterpret9 {

	public static void main(String[] args) {
		try {
			String[] codes = new String[]{

					"import \"sys.base\"; import \"sys.proc\";\n" +
							"var a = func ~() {\n" +
							"    foreach (var i : call g_range(1, 10)) {\n" +
							"        call g_printn(\"[\" + call g_get_pid() + \"] Hello world!\");\n" +
							"    }\n" +
							"};\n" +
							"call g_create_process(a);\n" +
							"call a();\n" +
							"\n",

					"import \"sys.base\"; import \"sys.proc\";\n" +
							"var proc = func ~() {\n" +
							"    var handle = call g_create_pipe(\"test\");\n" +
							"    var print = func ~(ch) -> call g_print(ch);\n" +
							"    var pid = call g_get_pid();\n" +
							"    if (pid == 0) {\n" +
							"        foreach (var i : call g_range(1, 10)) {\n" +
							"            call g_printn(\"[\" + call g_get_pid() + \"] Hello world!\");\n" +
							"            call g_write_pipe(handle, \"\" + i);\n" +
							"        }\n" +
							"        call g_destroy_pipe(handle);\n" +
							"    } else {\n" +
							"        call g_printn(\"[\" + call g_get_pid() + \"] Hello world!\");\n" +
							"        call g_read_pipe(handle, print);\n" +
							"    }\n" +
							"};\n" +
							"call g_create_process(proc);\n" +
							"call proc();\n" +
							"\n",

					"import \"sys.base\";\n" +
							"import \"sys.list\";\n" +
							"import \"sys.proc\";\n" +
							"\n" +
							"var goods = [];\n" +
							"call g_start_share(\"goods\", goods);\n" +
							"var index = 1;\n" +
							"call g_start_share(\"index\", index);\n" +
							"var consumer = func ~() {\n" +
							"    for (;;) {\n" +
							"        var goods = call g_query_share(\"goods\");\n" +
							"        if (call g_is_null(goods)) {\n" +
							"            break;\n" +
							"        }\n" +
							"        var g = call g_array_pop(goods);\n" +
							"        if (!call g_is_null(g)) {\n" +
							"            call g_printn(\"Consumer#\" + call g_get_pid() + \" ---- get \" + g);\n" +
							"        }\n" +
							"    }\n" +
							"    call g_printn(\"Consumer#\" + call g_get_pid() + \" exit\");\n" +
							"};\n" +
							"var producer = func ~() {\n" +
							"    foreach (var i : call g_range(1, 5)) {\n" +
							"        var goods = call g_reference_share(\"goods\");\n" +
							"        call g_lock_share(\"index\");\n" +
							"        var index = call g_reference_share(\"index\");\n" +
							"        call g_printn(\"Producer#\" + call g_get_pid() + \" ++++ put \" + index);\n" +
							"        call g_array_add(goods, index);\n" +
							"        index++;\n" +
							"        call g_stop_share(\"index\");\n" +
							"        call g_unlock_share(\"index\");\n" +
							"        call g_stop_share(\"goods\");\n" +
							"    }\n" +
							"    call g_printn(\"Producer#\" + call g_get_pid() + \" exit\");\n" +
							"};\n" +
							"var create_consumer = func ~(n) {\n" +
							"    var handles = [];\n" +
							"    foreach (var i : call g_range(1, n)) {\n" +
							"        var h = call g_create_process(consumer);\n" +
							"        call g_array_add(handles, h);\n" +
							"        call g_printn(\"[\" + i + \"] Create consumer: #\" + h);\n" +
							"    }\n" +
							"    return handles;\n" +
							"};\n" +
							"var create_producer = func ~(n) {\n" +
							"    var handles = [];\n" +
							"    foreach (var i : call g_range(1, n)) {\n" +
							"        var h = call g_create_process(producer);\n" +
							"        call g_array_add(handles, h);\n" +
							"        call g_printn(\"[\" + i + \"] Create producer: #\" + h);\n" +
							"    }\n" +
							"    return handles;\n" +
							"};\n" +
							"\n" +
							"var consumers = call create_consumer(3);\n" +
							"var producers = call create_producer(4);\n" +
							"call g_printn(\"Waiting for producers to exit...\");\n" +
							"call g_join_process_array(producers);\n" +
							"call g_printn(\"Producers exit\");\n" +
							"call g_printn(\"Waiting for consumers to exit...\");\n" +
							"call g_stop_share(\"index\");\n" +
							"call g_stop_share(\"goods\");\n" +
							"call g_join_process_array(consumers);\n" +
							"call g_printn(\"Consumers exit\");\n" +
							"\n",

			};

			System.out.println(codes[codes.length - 1]);
			Interpreter interpreter = new Interpreter();
			Grammar grammar = new Grammar(codes[codes.length - 1]);
			//System.out.println(grammar.toString());
			RuntimeCodePage page = grammar.getCodePage();
			//System.out.println(page.toString());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			RuntimeCodePage.Companion.exportFromStream(page, baos);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			interpreter.run("test_1", bais);

		} catch (RegexException e) {
			System.err.println();
			System.err.println(e.getPosition() + "," + e.getMessage());
			e.printStackTrace();
		} catch (SyntaxException e) {
			System.err.println();
			System.err.println(e.getPosition() + "," + e.getMessage() + " "
					+ e.getInfo());
			e.printStackTrace();
		} catch (RuntimeException e) {
			System.err.println();
			System.err.println(e.getPosition() + ": " + e.getInfo());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println();
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
