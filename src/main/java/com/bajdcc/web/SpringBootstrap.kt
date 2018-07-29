package com.bajdcc.web

import com.bajdcc.LALR1.interpret.module.ModuleNet
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/**
 * 【Web服务】启动类
 *
 * @author bajdcc
 */
@Controller
@SpringBootApplication
open class SpringBootstrap : Thread() {
    private var ctx: ConfigurableApplicationContext? = null

    @RequestMapping("/")
    fun home(): String {
        return "redirect:info/env"
    }

    override fun run() {
        var wait = 1
        while (ctx == null) {
            try {
                ctx = SpringApplication.run(javaClass)
                return
            } catch (e: Exception) {
                e.printStackTrace()
                ModuleNet.instance.resetBootstrap()
            }

            try {
                Thread.sleep((wait * 1000).toLong())
                wait++
                if (wait > 60) wait = 60
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }

    fun terminate() {
        ctx!!.close()
    }
}
