package priv.bajdcc.LALR1.interpret.os.ui;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;
import priv.bajdcc.util.ResourceLoader;

/**
 * 【界面】入口
 *
 * @author bajdcc
 */
public class UIMain implements IOSCodePage {
	@Override
	public String getName() {
		return "/ui/main";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
