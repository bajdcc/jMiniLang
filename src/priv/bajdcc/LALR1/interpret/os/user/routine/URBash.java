package priv.bajdcc.LALR1.interpret.os.user.routine;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;
import priv.bajdcc.util.ResourceLoader;

/**
 * 【用户态】批处理
 *
 * @author bajdcc
 */
public class URBash implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/bash";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
