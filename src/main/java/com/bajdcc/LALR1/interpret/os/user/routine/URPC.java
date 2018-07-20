package com.bajdcc.LALR1.interpret.os.user.routine;

import com.bajdcc.LALR1.interpret.os.IOSCodePage;
import com.bajdcc.util.ResourceLoader;

/**
 * 【用户态】行为树示例：计算机网络模拟
 *
 * @author bajdcc
 */
public class URPC implements IOSCodePage {
	@Override
	public String getName() {
		return "/usr/p/pc";
	}

	@Override
	public String getCode() {
		return ResourceLoader.INSTANCE.load(getClass());
	}
}
