package com.bajdcc.web

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam


/**
 * 【Web服务】文档展示
 *
 * @author bajdcc
 */
@Controller
@RequestMapping("/md")
class SpringMarkdownController {

    @RequestMapping(value = ["/{item}"], method = [(RequestMethod.GET)])
    fun info(@PathVariable(value = "item") item: String, model: Model): String {
        model.addAttribute("name", item)
        return "md/index"
    }

    @RequestMapping(value = ["/vfs"], params = ["v"], method = [(RequestMethod.GET)])
    fun vfs(@RequestParam("v") path: String, model: Model): String {
        model.addAttribute("path", path)
        return "md/vfs"
    }
}
