package com.bajdcc.LALR1.interpret.module;

import com.bajdcc.LALR1.grammar.Grammar;
import com.bajdcc.LALR1.grammar.runtime.IRuntimeDebugInfo;
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import com.bajdcc.util.ResourceLoader;

/**
 * 【模块】LISP
 *
 * @author bajdcc
 */
public class ModuleLisp implements IInterpreterModule {

	private static ModuleLisp instance = new ModuleLisp();
	private RuntimeCodePage runtimeCodePage;

	public static ModuleLisp getInstance() {
		return instance;
	}

	@Override
	public String getModuleName() {
		return "module.lisp";
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