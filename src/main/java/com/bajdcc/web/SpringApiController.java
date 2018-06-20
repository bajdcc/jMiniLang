package com.bajdcc.web;

import com.bajdcc.LALR1.interpret.module.ModuleNet;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 【Web服务】启动类
 *
 * @author bajdcc
 */
@RestController
@RequestMapping("/api")
public class SpringApiController {

	@RequestMapping(value = "/env", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Object environment() {
		Object obj = ModuleNet.getInstance().getWebApi().sendRequest("env");
		if (obj != null) {
			return obj;
		}
		return "";
	}
}
