package com.bajdcc.util

import org.apache.log4j.Logger

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException
import java.net.URISyntaxException
import java.util.stream.Collectors

object ResourceLoader {

    private val logger = Logger.getLogger("loader")
    private const val PACKAGE_NAME = "/com/bajdcc/code"

    private fun getPrefix(name: String): String =
            with(name) {
                when {
                    startsWith("Module") -> when {
                        startsWith("User", 6) -> "/module/user/$name"
                        startsWith("Std", 6) -> "/module/std/$name"
                        else -> "/module/$name"
                    }
                    else -> when {
                        startsWith("U") -> when {
                            startsWith("RFile", 1) -> "/os/user/routine/file/$name"
                            startsWith("R", 1) -> "/os/user/routine/$name"
                            startsWith("I", 1) -> "/os/ui/$name"
                            else -> "/os/user/$name"
                        }
                        startsWith("IR") -> "/os/irq/$name"
                        startsWith("OS") -> "/os/kern/$name"
                        startsWith("TK") -> "/os/task/$name"
                        else -> "/os/user/$name"
                    }
                }
            }

    fun load(cls: Class<*>): String {
        val buffer: BufferedReader
        try {
            val path = PACKAGE_NAME + getPrefix(cls.simpleName) + ".txt"
            logger.debug("Load txt: " + cls.getResource(path).toURI())
            buffer = BufferedReader(InputStreamReader(cls.getResourceAsStream(path), "UTF-8"))
            val code = buffer.lines().collect(Collectors.joining(System.lineSeparator()))
            return if (code.isEmpty()) ";" else code
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
        return ""
    }
}
