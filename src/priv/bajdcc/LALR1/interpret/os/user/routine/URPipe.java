package priv.bajdcc.LALR1.interpret.os.user.routine;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;
import priv.bajdcc.util.ResourceLoader;

/**
 * 【用户态】管道
 *
 * @author bajdcc
 */
public class URPipe implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/pipe";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
