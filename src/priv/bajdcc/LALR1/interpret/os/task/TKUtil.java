package priv.bajdcc.LALR1.interpret.os.task;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;
import priv.bajdcc.util.ResourceLoader;

/**
 * 【服务】工具
 *
 * @author bajdcc
 */
public class TKUtil implements IOSCodePage {
	@Override
	public String getName() {
		return "/task/util";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
