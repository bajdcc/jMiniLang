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
public class TestInterpret7 {

	public static void main(String[] args) {
		try {
			String[] codes = new String[] {

					"import \"sys.base\";\n"
							+ "var f = func ~(a) -> a;\n"
							+ "call g_printn(call g_get_type(f));"
							+ "\n",

					"import \"sys.base\";\n" +
							"var move = func ~(i, x, y) {\n" +
							"    call g_printn(\"\" + i + \": \" + x + \" -> \" + y);\n" +
							"};\n" +
							"var hanoi = func ~(f) {\n" +
							"    var fk = func ~(i, a, b, c) {\n" +
							"        if (i == 1) {\n" +
							"            call move(i, a, c);\n" +
							"        } else {\n" +
							"            call f(i - 1, a, c, b);\n" +
							"            call move(i, a, c);\n" +
							"            call f(i - 1, b, a, c);\n" +
							"        }\n" +
							"    };\n" +
							"    return fk;\n" +
							"};\n" +
							"var h = call (func ~(f) {\n" +
							"    var fx = func ~(x) {\n" +
							"        var fn = func ~(i, a, b, c) {\n" +
							"            var vf = call f(call x(x));\n" +
							"            return call vf(i, a, b, c);\n" +
							"        };\n" +
							"        return fn;\n" +
							"    };\n" +
							"    return call (func ~(h) -> call h(h))(fx);\n" +
							"})(hanoi);\n" +
							"call h(3, 'A', 'B', 'C');\n" +
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
