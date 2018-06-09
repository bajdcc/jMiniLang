package com.bajdcc.LALR1.interpret.module;

import org.apache.log4j.Logger;
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
	private static Logger logger = Logger.getLogger("lisp");
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
		return ResourceLoader.load(getClass());
	}

	@Override
	public RuntimeCodePage getCodePage() throws Exception {
		if (runtimeCodePage != null)
			return runtimeCodePage;

		String base = ResourceLoader.load(getClass());

		Grammar grammar = new Grammar(base);
		RuntimeCodePage page = grammar.getCodePage();
		IRuntimeDebugInfo info = page.getInfo();

		return runtimeCodePage = page;
	}
}