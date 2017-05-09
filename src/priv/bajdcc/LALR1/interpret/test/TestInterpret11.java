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
							"var c = call g_func_apply(\"g_func_add\", a);\n" +
							"call g_printn(c);\n" +
							"var d = call g_func_apply_arg(\"g_func_add\", call g_string_split(\"12345\", \"\"), \"g_func_swap\");\n" +
							"call g_printn(d);\n" +
							"var reverse = func ~(str) -> call g_string_reverse(str);\n" +
							"var e = call g_func_applicative(\"g_func_eq\", \"12321\", reverse);\n" +
							"call g_printn(e);\n" +
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
