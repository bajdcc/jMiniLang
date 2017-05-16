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
				"var g_func_and = func ~(a, b) -> a && b;\n" +
				"var g_func_or = func ~(a, b) -> a || b;\n" +
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
				"export \"g_func_and\";\n" +
				"export \"g_func_or\";\n" +
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
				"var g_func_always_1 = func ~(a) -> 1;\n" +
				"export \"g_func_always_1\";\n" +
				"var g_func_always_true = func ~(a) -> true;\n" +
				"export \"g_func_always_true\";\n" +
				"\n" +
				"var g_func_xsl = func [\"数组遍历闭包-foldl\"] ~(l) {\n" +
				"    var len = call g_array_size(l);\n" +
				"    var idx = 0;\n" +
				"    var _xsl = func ~() {\n" +
				"        if (idx == len) { return g__; }\n" +
				"        var d = call g_array_get(l, idx);\n" +
				"        idx++;\n" +
				"        var _xsl_ = func ~() -> d;\n" +
				"        return _xsl_;\n" +
				"    };\n" +
				"    return _xsl;\n" +
				"};\n" +
				"export \"g_func_xsl\";\n" +
				"var g_func_xsr = func [\"数组遍历闭包-foldr\"] ~(l) {\n" +
				"    var idx = call g_array_size(l) - 1;\n" +
				"    var _xsr = func ~() {\n" +
				"        if (idx < 0) { return g__; }\n" +
				"        var d = call g_array_get(l, idx);\n" +
				"        idx--;\n" +
				"        var _xsr_ = func ~() -> d;\n" +
				"        return _xsr_;\n" +
				"    };\n" +
				"    return _xsr;\n" +
				"};\n" +
				"export \"g_func_xsr\";\n" +
				"// ----------------------------------------------\n" +
				"var g_func_fold = func \n" +
				"    [\n" +
				"        \"函数名：g_func_fold\",\n" +
				"        \"参数解释：\",\n" +
				"        \"  - name: 套用的折叠函数\",\n" +
				"        \"  - list: 需处理的数组\",\n" +
				"        \"  - init: 初始值(不用则为空)\",\n" +
				"        \"  - xs: 数组遍历方式(xsl=从左到右,xsr=从右到左)\",\n" +
				"        \"  - map: 对遍历的每个元素施加的变换\",\n" +
				"        \"  - arg: 对二元操作进行包装(默认=g_func_1,例=g_func_swap)\",\n" +
				"        \"  - filter: 对map后的元素进行过滤(true则处理)\"\n" +
				"    ]\n" +
				"    ~(name, list, init, xs, map, arg, filter) {\n" +
				"    var len = call g_array_size(list);\n" +
				"    if (len == 0) { return g__; }// 肯定返回空\n" +
				"    var val = g__;\n" +
				"    var x = g__;\n" +
				"    if (call g_is_null(init)) { // 没初值的话，取第一个元素为初值\n" +
				"        if (len == 1) { return call g_array_get(list, 0); }\n" +
				"        let x = call xs(list);// 创建遍历闭包\n" +
				"        let val = call x();// 取第一个元素\n" +
				"        let val = call val();\n" +
				"        let val = call map(val);// 对元素进行变换\n" +
				"    } else {\n" +
				"        let x = call xs(list);\n" +
				"        let val = init;\n" +
				"    }\n" +
				"    var n = name;// 对数组进行变换\n" +
				"    let n = call arg(n);// 对卷积方式进行变换\n" +
				"    for (;;) {// 遍历数组\n" +
				"        var v2 = call x();// 取得下一元素\n" +
				"        if (call g_is_null(v2)) { break; }// 没有下一元素，中止\n" +
				"        let v2 = call v2();// 下一元素\n" +
				"        let v2 = call map(v2);// 对下一元素进行变换\n" +
				"        if (call filter(v2)) {// 过滤控制\n" +
				"            let val = call n(val, v2);// 将两元素进行处理\n" +
				"        }\n" +
				"    }\n" +
				"    return val;\n" +
				"};\n" +
				"export \"g_func_fold\";\n" +
				"// ----------------------------------------------\n" +
				"var g_func_apply = func ~(name, list) ->\n" +
				"    call g_func_apply_arg(name, list, \"g_func_1\");\n" +
				"export \"g_func_apply\";\n" +
				"var g_func_apply_arg = func ~(name, list, arg) ->\n" +
				"    call g_func_fold(name, list, g__, \"g_func_xsl\", \"g_func_1\", arg, \"g_func_always_true\");\n" +
				"export \"g_func_apply_arg\";\n" +
				"var g_func_applyr = func ~(name, list) ->\n" +
				"    call g_func_applyr_arg(name, list, \"g_func_1\");\n" +
				"export \"g_func_applyr\";\n" +
				"var g_func_applyr_arg = func ~(name, list, arg) ->\n" +
				"    call g_func_fold(name, list, g__, \"g_func_xsr\", \"g_func_1\", arg, \"g_func_always_true\");\n" +
				"export \"g_func_applyr_arg\";\n" +
				"// ----------------------------------------------\n" +
				"var g_func_map = func ~(list, arg) ->\n" +
				"    call g_func_fold(\"g_array_add\", list, g_new_array, \"g_func_xsl\", arg, \"g_func_1\", \"g_func_always_true\");\n" +
				"export \"g_func_map\";\n" +
				"var g_func_mapr = func ~(list, arg) ->\n" +
				"    call g_func_fold(\"g_array_add\", list, g_new_array, \"g_func_xsr\", arg, \"g_func_1\", \"g_func_always_true\");\n" +
				"export \"g_func_mapr\";\n" +
				"var g_func_length = func ~(list) ->\n" +
				"    call g_func_fold(\"g_func_add\", list, 0, \"g_func_xsl\", \"g_func_always_1\", \"g_func_1\", \"g_func_always_true\");\n" +
				"export \"g_func_length\";\n" +
				"var g_func_filter = func ~(list, filter) ->\n" +
				"    call g_func_fold(\"g_array_add\", list, g_new_array, \"g_func_xsl\", \"g_func_1\", \"g_func_1\", filter);\n" +
				"export \"g_func_filter\";\n" +
				"// ----------------------------------------------\n" +
				"var take_filter = func ~(n) {//取数组前N个元素\n" +
				"    var idx = 0;\n" +
				"    var end = n;\n" +
				"    var _take_filter = func ~(a) -> idx++ <= end;\n" +
				"    return _take_filter;\n" +
				"};\n" +
				"var drop_filter = func ~(n) {//取数组后len-N个元素\n" +
				"    var idx = 0;\n" +
				"    var end = n;\n" +
				"    var _drop_filter = func ~(a) -> idx++ > end;\n" +
				"    return _drop_filter;\n" +
				"};\n" +
				"var g_func_take = func ~(list, n) ->\n" +
				"    call g_func_fold(\"g_array_add\", list, g_new_array, \"g_func_xsl\", \"g_func_1\", \"g_func_1\", call take_filter(n));\n" +
				"export \"g_func_take\";\n" +
				"var g_func_taker = func ~(list, n) ->\n" +
				"    call g_func_fold(\"g_array_add\", list, g_new_array, \"g_func_xsr\", \"g_func_1\", \"g_func_1\", call take_filter(n));\n" +
				"export \"g_func_taker\";\n" +
				"var g_func_drop = func ~(list, n) ->\n" +
				"    call g_func_fold(\"g_array_add\", list, g_new_array, \"g_func_xsl\", \"g_func_1\", \"g_func_1\", call drop_filter(n));\n" +
				"export \"g_func_drop\";\n" +
				"var g_func_dropr = func ~(list, n) ->\n" +
				"    call g_func_fold(\"g_array_add\", list, g_new_array, \"g_func_xsr\", \"g_func_1\", \"g_func_1\", call drop_filter(n));\n" +
				"export \"g_func_dropr\";\n" +
				"// ----------------------------------------------\n" +
				"var func_zip = func ~(name, a, b, xs) {//将两数组进行合并\n" +
				"    var val = [];\n" +
				"    var xa = call xs(a);\n" +
				"    var xb = call xs(b);\n" +
				"    for (;;) {\n" +
				"        var _a = call xa();\n" +
				"        var _b = call xb();\n" +
				"        if (call g_is_null(_a) || call g_is_null(_b)) {\n" +
				"            break;\n" +
				"        }\n" +
				"        var c = call name(call _a(), call _b());\n" +
				"        call g_array_add(val, c);\n" +
				"    }\n" +
				"    return val;\n" +
				"};\n" +
				"var g_func_zip = func ~(name, a, b) ->\n" +
				"    call func_zip(name, a, b, \"g_func_xsl\");\n" +
				"export \"g_func_zip\";\n" +
				"var g_func_zipr = func ~(name, a, b) ->\n" +
				"    call func_zip(name, a, b, \"g_func_xsr\");\n" +
				"export \"g_func_zipr\";\n" +
				"// ----------------------------------------------\n" +
				"var g_func_applicative = func ~(f, a, b) -> call f(a, call b(a));\n" +
				"export \"g_func_applicative\";\n" +
				"var g_func_import_string_module = func ~() { import \"sys.string\"; };\n" +
				"export \"g_func_import_string_module\";\n" +
				"\n";

		Grammar grammar = new Grammar(base);
		RuntimeCodePage page = grammar.getCodePage();
		IRuntimeDebugInfo info = page.getInfo();
		info.addExternalValue("g__", () -> new RuntimeObject(null));

		return page;
	}
}