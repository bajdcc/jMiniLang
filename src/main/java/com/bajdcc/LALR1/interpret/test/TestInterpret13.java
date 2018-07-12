package com.bajdcc.LALR1.interpret.test;

import com.bajdcc.LALR1.grammar.Grammar;
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import com.bajdcc.LALR1.grammar.runtime.RuntimeException;
import com.bajdcc.LALR1.interpret.Interpreter;
import com.bajdcc.LALR1.syntax.handler.SyntaxException;
import com.bajdcc.util.lexer.error.RegexException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@SuppressWarnings("unused")
public class TestInterpret13 {

	public static void main(String[] args) {
		try {
			String[] codes = new String[]{
					"import \"sys.base\";\n" +
							"import \"sys.func\";\n" +
							"import \"sys.list\";\n" +
							"import \"sys.string\";\n" +
							"import \"sys.math\";\n" +
							"import \"sys.class\";\n" +
							"\n" +
							"var ctx = call g_create_context();\n" +
							"call g_register_class(ctx, \"shape\", lambda(this){\n" +
							"call g_create_property(this, \"type\", \"shape\");\n" +
							"call g_create_method(this, \"get_area\", lambda(this)->0);\n" +
							"call g_create_method(this, \"get_index\", lambda(this,i)->i);\n" +
							"}, g_null);\n" +
							"call g_register_class(ctx, \"square\", lambda(this){\n" +
							"call g_create_property(this, \"type\", \"square\");\n" +
							"call g_create_property(this, \"a\", 0);\n" +
							"call g_create_property(this, \"b\", 0);\n" +
							"call g_create_method(this, \"get_area\", lambda(this){\n" +
							"return call g_get_property(this, \"a\") * call g_get_property(this, \"b\");\n" +
							"});\n" +
							"call g_create_method(this, \"to_string\", lambda(this){\n" +
							"return \"\" + call g_get_property(this, \"type\")\n" +
							"+ \" a=\" + call g_get_property(this, \"a\")\n" +
							"+ \" b=\" + call g_get_property(this, \"b\")\n" +
							"+ \" area=\"\n" +
							"+ call g_invoke_method(this, \"get_area\");\n" +
							"});\n" +
							"}, \"shape\");\n" +
							"call g_register_class(ctx, \"circle\", lambda(this){\n" +
							"call g_create_property(this, \"type\", \"circle\");\n" +
							"call g_create_property(this, \"r\", 0);\n" +
							"call g_create_method(this, \"get_area\", lambda(this){\n" +
							"var r = call g_get_property(this, \"r\");\n" +
							"return 3.14 * r * r;\n" +
							"});\n" +
							"call g_create_method(this, \"to_string\", lambda(this){\n" +
							"return \"\" + call g_get_property(this, \"type\")\n" +
							"+ \" r=\" + call g_get_property(this, \"r\")\n" +
							"+ \" area=\"\n" +
							"+ call g_invoke_method(this, \"get_area\");\n" +
							"});\n" +
							"}, \"shape\");\n" +
							"\n" +
							"\n" +
							"var square = call g_create_class(ctx, \"square\");\n" +
							"call g_set_property(square, \"a\", 5);\n" +
							"call g_set_property(square, \"b\", 6);\n" +
							"call g_printn(\"\" + call g_get_property(square, \"type\")\n" +
							"+ \" a=\" + call g_get_property(square, \"a\")\n" +
							"+ \" b=\" + call g_get_property(square, \"b\")\n" +
							"+ \" area=\"\n" +
							"+ call g_invoke_method(square, \"get_area\"));\n" +
							"var circle = call g_create_class(ctx, \"circle\");\n" +
							"call g_set_property(circle, \"r\", 10);\n" +
							"call g_set_property(circle, \"s\", square);\n" +
							"call g_printn(\"\" + call g_get_property(circle, \"type\")\n" +
							"+ \" r=\" + call g_get_property(circle, \"r\")\n" +
							"+ \" area=\"\n" +
							"+ call g_invoke_method(circle, \"get_area\"));\n" +
							"call g_printn(call g_invoke_method(square, \"to_string\"));\n" +
							"call g_printn(call g_invoke_method(circle, \"to_string\"));\n" +
							"call g_printn(circle.\"r\");\n" +
							"call g_printn(circle.\"s\".\"__type__\");\n" +
							"call g_printn(circle.\"s\".\"a\");\n" +
							"call g_printn(circle.\"s\".\"b\");\n" +
							"call g_printn(circle.\"__type__\");\n" +
							"call g_printn(square.\"__type__\");\n" +
							"set square::\"a\" = 100;\n" +
							"set square::\"b\" = 120;\n" +
							"call g_printn(circle.\"s\".\"a\");\n" +
							"call g_printn(circle.\"s\".\"b\");\n" +
							"call g_printn(invoke circle::\"get_area\"());\n" +
							"call g_printn(invoke square::\"get_area\"());\n" +
							"call g_printn(invoke circle::\"get_index\"(1));\n" +
							"call g_printn(invoke square::\"get_index\"(2));\n" +
							"",


					"import \"sys.base\";\n" +
					"import \"sys.class\";\n" +
					"import \"std.base\";\n" +
							"var ctx = g_create_context();\n" +
							"g_import_std_base(ctx);\n" +
							"var a = g_create_class(ctx, \"list::array\");\n" +
							"var b = g_create_class(ctx, \"list::array\");\n" +
							"a.\"add\"(b);\n" +
							"b.\"add\"(0);\n" +
							"g_printn(a.\"get\"(0).\"size\"());\n" +
							"a.\"get\"(0).\"c\" := 2;\n" +
							"a.\"get\"(0).\"c\" *= 2;\n" +
							"a.\"get\"(0).\"c\" ++;\n" +
							"g_printn(a.\"get\"(0).\"c\"++);\n",

					"import \"sys.base\";\n" +
							"var move = func ~(i, x, y) {\n" +
							"    g_printn(g_to_string(i) + \": \" + g_to_string(x) + \" -> \" + g_to_string(y));\n" +
							"};\n" +
							"var hanoi = func ~(f) {\n" +
							"    var fk = func ~(i, a, b, c) {\n" +
							"        if (i == 1) {\n" +
							"            move(i, a, c);\n" +
							"        } else {\n" +
							"            f(i - 1, a, c, b);\n" +
							"            move(i, a, c);\n" +
							"            f(i - 1, b, a, c);\n" +
							"        }\n" +
							"    };\n" +
							"    return fk;\n" +
							"};\n" +
							"var h = call (func ~(f) ->\n" +
							"    call (func ~(h) -> h(h))(\n" +
							"        lambda(x) -> lambda(i, a, b, c) ->\n" +
							"            call (f(x(x)))(i, a, b, c)))(hanoi);\n" +
							"h(3, 'A', 'B', 'C');\n",

					"import \"sys.base\";\n" +
							"var c = yield ~(){throw 3;};\n" +
							"var b = yield ~(){\n" +
							"try{foreach(var k : c()){}yield 1;}\n" +
							"catch{\n" +
							"  yield 4;throw 2;}};\n" +
							"/*g_set_debug(true);*/\n" +
							"try{foreach(var k : b()){g_printn(k);}}catch{g_printn(5);}\n",
					"import \"sys.base\";\n" +
							"var a = func ~(){ foreach (var i : g_range(1, 4)+1) {\n" +
							"    foreach (var j : g_range(1, 5)+1) {\n" +
							"        g_printn(i*j);\n" +
							"    }\n" +
							"}};\n" +
							"g_printn(a());\n" +
							"g_printn(a());",
					"import \"sys.base\";\n" +
							"var b = func ~() { foreach (var j : g_range(1, 5)+1) {\n" +
							"    g_printn(j);\n" +
							"}};\n" +
							"var a = func ~() { foreach (var i : g_range(1, 4)+1) {\n" +
							"    b();\n" +
							"}};\n" +
							"g_printn(a());\n" +
							"g_printn(a());",
					"import \"sys.base\";\n" +
							"import \"sys.string\";\n" +
							"var a = func ~(){ foreach (var i : g_range_string(\"ertyrt\")) {\n" +
							"    foreach (var j : g_range(1, 5)+1) {\n" +
							"        g_printn(i);\n" +
							"    }\n" +
							"}};\n" +
							"g_printn(a());\n" +
							"g_printn(a());",
					"import \"sys.base\";\n" +
							"import \"sys.string\";\n" +
							"var a = func ~(){ foreach (var i : g_range_string(\"ertyrt\")) {\n" +
							"    foreach (var j : g_range(1, 5)+1) {\n" +
							"        return i;\n" +
							"    }\n" +
							"}};\n" +
							"g_printn(a());\n" +
							"g_printn(a());",
			};

			System.out.println(codes[codes.length - 1]);
			Interpreter interpreter = new Interpreter();
			Grammar grammar = new Grammar(codes[codes.length - 1]);
			System.out.println(grammar.toString());
			RuntimeCodePage page = grammar.getCodePage();
			//System.out.println(page.toString());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			RuntimeCodePage.exportFromStream(page, baos);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			interpreter.run("test_1", bais);

		} catch (RegexException e) {
			System.err.println();
			System.err.println(e.getPosition() + "," + e.getMessage());
			e.printStackTrace();
		} catch (SyntaxException e) {
			System.err.println();
			System.err.println(String.format("模块名：%s. 位置：%s. 错误：%s-%s(%s:%d)",
					e.getPageName(), e.getPosition(), e.getMessage(),
					e.getInfo(), e.getFileName(), e.getPosition().iLine + 1));
			e.printStackTrace();
		} catch (RuntimeException e) {
			System.err.println();
			System.err.println(e.getPosition() + ": " + e.getInfo());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println();
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
