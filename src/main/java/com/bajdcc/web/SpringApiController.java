package com.bajdcc.web;

import com.bajdcc.LALR1.interpret.module.ModuleNet;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 【Web服务】API接口
 *
 * @author bajdcc
 */
@RestController
@RequestMapping("/api")
public class SpringApiController {

	@RequestMapping(value = "/query/{item}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public Object queryItem(@PathVariable(value = "item") String item) {
		Map<String, Object> map = new HashMap<>();
		Object obj = ModuleNet.getInstance().getWebApi().sendRequest("query/" + item);
		if (obj != null) {
			map.put("code", 200);
			map.put("data", obj);
		} else {
			map.put("code", 404);
		}
		return map;
	}

	@RequestMapping(value = "/md/{item}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public Object getMarkdown(@PathVariable(value = "item") String item) {
		Map<String, Object> map = new HashMap<>();
		Object obj = ModuleNet.getInstance().getWebApi().sendRequest("md/" + item);
		if (obj != null) {
			map.put("code", 200);
			map.put("data", obj);
		} else {
			map.put("code", 404);
		}
		return map;
	}
}
