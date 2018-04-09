package priv.bajdcc.LALR1.grammar.test;

import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeException;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeMachine;
import priv.bajdcc.LALR1.syntax.handler.SyntaxException;
import priv.bajdcc.util.lexer.error.RegexException;

@SuppressWarnings("unused")
public class TestGrammar2 {

	public static void main(String[] args) {
		try {
			String[] exprs = new String[]{
					"var a = 1;",
					"var a = 4 + 4;",
					"var a = !(!true * (++4 / !4 + 4 ? 5 & 6 && true ^ !\"ddd\" | 'r' : 8.0+9*9));",
					"var a = 3.0 + 4.0 / 7 * 8.9;",
					"var a = 3000.0 << ++2.0;",
					"var a = true << 9 ? 1 + 3 : 2;",
					"var a = 5;let a = a + 4 + a ? 3 : 5;var t = func main1(){};",
					"var d= func [\"d\",\"t\"] ~()->a;",
					"var d = func ~()->4;",
					"var a = call (func ~(a) {call (func b(c){var b = '5'+ 66; call b(666);})();}) (6);",
					"var a = call (func ~(a) {\n"
							+ "call (func b(c){\n"
							+ "var c = '5'+ 1;\n"
							+ "var d = c;\n"
							+ "//call b(666);\n"
							+ "let c = 7;\n" + "})(7);\n"
							+ "}) (44);",
					"var a = call\n"
							+ "(func ~(x, y) -> x + y)\n"
							+ " (1, 2);\n",
					"var d = 4;"
							+ "var c = d || 6;",
			};
			/*
			 * BufferedReader br = new BufferedReader(new
			 * FileReader("E:/http.c")); String line = ""; StringBuffer sb = new
			 * StringBuffer(); while ((line = br.readLine()) != null) {
			 * sb.append(line + System.lineSeparator()); }
			 * br.close();
			 */
			Grammar grammar = new Grammar(exprs[exprs.length - 1]);
			System.out.println(grammar.toString());
			RuntimeCodePage page = grammar.getCodePage();
			System.out.println(page.toString());
			RuntimeMachine machine = new RuntimeMachine();
			machine.run("test1", page);
			// FileWriter fw = new FileWriter("E:/testgrammar.txt");
			// fw.append(grammar.toString());
			// fw.close();
		} catch (RegexException e) {
			System.err.println(e.getPosition() + "," + e.getMessage());
			e.printStackTrace();
		} catch (SyntaxException e) {
			System.err.println(e.getPosition() + "," + e.getMessage() + " "
					+ e.getInfo());
			e.printStackTrace();
			// } catch (IOException e) {
			// System.err.println(e.getMessage());
			// e.printStackTrace();
		} catch (RuntimeException e) {
			System.err.println(e.getPosition() + ": "
					+ e.getInfo());
			e.printStackTrace();
			// } catch (IOException e) {
			// System.err.println(e.getMessage());
			// e.printStackTrace();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			// } catch (IOException e) {
			// System.err.println(e.getMessage());
			// e.printStackTrace();
		}
	}
}
