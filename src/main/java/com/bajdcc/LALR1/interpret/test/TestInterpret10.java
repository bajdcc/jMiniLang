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
public class TestInterpret10 {

	public static void main(String[] args) {
		try {
			String[] codes = new String[]{

					"import \"sys.base\";\n" +
							"import \"sys.list\";\n" +
							"import \"sys.proc\";\n" +
							"\n" +
							"var pc_router = 5;\n" +
							"call g_start_share(\"pc_router\", pc_router);\n" +
							"\n" +
							"var pc = func ~(index) {\n" +
							"    var pc_router = call g_query_share(\"pc_router\");\n" +
							"    var name = \"pc_\" + index;\n" +
							"    var router = index / pc_router;\n" +
							"    var display = \"PC #\" + index;\n" +
							"    call g_printn(display + \" started\");\n" +
							"    call g_sleep(50);\n" +
							"    var handle = call g_create_pipe(name);\n" +
							"    call g_printn(display + \" connecting...\");\n" +
							"    var router_connection = \"router#\" + router;\n" +
							"    for (;;) {\n" +
							"        call g_sleep(100);\n" +
							"        call g_lock_share(router_connection);\n" +
							"        var connection = call g_query_share(router_connection);\n" +
							"        if (call g_is_null(connection)) {\n" +
							"            call g_unlock_share(router_connection);\n" +
							"            continue;\n" +
							"        }\n" +
							"        call g_printn(display + \" connecting to #\" + router);\n" +
							"        call g_array_add(connection, index);\n" +
							"        call g_unlock_share(router_connection);\n" +
							"        break;\n" +
							"    }\n" +
							"    var get_id = func ~(ch) {\n" +
							"        if (ch == '@') {\n" +
							"            call g_printn(display + \" connected to router #\" + router);\n" +
							"        }\n" +
							"    };\n" +
							"    call g_read_pipe(handle, get_id);\n" +
							"    call g_sleep(1000);\n" +
							"    call g_printn(display + \" stopped\");\n" +
							"};\n" +
							"\n" +
							"var router = func ~(index) {\n" +
							"    var pc_router = call g_query_share(\"pc_router\");\n" +
							"    var name = \"router_\" + index;\n" +
							"    var display = \"Router #\" + index;\n" +
							"    call g_printn(display + \" started\");\n" +
							"    var router_connection = \"router#\" + index;\n" +
							"    var connection = [];\n" +
							"    var list = [];\n" +
							"    call g_start_share(router_connection, connection);\n" +
							"    var connected = 0;\n" +
							"    var handle_pc = func ~(args) {\n" +
							"        var connected = call g_array_get(args, 0);\n" +
							"        var pc_router = call g_array_get(args, 1);\n" +
							"        var router_connection = call g_array_get(args, 2);\n" +
							"        var display = call g_array_get(args, 3);\n" +
							"        var list = call g_array_get(args, 4);\n" +
							"        for (;;) {\n" +
							"            call g_sleep(100);\n" +
							"            call g_lock_share(router_connection);\n" +
							"            var connection = call g_query_share(router_connection);\n" +
							"            var new_pc = call g_array_pop(connection);\n" +
							"            if (call g_is_null(new_pc)) {\n" +
							"                call g_unlock_share(router_connection);\n" +
							"                continue;\n" +
							"            }\n" +
							"            connected++;\n" +
							"            call g_array_add(list, new_pc);\n" +
							"            call g_printn(display + \" connecting to pc #\" + new_pc);\n" +
							"            call g_unlock_share(router_connection);\n" +
							"            var name = \"pc_\" + new_pc;\n" +
							"            var handle = call g_create_pipe(name);\n" +
							"            call g_destroy_pipe(handle);\n" +
							"            call g_printn(display + \" connected to #\" + new_pc);\n" +
							"            if (connected == pc_router) { break; }\n" +
							"        }\n" +
							"    };\n" +
							"    var args = [];\n" +
							"    call g_array_add(args, connected);\n" +
							"    call g_array_add(args, pc_router);\n" +
							"    call g_array_add(args, router_connection);\n" +
							"    call g_array_add(args, display);\n" +
							"    call g_array_add(args, list);\n" +
							"    call g_join_process(call g_create_process_args(handle_pc, args));\n" +
							"    var stop_pc = func ~(args) {\n" +
							"        var display = call g_array_get(args, 3);\n" +
							"        var list = call g_array_get(args, 4);\n" +
							"        var size = call g_array_size(list);\n" +
							"        for (var i = 0; i < size; i++) {\n" +
							"            call g_sleep(10);\n" +
							"            var name = \"pc_\" + call g_array_get(list, i);\n" +
							"            var handle = call g_create_pipe(name);\n" +
							"            call g_write_pipe(handle, \"!\");\n" +
							"            call g_printn(display + \" disconnected with #\" + i);\n" +
							"        }\n" +
							"    };\n" +
							"    call g_sleep(100);\n" +
							"    call g_join_process(call g_create_process_args(stop_pc, args));\n" +
							"    call g_printn(display + \" stopped\");\n" +
							"};\n" +
							"\n" +
							"var create_pc = func ~(n) {\n" +
							"    var handles = [];\n" +
							"    foreach (var i : call g_range(0, n - 1)) {\n" +
							"        var h = call g_create_process_args(pc, i);\n" +
							"        call g_array_add(handles, h);\n" +
							"        call g_printn(\"Create pc: #\" + i);\n" +
							"    }\n" +
							"    return handles;\n" +
							"};\n" +
							"var create_router = func ~(n) {\n" +
							"    var handles = [];\n" +
							"    foreach (var i : call g_range(0, n - 1)) {\n" +
							"        var h = call g_create_process_args(router, i);\n" +
							"        call g_array_add(handles, h);\n" +
							"        call g_printn(\"Create router: #\" + i);\n" +
							"    }\n" +
							"    return handles;\n" +
							"};\n" +
							"\n" +
							"call g_printn(\"Starting pc...\");\n" +
							"var pcs = call create_pc(5);\n" +
							"call g_printn(\"Starting router...\");\n" +
							"var routers = call create_router(1);\n" +
							"call g_join_process_array(pcs);\n" +
							"call g_join_process_array(routers);\n" +
							"\n",

			};

			System.out.println(codes[codes.length - 1]);
			Interpreter interpreter = new Interpreter();
			Grammar grammar = new Grammar(codes[codes.length - 1]);
			//System.out.println(grammar.toString());
			RuntimeCodePage page = grammar.getCodePage();
			//System.out.println(page.toString());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			RuntimeCodePage.exportFromStream(page, baos);
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
