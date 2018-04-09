package priv.bajdcc.LALR1.interpret.os.user.routine;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;
import priv.bajdcc.util.ResourceLoader;

/**
 * 【用户态】查看进程
 *
 * @author bajdcc
 */
public class URProc implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/proc";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
