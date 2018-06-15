package com.bajdcc.util;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

public class ResourceLoader {

	private static Logger logger = Logger.getLogger("loader");
	private static String sep = System.lineSeparator();

	private static final String PACKAGE_NAME = "/com/bajdcc/code";

	private static String getPrefix(String name) {
		if (name.startsWith("Module")) {
			if (name.startsWith("ModuleUser")) {
				return "/module/user/" + name;
			} else if (name.startsWith("ModuleStd")) {
				return "/module/std/" + name;
			}
			return "/module/" + name;
		} else if (name.startsWith("U")) {
			if (name.startsWith("URFile")) {
				return "/os/user/routine/file/" + name;
			} else if (name.startsWith("UR")) {
				return "/os/user/routine/" + name;
			} else if (name.startsWith("UI")) {
				return "/os/ui/" + name;
			}  else {
				return "/os/user/" + name;
			}
		} else if (name.startsWith("IR")) {
			return "/os/irq/" + name;
		} else if (name.startsWith("OS")) {
			return "/os/kern/" + name;
		} else if (name.startsWith("TK")) {
			return "/os/task/" + name;
		}  else {
			return "/os/user/" + name;
		}
	}

	public static String load(Class cls) {
		BufferedReader buffer = null;
		try {
			logger.debug("Load txt: " + cls.getResource(PACKAGE_NAME + getPrefix(cls.getSimpleName()) + ".txt").toURI());
			buffer = new BufferedReader(new InputStreamReader(cls.getResourceAsStream(PACKAGE_NAME + getPrefix(cls.getSimpleName()) + ".txt"), "UTF-8"));
			return buffer.lines().collect(Collectors.joining(System.lineSeparator()));
		} catch (UnsupportedEncodingException | URISyntaxException e) {
			e.printStackTrace();
		}
		return "";
	}
}
