package priv.bajdcc.LALR1.interpret.os;

import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeException;
import priv.bajdcc.LALR1.interpret.Interpreter;
import priv.bajdcc.LALR1.interpret.os.kern.OSEntry;
import priv.bajdcc.LALR1.interpret.os.kern.OSIrq;
import priv.bajdcc.LALR1.interpret.os.proc.OSSchd;
import priv.bajdcc.LALR1.interpret.os.user.UserMain;
import priv.bajdcc.LALR1.interpret.os.user.routine.URDup;
import priv.bajdcc.LALR1.interpret.os.user.routine.UREcho;
import priv.bajdcc.LALR1.interpret.os.user.routine.URPipe;
import priv.bajdcc.LALR1.interpret.os.user.routine.URShell;
import priv.bajdcc.LALR1.syntax.handler.SyntaxException;
import priv.bajdcc.util.lexer.error.RegexException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 【操作系统】初始化
 *
 * @author bajdcc
 */
public class OSMain {
	public static void main(String[] args) {

		IOSCodePage pages[] = new IOSCodePage[] {
				// OS
				new OSEntry(),
				new OSIrq(),
				new OSSchd(),
				// USER
				new UserMain(),
				// USER ROUTINE
				new URShell(),
				new UREcho(),
				new URPipe(),
				new URDup(),
		};

		try {
			String code =	"import \"sys.base\";\n" +
							"import \"sys.proc\";\n" +
							"call g_load_sync_x(\"/kern/entry\");\n";

			Interpreter interpreter = new Interpreter();

			for (IOSCodePage page : pages) {
				interpreter.load(page.getName(), page.getCode());
			}

			Grammar grammar = new Grammar(code);
			//System.out.println(grammar.toString());
			RuntimeCodePage page = grammar.getCodePage();
			//System.out.println(page.toString());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			RuntimeCodePage.exportFromStream(page, baos);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			interpreter.run("@main", bais);

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
