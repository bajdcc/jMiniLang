package priv.bajdcc.LALR1.interpret.os.user.routine;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;
import priv.bajdcc.util.ResourceLoader;

/**
 * 【用户态】生成序列
 *
 * @author bajdcc
 */
public class URRange implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/range";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
