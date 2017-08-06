package priv.bajdcc.LALR1.interpret.os.user.routine;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;
import priv.bajdcc.util.ResourceLoader;

/**
 * 【用户态】服务
 *
 * @author bajdcc
 */
public class URTask implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/task";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
