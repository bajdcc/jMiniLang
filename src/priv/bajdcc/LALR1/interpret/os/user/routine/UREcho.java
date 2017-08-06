package priv.bajdcc.LALR1.interpret.os.user.routine;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;
import priv.bajdcc.util.ResourceLoader;

/**
 * 【用户态】打印输出
 *
 * @author bajdcc
 */
public class UREcho implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/echo";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
