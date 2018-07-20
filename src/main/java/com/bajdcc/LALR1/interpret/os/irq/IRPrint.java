package com.bajdcc.LALR1.interpret.os.irq;

import com.bajdcc.LALR1.interpret.os.IOSCodePage;
import com.bajdcc.util.ResourceLoader;

/**
 * 【中断】文字输出
 *
 * @author bajdcc
 */
public class IRPrint implements IOSCodePage {
	@Override
	public String getName() {
		return "/irq/print";
	}

	@Override
	public String getCode() {
		return ResourceLoader.INSTANCE.load(getClass());
	}
}
