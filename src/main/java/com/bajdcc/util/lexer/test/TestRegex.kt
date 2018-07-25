package com.bajdcc.util.lexer.test

import com.bajdcc.util.lexer.error.RegexException
import com.bajdcc.util.lexer.regex.Regex
import java.util.*

object TestRegex {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val scanner = Scanner(System.`in`)
            val str = scanner.nextLine()
            val ra = Regex(str, true)
            val context = scanner.nextLine()
            val match = ra.match(context)
            if (match == null) {
                System.err.println("failed")
            } else {
                println(match)
            }
            scanner.close()
        } catch (e: RegexException) {
            System.err.println(e.position.toString() + "," + e.message)
            e.printStackTrace()
        }

    }
}
