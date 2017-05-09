package priv.bajdcc.LALR1.interpret.module;

import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.IRuntimeDebugInfo;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeObject;

/**
 * 【模块】函数
 *
 * @author bajdcc
 */
public class ModuleFunction implements IInterpreterModule {

	private static ModuleFunction instance = new ModuleFunction();

	public static ModuleFunction getInstance() {
		return instance;
	}

	@Override
	public String getModuleName() {
		return "sys.func";
	}

	@Override
	public RuntimeCodePage getCodePage() throws Exception {
		String base = "import \"sys.base\";\n" +
				"import \"sys.list\";\n" +
				"var g_func_max = func ~(a, b) -> a > b ? a : b;\n" +
				"var g_func_min = func ~(a, b) -> a < b ? a : b;\n" +
				"var g_func_lt = func ~(a, b) -> a < b;\n" +
				"var g_func_lte = func ~(a, b) -> a <= b;\n" +
				"var g_func_gt = func ~(a, b) -> a > b;\n" +
				"var g_func_gte = func ~(a, b) -> a >= b;\n" +
				"var g_func_eq = func ~(a, b) -> a == b;\n" +
				"var g_func_neq = func ~(a, b) -> a != b;\n" +
				"var g_func_add = func ~(a, b) -> a + b;\n" +
				"var g_func_sub = func ~(a, b) -> a - b;\n" +
				"var g_func_mul = func ~(a, b) -> a * b;\n" +
				"var g_func_div = func ~(a, b) -> a / b;\n" +
				"export \"g_func_max\";\n" +
				"export \"g_func_min\";\n" +
				"export \"g_func_lt\";\n" +
				"export \"g_func_lte\";\n" +
				"export \"g_func_gt\";\n" +
				"export \"g_func_gte\";\n" +
				"export \"g_func_eq\";\n" +
				"export \"g_func_neq\";\n" +
				"export \"g_func_add\";\n" +
				"export \"g_func_sub\";\n" +
				"export \"g_func_mul\";\n" +
				"export \"g_func_div\";\n" +
				"var g_func_curry = func ~(a, b) {\n" +
				"    var _curry = func ~(c) -> call a(b, c);\n" +
				"    return _curry;\n" +
				"};\n" +
				"var g_func_swap = func ~(a) {\n" +
				"    var _swap = func ~(b, c) -> call a(c, b);\n" +
				"    return _swap;\n" +
				"};\n" +
				"export \"g_func_curry\";\n" +
				"export \"g_func_swap\";\n" +
				"var g_func_1 = func ~(a) -> a;\n" +
				"export \"g_func_1\";\n" +
				"\n" +
				"var xs = func [\"数组遍历闭包\"] ~(l) {\n" +
				"    var len = call g_array_size(l);\n" +
				"    var idx = 0;\n" +
				"    var _xs = func ~() {\n" +
				"        if (idx == len) { return g__; }\n" +
				"        var d = call g_array_get(l, idx);\n" +
				"        idx++;\n" +
				"        var _xs_ = func ~() -> d;\n" +
				"        return _xs_;\n" +
				"    };\n" +
				"    return _xs;\n" +
				"};\n" +
				"\n" +
				"var g_func_apply = func ~(name, list) {\n" +
				"    return call g_func_apply_arg(name, list, \"g_func_1\");\n" +
				"};\n" +
				"export \"g_func_apply\";\n" +
				"var g_func_apply_arg = func ~(name, list, arg) {\n" +
				"    var len = call g_array_size(list);\n" +
				"    if (len == 0) { return g__; }\n" +
				"    if (len == 1) { return call g_array_get(list, 0); }\n" +
				"    var x = call xs(list);\n" +
				"    var val = call x();\n" +
				"    let val = call val();\n" +
				"    var n = name;\n" +
				"    let n = call arg(n);\n" +
				"    for (;;) {\n" +
				"        var v2 = call x();\n" +
				"        if (call g_is_null(v2)) { break; }\n" +
				"        let v2 = call v2();\n" +
				"        let val = call n(val, v2);\n" +
				"    }\n" +
				"    return val;\n" +
				"};\n" +
				"export \"g_func_apply_arg\";\n" +
				"var g_func_applicative = func ~(f, a, b) -> call f(a, call b(a));\n" +
				"export \"g_func_applicative\";\n" +
				"\n";

		Grammar grammar = new Grammar(base);
		RuntimeCodePage page = grammar.getCodePage();
		IRuntimeDebugInfo info = page.getInfo();
		info.addExternalValue("g__", () -> new RuntimeObject(null));

		return page;
	}
}