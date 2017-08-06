package priv.bajdcc.LALR1.interpret.os.user.routine;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;
import priv.bajdcc.util.ResourceLoader;

/**
 * 【用户态】测试
 *
 * @author bajdcc
 */
public class URTest implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/test";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
