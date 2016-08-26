package priv.bajdcc.LALR1.interpret.test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeDebugExec;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeDebugValue;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeException;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeMachine;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeObjectType;
import priv.bajdcc.LALR1.interpret.Interpreter;
import priv.bajdcc.LALR1.syntax.handler.SyntaxException;
import priv.bajdcc.util.lexer.error.RegexException;

@SuppressWarnings("unused")
public class TestInterpret6 {

	public static void main(String[] args) {
		try {
			String[] codes = new String[] {
							
					"import \"sys.base\";\n"
							+ "var ff = func ~(f) {\n"
							+ "    var fh = func ~(h) {\n"
							+ "        return call h(h);\n"
							+ "    };\n"
							+ "    var fx = func ~(x) {\n"
							+ "        var fn = func ~(n) {\n"
							+ "            var vx = call x(x);\n"
							+ "            var vf = call f(vx);\n"
							+ "            return call vf(n);\n"
							+ "        };\n"
							+ "        return fn;\n"
							+ "    };\n"
							+ "    return call fh(fx);\n"
							+ "};\n"
							+ "var fact = func ~(f) {\n"
							+ "    var fn = func ~(n) -> (n > 0) ? (n * call f(n - 1)) : 1;\n"
							+ "    return fn;\n"
							+ "};\n"
							+ "var ffact = call ff(fact);\n"
							+ "var fact_5 = call ffact(5);\n"
							+ "call g_printn(fact_5);\n"
							+ "\n",
							
					"import \"sys.base\";\n"
							+ "var ff = func ~(f) {\n"
							+ "    var fh = func ~(h) {\n"
							+ "        return call h(h);\n"
							+ "    };\n"
							+ "    var fx = func ~(x) {\n"
							+ "        var fn = func ~(n) {\n"
							+ "            var vx = call x(x);\n"
							+ "            var vf = call f(vx);\n"
							+ "            return call vf(n);\n"
							+ "        };\n"
							+ "        return fn;\n"
							+ "    };\n"
							+ "    return call fh(fx);\n"
							+ "};\n"
							+ "var fact = func ~(f) {\n"
							+ "    var fk = func ~(n) {\n"
							+ "        if (n > 0) {\n"
							+ "            return n * call f(n - 1);\n"
							+ "        } else {\n"
							+ "            return 1;\n"
							+ "        };\n"
							+ "    };\n"
							+ "    return fk;\n"
							+ "};\n"
							+ "var ffact = call ff(fact);\n"
							+ "var fact_5 = call ffact(5);\n"
							+ "call g_printn(fact_5);\n"
							+ "\n",
							
							};

			Interpreter interpreter = new Interpreter();
			Grammar grammar = new Grammar(codes[codes.length - 1]);
			System.out.println(codes[codes.length - 1]);
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
