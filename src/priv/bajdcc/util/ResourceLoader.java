package priv.bajdcc.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;

public class ResourceLoader {

	private static String sep = System.getProperty("line.separator");

	public static String load(Class cls) {
		BufferedReader buffer = null;
		try {
			buffer = new BufferedReader(new InputStreamReader(cls.getResourceAsStream(cls.getSimpleName() + ".txt"), "UTF-8"));
			return buffer.lines().collect(Collectors.joining(System.getProperty("line.separator")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
}
