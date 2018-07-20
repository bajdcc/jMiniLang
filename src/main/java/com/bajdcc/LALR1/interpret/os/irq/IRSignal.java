package com.bajdcc.LALR1.interpret.os.irq;

import com.bajdcc.LALR1.interpret.os.IOSCodePage;
import com.bajdcc.util.ResourceLoader;

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
		return ResourceLoader.INSTANCE.load(getClass());
	}
}
