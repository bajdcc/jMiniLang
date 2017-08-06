package priv.bajdcc.LALR1.interpret.os.ui;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;
import priv.bajdcc.util.ResourceLoader;

/**
 * 【界面】时钟
 *
 * @author bajdcc
 */
public class UIClock implements IOSCodePage {
	@Override
	public String getName() {
		return "/ui/clock";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
