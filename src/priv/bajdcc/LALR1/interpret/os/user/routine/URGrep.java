package priv.bajdcc.LALR1.interpret.os.user.routine;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;
import priv.bajdcc.util.ResourceLoader;

/**
 * 【用户态】过滤流
 *
 * @author bajdcc
 */
public class URGrep implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/grep";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
