package com.bajdcc.web;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashSet;
import java.util.Set;

/**
 * 【Web服务】信息展示
 *
 * @author bajdcc
 */
@Controller
@RequestMapping("/info")
public class SpringInfoController extends Thread {

	private ConfigurableApplicationContext ctx;
	private static Set<String> pages;

	public SpringInfoController() {
		if (pages == null) {
			pages = new HashSet<>();
			pages.add("env");
			pages.add("resource");
			pages.add("proc");
			pages.add("pipe");
			pages.add("share");
		}
	}

	@RequestMapping(value = "/{item}", method = RequestMethod.GET)
	public String info(@PathVariable(value = "item") String item) {
		if (pages.contains(item))
			return "info/" + item;
		return "info/info";
	}
}
