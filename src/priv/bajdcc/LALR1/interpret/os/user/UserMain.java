package priv.bajdcc.LALR1.interpret.os.user;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;
import priv.bajdcc.util.ResourceLoader;

/**
 * 【用户态】主进程
 *
 * @author bajdcc
 */
public class UserMain implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/main";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
