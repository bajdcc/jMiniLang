package priv.bajdcc.LALR1.interpret.module.std;

import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeDebugInfo;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import priv.bajdcc.LALR1.interpret.module.IInterpreterModule;
import priv.bajdcc.util.ResourceLoader;

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