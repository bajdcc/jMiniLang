package com.bajdcc.LALR1.interpret.module.user;

import com.bajdcc.LALR1.grammar.Grammar;
import com.bajdcc.LALR1.grammar.runtime.IRuntimeDebugInfo;
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import com.bajdcc.LALR1.interpret.module.IInterpreterModule;
import com.bajdcc.util.ResourceLoader;
import org.apache.log4j.Logger;

/**
 * 【模块】用户态-C语言编译器
 *
 * @author bajdcc
 */
public class ModuleUserCParser implements IInterpreterModule {

	private static ModuleUserCParser instance = new ModuleUserCParser();
	private RuntimeCodePage runtimeCodePage;
	private static Logger logger = Logger.getLogger("cparser");

	public static ModuleUserCParser getInstance() {
		return instance;
	}

	@Override
	public String getModuleName() {
		return "user.cparser";
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