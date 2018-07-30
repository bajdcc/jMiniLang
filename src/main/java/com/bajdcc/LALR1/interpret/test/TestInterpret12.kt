package com.bajdcc.LALR1.interpret.test

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage
import com.bajdcc.LALR1.grammar.runtime.RuntimeException
import com.bajdcc.LALR1.interpret.Interpreter
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.util.lexer.error.RegexException

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object TestInterpret12 {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val codes = arrayOf("import \"sys.base\";\n" +
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
                    "call g_print(call g_lisp_repl(env, \"(cdr (cons (quote a) (quote (b c))))\"));\n" +
                    "call g_print(call g_lisp_repl(env, \"(define repeat (lambda (f) (lambda (x) (f (f x)))))\"));\n" +
                    "call g_print(call g_lisp_repl(env, \"(define twice (lambda (x) (* 2 x)))\"));\n" +
                    "call g_print(call g_lisp_repl(env, \"((repeat twice) 10)\"));\n" +
                    "call g_print(call g_lisp_repl(env, \"(define sum (lambda (n) (if (< n 2) 1 (+ n (sum (- n 1))))))\"));\n" +
                    "call g_print(call g_lisp_repl(env, \"(sum 10)\"));\n" +
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
                    "call g_print(call g_lisp_repl(env, \"(append '(a b) '(c d))\"));\n" +
                    "call g_print(call g_lisp_repl(env, \"(apply 'append '('(a b) '(c d)))\"));\n" +
                    "call g_print(call g_lisp_repl(env, \"(apply max (range 1 10))\"));\n" +
                    "call g_print(\"-----\");\n" +
                    "call g_print(call g_lisp_repl(env, \"(define fib_Y (lambda (f) (lambda (n) (if (<= n 2) 1 (+ (f (- n 1)) (f (- n 2)))))))\"));\n" +
                    "call g_print(call g_lisp_repl(env, \"(apply + (map (Y fib_Y) (range 1 10)))\"));\n" +
                    "call g_print(\"-----\");\n" +
                    "call g_print(call g_lisp_repl(env, \"(cond ((== 1 2) 3 7) ((== 4 4) 6))\"));\n" +
                    "call g_print(call g_lisp_repl(env, \"(cond ((== 1 2) 3) ((== 4 4) 6))\"));\n" +
                    "call g_print(call g_lisp_repl(env, \"(define N 8)\"));\n" +
                    "call g_print(call g_lisp_repl(env, \"(case N (1 2) (8 9))\"));\n" +
                    "call g_print(call g_lisp_repl(env, \"(case N (3 2) (2 9) ('(4 8) 5))\"));\n" +
                    "call g_print(call g_lisp_repl(env, \"(when (> N 5) 6)\"));\n" +
                    "call g_print(call g_lisp_repl(env, \"(when (> N 50) 6)\"));\n" +
                    "call g_print(call g_lisp_repl(env, \"(while (< N 12) (set! N (++ N)))\"));\n" +
                    "call g_print(call g_lisp_repl(env, \"(val N)\"));\n" +
                    "call g_print(\"-----\");\n" +
                    "call g_print(call g_lisp_repl(env, \"(call/cc (lambda (k) (* 5 4)) (lambda (c) c))\"));\n" +
                    "call g_print(call g_lisp_repl(env, \"(call/cc (lambda (k) (k 4)) (lambda (c) c))\"));\n" +
                    "")

            val interpreter = Interpreter()
            val grammar = Grammar(codes[codes.size - 1])
            //System.out.println(grammar.toString());
            val page = grammar.codePage
            //System.out.println(page.toString());
            val baos = ByteArrayOutputStream()
            RuntimeCodePage.exportFromStream(page, baos)
            val bais = ByteArrayInputStream(baos.toByteArray())
            interpreter.run("test_1", bais)

        } catch (e: RegexException) {
            System.err.println()
            System.err.println(e.position.toString() + "," + e.message)
            e.printStackTrace()
        } catch (e: SyntaxException) {
            System.err.println()
            System.err.println(e.position.toString() + "," + e.message + " "
                    + e.info)
            e.printStackTrace()
        } catch (e: RuntimeException) {
            System.err.println()
            System.err.println(e.position.toString() + ": " + e.info)
            e.printStackTrace()
        } catch (e: Exception) {
            System.err.println()
            System.err.println(e.message)
            e.printStackTrace()
        }

    }
}
