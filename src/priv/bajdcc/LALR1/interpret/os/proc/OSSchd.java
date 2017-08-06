package priv.bajdcc.LALR1.interpret.os.proc;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;
import priv.bajdcc.util.ResourceLoader;

/**
 * 【进程】多进程调度
 *
 * @author bajdcc
 */
public class OSSchd implements IOSCodePage {
	@Override
	public String getName() {
		return "/proc/schd";
	}

	@Override
	public String getCode() {
		return ResourceLoader.load(getClass());
	}
}
