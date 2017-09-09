package priv.bajdcc.LALR1.interpret.test;

import priv.bajdcc.LALR1.grammar.Grammar;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import priv.bajdcc.LALR1.grammar.runtime.RuntimeException;
import priv.bajdcc.LALR1.interpret.Interpreter;
import priv.bajdcc.LALR1.syntax.handler.SyntaxException;
import priv.bajdcc.util.lexer.error.RegexException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@SuppressWarnings("unused")
public class TestInterpret12 {

    public static void main(String[] args) {
        try {
            String[] codes = new String[]{
                    "import \"sys.base\";\n" +
                            "import \"sys.func\";\n" +
                            "import \"sys.list\";\n" +
                            "import \"sys.string\";\n" +
                            "import \"module.lisp\";\n" +
                            "\n" +
                            "var env = call g_lisp_env();\n" +
                            "call g_lisp_repl(env, \"(define circle-area (lambda (r) (* PI (* r r))))\");\n" +
                            "call g_print(call g_lisp_repl(env, \"(circle-area 10)\"));\n" +
                            "call g_lisp_repl(env, \"(define fact (lambda (n) (if (<= n 1) 1 (* n (fact (- n 1))))))\");\n" +
                            "call g_print(call g_lisp_repl(env, \"(fact 10)\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(list 1 2 3 4 5)\"));\n" +
                            "call g_print(\"-----\");\n" +
                            "call g_print(call g_lisp_repl(env, \"(define L (list 1 2 3 4 5))\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(car L)\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(cdr L)\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(count 0 (list 0 1 2 3 0 0))\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(count (quote the) (quote (the more the merrier the bigger the better)))\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(null? (list))\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(number? 5.0)\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(number? (list))\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(type (quote hello))\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(list? (list))\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(car (quote (a b c)))\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(type (car (quote (a b c))))\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(type (car (quote (a b c))))\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(car (cons (quote a) (quote (b c))))\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(cdr (cons (quote a) (quote (b c))))\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(define repeat (lambda (f) (lambda (x) (f (f x)))))\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(define twice (lambda (x) (* 2 x)))\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"((repeat twice) 10)\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(define sum (lambda (n) (if (< n 2) 1 (+ n (sum (- n 1))))))\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(sum 10)\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(define min2 (lambda (a b) (if (< a b) a b)))\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(min 50 60)\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(range 0 10)\"));\n" +
                            "call g_print(\"-----\");\n" +
                            "call g_print(call g_lisp_repl(env, \"(define fib (lambda (n) (if (<= n 2) 1 (+ (fib (- n 1)) (fib (- n 2))))))\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(fib 10)\"));\n" +
                            "call g_print(\"-----\");\n" +
                            "call g_print(call g_lisp_repl(env, \"(map fib (list 3 2 3 4 5))\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(map fib (range 0 10))\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(map (lambda (n) ((repeat twice) n)) (range 1 10))\"));\n" +
                            "call g_print(\"-----\");\n" +
                            "call g_lisp_repl(env, \"(print \\\"ab_cd\\\")\");\n" +
                            "call g_print(call g_lisp_repl(env, \"(car (cons 'a '(b c)))\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(cdr (cons 'a '(b c)))\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(apply + (range 1 10))\"));\n" +
                            "call g_print(call g_lisp_repl(env, \"(apply + (list \\\"hello\\\" #s \\\"world\\\" #s \\\"bajdcc\\\"))\"));\n" +
                            ""
            };

            Interpreter interpreter = new Interpreter();
            Grammar grammar = new Grammar(codes[codes.length - 1]);
            //System.out.println(grammar.toString());
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
            System.err.println(e.getPosition() + "," + e.getMessage() + " "
                    + e.getInfo());
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
