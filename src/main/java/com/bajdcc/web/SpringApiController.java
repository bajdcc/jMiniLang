package com.bajdcc.web;

import com.bajdcc.LALR1.grammar.runtime.RuntimeProcess;
import com.bajdcc.LALR1.interpret.module.ModuleNet;
import com.bajdcc.LALR1.interpret.module.user.ModuleUserBase;
import com.bajdcc.web.bean.SpringBeanExec;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

	@RequestMapping(value = "/vfs", params = "path", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public Object getVfs(@RequestParam("path") String path) {
		Map<String, Object> map = new HashMap<>();
		path = StringEscapeUtils.unescapeHtml4(path);
		Object obj = ModuleNet.getInstance().getWebApi().sendRequest("vfs/" + path.replace('/', '_'));
		if (obj != null) {
			map.put("code", 200);
			map.put("data", obj);
		} else {
			map.put("code", 404);
		}
		return map;
	}

	private static int execId = 1;

	@RequestMapping(value = "/exec", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public Object execCode(@RequestBody SpringBeanExec ctx) {
		Map<String, Object> map = new HashMap<>();
		if (ctx.getCode() != null) {
			RuntimeProcess.writePipe(ModuleUserBase.EXEC_PREFIX + execId, ctx.getCode());
			map.put("code", 200);
			map.put("data", ModuleNet.getInstance().getWebApi().sendRequest("exec/" + execId));
			execId++;
		} else {
			map.put("code", 404);
		}
		return map;
	}
}
