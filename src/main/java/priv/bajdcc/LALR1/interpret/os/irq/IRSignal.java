package priv.bajdcc.LALR1.interpret.os.irq;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;
import priv.bajdcc.util.ResourceLoader;

/**
 * 【中断】信号
 *
 * @author bajdcc
 */
public class IRSignal implements IOSCodePage {
	@Override
	public String getName() {
		return "/irq/signal";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
