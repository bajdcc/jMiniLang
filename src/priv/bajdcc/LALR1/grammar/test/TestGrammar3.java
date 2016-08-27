package priv.bajdcc.LALR1.grammar.test;

import java.util.List;

import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeDebugExec;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeDebugValue;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeStatus;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeException;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeMachine;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeObjectType;
import priv.bajdcc.LALR1.syntax.handler.SyntaxException;
import priv.bajdcc.util.lexer.error.RegexException;

@SuppressWarnings("unused")
public class TestGrammar3 {

	public static void main(String[] args) {
		try {
			String a = "\n"
					+ "var g_a = func ~(x, y) { return x + y;};\n"
					+ "export \"g_a\";";

			String b = "import \"test1\";\n"
					+ "var d = call g_a(1,2);\n"
					+ "var c = g_gk;\n" 
					+ "call g_print(c);\n"
					+ "var t = call g_print(c);";

			/*
			 * BufferedReader br = new BufferedReader(new
			 * FileReader("E:/http.c")); String line = ""; StringBuffer sb = new
			 * StringBuffer(); while ((line = br.readLine()) != null) {
			 * sb.append(line + System.lineSeparator()); }
			 * br.close();
			 */
			Grammar grammar = new Grammar(a);
			System.out.println(grammar.toString());
			RuntimeCodePage page = grammar.getCodePage();
			System.out.println(page.toString());
			RuntimeMachine machine = new RuntimeMachine();
			machine.run("test1", page);

			Grammar grammar2 = new Grammar(b);
			System.out.println(grammar2.toString());
			RuntimeCodePage page2 = grammar2.getCodePage();
			page2.getInfo().addExternalValue("g_gk", () -> new RuntimeObject("abc"));
			page2.getInfo().addExternalFunc("g_print", new IRuntimeDebugExec() {
				@Override
				public String getDoc() {
					return "Print";
				}

				@Override
				public RuntimeObjectType[] getArgsType() {
					return new RuntimeObjectType[] { RuntimeObjectType.kObject };
				}

				@Override
				public RuntimeObject ExternalProcCall(List<RuntimeObject> args, IRuntimeStatus status) {
					System.out.println(args.get(0).getObj());
					return null;
				}
			});
			System.out.println(page2.toString());
			machine.run("test2", page2);

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
			System.err.println(e.getPosition() + ": " + e.getInfo());
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
