package com.bajdcc.web;

import com.bajdcc.LALR1.interpret.module.ModuleNet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 【Web服务】启动类
 *
 * @author bajdcc
 */
@Controller
@SpringBootApplication
public class SpringBootstrap extends Thread {

	private ConfigurableApplicationContext ctx;

	@RequestMapping("/")
	public String home() {
		return "redirect:info/env";
	}

	@Override
	public void run() {
		int wait = 1;
		while (ctx == null) {
			try {
				ctx = SpringApplication.run(getClass());
				return;
			} catch (Exception e) {
				e.printStackTrace();
				ModuleNet.getInstance().resetBootstrap();
			}
			try {
				Thread.sleep(wait * 1000);
				wait++;
				if (wait > 60) wait = 60;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void terminate() {
		ctx.close();
	}
}
