package priv.bajdcc.LALR1.interpret.test;

import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeException;
import priv.bajdcc.LALR1.interpret.Interpreter;
import priv.bajdcc.LALR1.syntax.handler.SyntaxException;
import priv.bajdcc.util.lexer.error.RegexException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@SuppressWarnings("unused")
public class TestInterpret8 {

	public static void main(String[] args) {
		try {
			String[] codes = new String[] {

					"import \"sys.base\"; import \"sys.list\";\n" +
							"var a = [];\n" +
							"call g_array_add(a, 5);\n" +
							"call g_array_set(a, 0, 4);\n" +
							"call g_printn(call g_array_get(a, 0));\n" +
							"call g_array_remove(a, 0);\n" +
							"call g_array_add(a, 50);\n" +
							"call g_array_add(a, 100);\n" +
							"call g_array_set(a, 1, 400);\n" +
							"call g_printn(call g_array_get(a, 1));\n" +
							"call g_array_pop(a);\n" +
							"call g_array_pop(a);\n" +
							"call g_printn(call g_array_size(a));\n" +
							"\n" +
							"let a = {};\n" +
							"call g_map_put(a, \"x\", 5);\n" +
							"call g_map_put(a, \"y\", 10);\n" +
							"call g_map_put(a, \"x\", 50);\n" +
							"call g_printn(call g_map_size(a));\n" +
							"call g_printn(call g_map_get(a, \"x\"));\n" +
							"call g_printn(call g_map_get(a, \"y\"));\n" +
							"call g_printn(call g_map_contains(a, \"x\"));\n" +
							"call g_map_remove(a, \"x\");\n" +
							"call g_printn(call g_map_contains(a, \"x\"));\n" +
							"call g_printn(call g_map_size(a));\n" +
							"\n",

					"import \"sys.base\"; import \"sys.list\";\n" +
							"var create_node = func ~(data) {\n" +
							"    var new_node = g_new_map;\n" +
							"    call g_map_put(new_node, \"data\", data);\n" +
							"    call g_map_put(new_node, \"prev\", g_null);\n" +
							"    call g_map_put(new_node, \"next\", g_null);\n" +
							"    return new_node;\n" +
							"};\n" +
							"var append = func ~(head, obj) {\n" +
							"    var new_node = call create_node(obj);\n" +
							"    call g_map_put(new_node, \"next\", head);\n" +
							"    call g_map_put(head, \"prev\", new_node);\n" +
							"    return new_node;" +
							"};\n" +
							"var head = call create_node(0);\n" +
							"foreach (var i : call g_range(1, 10)) {\n" +
							"    let head = call append(head, i);\n" +
							"}\n" +
							"var p = head;\n" +
							"while (!call g_is_null(p)) {\n" +
							"    call g_printn(call g_map_get(p, \"data\"));\n" +
							"    let p = call g_map_get(p, \"next\");\n" +
							"}\n" +
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
