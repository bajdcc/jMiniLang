package priv.bajdcc.LALR1.interpret;

import priv.bajdcc.LALR1.grammar.runtime.RuntimeMachine;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeProcess;
import priv.bajdcc.LALR1.interpret.module.IInterpreterModule;
import priv.bajdcc.LALR1.interpret.module.ModuleBase;
import priv.bajdcc.LALR1.interpret.module.ModuleList;
import priv.bajdcc.LALR1.interpret.module.ModuleMath;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 【运行时】扩展/内建虚拟机
 *
 * @author bajdcc
 */
public class Interpreter {

	private RuntimeProcess rtProcess;
	private Map<String, String> arrCodes = new HashMap<>();

	public void run(String name, InputStream input) throws Exception {
		rtProcess = new RuntimeProcess(name, input);
		for (Map.Entry<String, String> entry : arrCodes.entrySet()) {
			rtProcess.addCodePage(entry.getKey(), entry.getValue());
		}
		arrCodes.clear();
		rtProcess.run();
	}

	/**
	 * 添加代码页
	 * @param name 页名
	 * @param code 代码
	 */
	public void load(String name, String code) {
		arrCodes.put(name, code);
	}
}
