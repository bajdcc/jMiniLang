package com.bajdcc.LALR1.interpret.module;

import com.bajdcc.LALR1.grammar.Grammar;
import com.bajdcc.LALR1.grammar.runtime.IRuntimeDebugInfo;
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import com.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeMap;
import com.bajdcc.util.ResourceLoader;

/**
 * 【模块】类
 *
 * @author bajdcc
 */
public class ModuleClass implements IInterpreterModule {

	private static ModuleClass instance = new ModuleClass();
	private RuntimeCodePage runtimeCodePage;
	private static RuntimeObject globalContext = new RuntimeObject(new RuntimeMap());

	public static ModuleClass getInstance() {
		return instance;
	}

	@Override
	public String getModuleName() {
		return "sys.class";
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

		info.addExternalValue("g_class_context", () -> globalContext);

		return runtimeCodePage = page;
	}
}