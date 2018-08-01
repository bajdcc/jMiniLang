package com.bajdcc.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 * 【Web服务】信息展示
 *
 * @author bajdcc
 */
@Controller
@RequestMapping("/info")
class SpringInfoController : Thread() {
    private val pages = setOf("env", "resource", "proc", "pipe", "share",
            "file", "vfs", "user", "doc", "exec")

    @RequestMapping(value = ["/{item}"], method = [(RequestMethod.GET)])
    fun info(@PathVariable(value = "item") item: String): String {
        return if (pages.contains(item)) "info/$item" else "info/info"
    }
}
