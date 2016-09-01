package priv.bajdcc.LALR1.interpret;

import priv.bajdcc.LALR1.grammar.runtime.RuntimeMachine;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeProcess;
import priv.bajdcc.LALR1.interpret.module.IInterpreterModule;
import priv.bajdcc.LALR1.interpret.module.ModuleBase;
import priv.bajdcc.LALR1.interpret.module.ModuleList;
import priv.bajdcc.LALR1.interpret.module.ModuleMath;

import java.io.InputStream;

/**
 * 【运行时】扩展/内建虚拟机
 *
 * @author bajdcc
 */
public class Interpreter {

	private RuntimeProcess rtProcess;

	public void run(String name, InputStream input) throws Exception {
		rtProcess = new RuntimeProcess(name, input);
		rtProcess.run();
	}
}
