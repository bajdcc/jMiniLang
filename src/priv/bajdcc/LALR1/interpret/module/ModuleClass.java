package priv.bajdcc.LALR1.interpret.module;

import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeDebugInfo;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import priv.bajdcc.util.ResourceLoader;

/**
 * 【模块】类
 *
 * @author bajdcc
 */
public class ModuleClass implements IInterpreterModule {

    private static ModuleClass instance = new ModuleClass();
    private RuntimeCodePage runtimeCodePage;

    public static ModuleClass getInstance() {
        return instance;
    }

    @Override
    public String getModuleName() {
        return "sys.class";
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