package priv.bajdcc.LALR1.interpret.os.user.routine;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;
import priv.bajdcc.util.ResourceLoader;

/**
 * 【用户态】通讯
 *
 * @author bajdcc
 */
public class URMsg implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/msg";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
