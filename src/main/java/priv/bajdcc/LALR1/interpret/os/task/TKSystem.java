package priv.bajdcc.LALR1.interpret.os.task;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;
import priv.bajdcc.util.ResourceLoader;

/**
 * 【服务】时间
 *
 * @author bajdcc
 */
public class TKSystem implements IOSCodePage {
	@Override
	public String getName() {
		return "/task/system";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
