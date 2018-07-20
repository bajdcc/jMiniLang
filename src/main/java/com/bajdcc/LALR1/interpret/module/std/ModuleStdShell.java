package com.bajdcc.LALR1.interpret.module.std;

import com.bajdcc.LALR1.grammar.Grammar;
import com.bajdcc.LALR1.grammar.runtime.IRuntimeDebugInfo;
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import com.bajdcc.LALR1.interpret.module.IInterpreterModule;
import com.bajdcc.util.ResourceLoader;

/**
 * 【模块】命令行
 *
 * @author bajdcc
 */
public class ModuleStdShell implements IInterpreterModule {

	private static ModuleStdShell instance = new ModuleStdShell();
	private RuntimeCodePage runtimeCodePage;

	public static ModuleStdShell getInstance() {
		return instance;
	}

	@Override
	public String getModuleName() {
		return "std.shell";
	}

	@Override
	public String getModuleCode() {
		return ResourceLoader.INSTANCE.load(getClass());
	}

	@Override
	public RuntimeCodePage getCodePage() throws Exception {
		if (runtimeCodePage != null)
			return runtimeCodePage;

		String base = ResourceLoader.INSTANCE.load(getClass());

		Grammar grammar = new Grammar(base);
		RuntimeCodePage page = grammar.getCodePage();
		IRuntimeDebugInfo info = page.getInfo();

		return runtimeCodePage = page;
	}
}