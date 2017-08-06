package priv.bajdcc.LALR1.interpret.os.kern;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;
import priv.bajdcc.util.ResourceLoader;

/**
 * 【内核】入口
 *
 * @author bajdcc
 */
public class OSEntry implements IOSCodePage {

	@Override
	public String getName() {
		return "/kern/entry";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
