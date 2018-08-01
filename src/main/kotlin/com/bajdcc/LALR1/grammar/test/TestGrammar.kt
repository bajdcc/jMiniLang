package com.bajdcc.LALR1.grammar.test

import com.bajdcc.LALR1.grammar.Grammar
import com.bajdcc.LALR1.syntax.handler.SyntaxException
import com.bajdcc.util.lexer.error.RegexException

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

object TestGrammar {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            //String expr = "void main(int argc, char** argv) {return 0;}";
            //BufferedReader br = new BufferedReader(new FileReader("E:/http.c"));
            val br = BufferedReader(FileReader("E:/a.c"))
            val sb = StringBuilder()
            var line = br.readLine()
            while (line != null) {
                sb.append(line).append(System.lineSeparator())
                line = br.readLine()
            }
            br.close()
            val grammar = Grammar(sb.toString())
            //System.out.println(grammar.getNgaString());
            println(grammar.npaString)
            println(grammar.inst)
            println(grammar.trackerError)
            println(grammar.tokenList)
            println(grammar.getObject())
            //FileWriter fw = new FileWriter("E:/testgrammar.txt");
            //fw.append(grammar.toString());
            //fw.close();
        } catch (e: RegexException) {
            System.err.println(e.position.toString() + "," + e.message)
            e.printStackTrace()
        } catch (e: SyntaxException) {
            System.err.println(e.position.toString() + "," + e.message + " "
                    + e.info)
            e.printStackTrace()
        } catch (e: IOException) {
            System.err.println(e.message)
            e.printStackTrace()
        }

    }
}
