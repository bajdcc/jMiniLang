package com.bajdcc.LALR1.interpret.os.kern;

import com.bajdcc.LALR1.interpret.os.IOSCodePage;
import com.bajdcc.util.ResourceLoader;

/**
 * 【内核】IRQ中断
 *
 * @author bajdcc
 */
public class OSIrq implements IOSCodePage {

	@Override
	public String getName() {
		return "/kern/irq";
	}

	@Override
	public String getCode() {
		return ResourceLoader.INSTANCE.load(getClass());
	}
}
