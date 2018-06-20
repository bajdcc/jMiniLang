package com.bajdcc.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 【Web服务】启动类
 *
 * @author bajdcc
 */
@SpringBootApplication
@RestController
public class SpringBootstrap extends Thread {

	private ConfigurableApplicationContext ctx;

	@RequestMapping("/")
	public String hello() {
		return "Hello World!";
	}

	@Override
	public void run() {
		ctx = SpringApplication.run(getClass());
	}

	public void terminate() {
		ctx.close();
	}
}
