package com.bajdcc.web;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashSet;
import java.util.Set;

/**
 * 【Web服务】文档展示
 *
 * @author bajdcc
 */
@Controller
@RequestMapping("/md")
public class SpringMarkdownController {

	@RequestMapping(value = "/{item}", method = RequestMethod.GET)
	public String info(@PathVariable(value = "item") String item, Model model) {
		model.addAttribute( "name", item );
		return "md/index";
	}
}
