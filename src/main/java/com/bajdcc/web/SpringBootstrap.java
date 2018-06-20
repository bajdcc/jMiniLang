package com.bajdcc.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

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
		return "redirect:/info/home";
	}

	@Override
	public void run() {
		ctx = SpringApplication.run(getClass());
	}

	public void terminate() {
		ctx.close();
	}
}
