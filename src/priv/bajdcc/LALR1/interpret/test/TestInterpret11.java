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
public class TestInterpret11 {

	public static void main(String[] args) {
		try {
			String[] codes = new String[]{
					"import \"sys.base\";\n" +
							"import \"sys.func\";\n" +
							"import \"sys.list\";\n" +
							"import \"sys.string\";\n" +
							"\n" +
							"var a = call g_array_range(1, 10);\n" +
							"var b = call g_func_apply(\"g_func_add\", a);\n" +
							"call g_printn(b);\n" +
							"var b1 = call g_func_length(a);\n" +
							"call g_printn(b1);\n" +
							"var c = call g_func_apply(\"g_func_add\", a);\n" +
							"call g_printn(c);\n" +
							"var c1 = call g_func_map(a, \"g_to_string\");\n" +
							"var c2 = call g_func_applyr(\"g_func_add\", c1);\n" +
							"call g_printn(c2);\n" +
							"let c1 = call g_func_mapr(a, \"g_to_string\");\n" +
							"let c2 = call g_func_apply(\"g_func_add\", c1);\n" +
							"call g_printn(c2);\n" +
							"var c3 = call g_func_applyr(\"g_func_sub\", a);\n" +
							"call g_printn(c3);\n" +
							"var f4 = func ~(x) -> x % 2 == 0;\n" +
							"var c4 = call g_func_filter(a, f4);\n" +
							"let c4 = call g_func_map(c4, \"g_to_string\");\n" +
							"let c4 = call g_func_apply(\"g_func_add\", c4);\n" +
							"call g_printn(c4);\n" +
							"var c5 = call g_func_take(c1, 5);\n" +
							"let c5 = call g_func_apply(\"g_func_add\", c5);\n" +
							"call g_printn(c5);\n" +
							"let c5 = call g_func_taker(c1, 5);\n" +
							"let c5 = call g_func_apply(\"g_func_add\", c5);\n" +
							"call g_printn(c5);\n" +
							"let c5 = call g_func_drop(c1, 5);\n" +
							"let c5 = call g_func_apply(\"g_func_add\", c5);\n" +
							"call g_printn(c5);\n" +
							"let c5 = call g_func_dropr(c1, 5);\n" +
							"let c5 = call g_func_apply(\"g_func_add\", c5);\n" +
							"call g_printn(c5);\n" +
							"var d = call g_func_apply_arg(\"g_func_add\", call g_string_split(\"12345\", \"\"), \"g_func_swap\");\n" +
							"call g_printn(d);\n" +
							"call g_func_import_string_module();\n" +
							"var e = call g_func_applicative(\"g_func_eq\", \"12321\", \"g_string_reverse\");\n" +
							"call g_printn(e);\n" +
							"call g_printn(call g_doc(\"g_func_fold\"));\n" +
							"var xx = func ~(l) {\n" +
							"    var idx = call g_array_size(l) - 1;\n" +
							"    var _xsr = func ~() ->\n" +
							"        idx < 0 ? g_null : call g_array_get(l, idx--);\n" +
							"    return _xsr;\n" +
							"};\n" +
							"var x1 = call xx(a);\n" +
							"var x2 = call x1();\n" +
							"while (!call g_is_null(x2)) {\n" +
							"    call g_printn(x2);\n" +
							"    let x2 = call x1();\n" +
							"}\n" +
							""
			};

			Interpreter interpreter = new Interpreter();
			Grammar grammar = new Grammar(codes[codes.length - 1]);
			System.out.println(grammar.toString());
			RuntimeCodePage page = grammar.getCodePage();
			System.out.println(page.toString());
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
