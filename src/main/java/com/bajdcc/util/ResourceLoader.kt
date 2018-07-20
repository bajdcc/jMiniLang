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

    private fun getPrefix(name: String): String {
        if (name.startsWith("Module")) {
            if (name.startsWith("ModuleUser")) {
                return "/module/user/$name"
            } else if (name.startsWith("ModuleStd")) {
                return "/module/std/$name"
            }
            return "/module/$name"
        } else return if (name.startsWith("U")) {
            if (name.startsWith("URFile")) {
                "/os/user/routine/file/$name"
            } else if (name.startsWith("UR")) {
                "/os/user/routine/$name"
            } else if (name.startsWith("UI")) {
                "/os/ui/$name"
            } else {
                "/os/user/$name"
            }
        } else if (name.startsWith("IR")) {
            "/os/irq/$name"
        } else if (name.startsWith("OS")) {
            "/os/kern/$name"
        } else if (name.startsWith("TK")) {
            "/os/task/$name"
        } else {
            "/os/user/$name"
        }
    }

    fun load(cls: Class<*>): String {
        val buffer: BufferedReader
        try {
            logger.debug("Load txt: " + cls.getResource(PACKAGE_NAME + getPrefix(cls.simpleName) + ".txt").toURI())
            buffer = BufferedReader(InputStreamReader(cls.getResourceAsStream(PACKAGE_NAME + getPrefix(cls.simpleName) + ".txt"), "UTF-8"))
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
