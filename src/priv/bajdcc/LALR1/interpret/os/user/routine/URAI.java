package priv.bajdcc.LALR1.interpret.os.user.routine;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;
import priv.bajdcc.util.ResourceLoader;

/**
 * 【用户态】行为树AI
 *
 * @author bajdcc
 */
public class URAI implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/ai";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
