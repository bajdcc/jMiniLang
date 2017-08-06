package priv.bajdcc.LALR1.interpret.os.user.routine;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;
import priv.bajdcc.util.ResourceLoader;

/**
 * 【用户态】复制流
 *
 * @author bajdcc
 */
public class URDup implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/dup";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
