package com.bajdcc.LALR1.interpret.module.user;

import com.bajdcc.LALR1.grammar.Grammar;
import com.bajdcc.LALR1.grammar.runtime.IRuntimeDebugInfo;
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import com.bajdcc.LALR1.interpret.module.IInterpreterModule;
import com.bajdcc.util.ResourceLoader;

/**
 * 【模块】用户态-Lisp
 *
 * @author bajdcc
 */
public class ModuleUserLisp implements IInterpreterModule {

	private static ModuleUserLisp instance = new ModuleUserLisp();
	private RuntimeCodePage runtimeCodePage;

	public static ModuleUserLisp getInstance() {
		return instance;
	}

	@Override
	public String getModuleName() {
		return "user.lisp";
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