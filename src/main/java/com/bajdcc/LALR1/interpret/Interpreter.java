package com.bajdcc.LALR1.interpret;

import com.bajdcc.LALR1.grammar.runtime.RuntimeProcess;
import com.bajdcc.LALR1.interpret.os.IOSCodePage;

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
	private Map<String, String> arrFiles = new HashMap<>();

	public void run(String name, InputStream input) throws Exception {
		rtProcess = new RuntimeProcess(name, input, arrFiles);
		for (Map.Entry<String, String> entry : arrCodes.entrySet()) {
			rtProcess.addCodePage(entry.getKey(), entry.getValue());
		}
		arrCodes.clear();
		rtProcess.run();
	}

	/**
	 * 添加代码页
	 *
	 * @param codePage 代码页
	 */
	public void load(IOSCodePage codePage) {
		arrCodes.put(codePage.getName(), codePage.getCode());
		arrFiles.put(codePage.getName(), codePage.getClass().getSimpleName());
	}

	/**
	 * 发送停机命令
	 */
	public void stop() {
		rtProcess.halt();
	}
}
