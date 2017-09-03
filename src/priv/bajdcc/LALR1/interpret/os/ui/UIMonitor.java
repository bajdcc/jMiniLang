package priv.bajdcc.LALR1.interpret.os.ui;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;
import priv.bajdcc.util.ResourceLoader;

/**
 * 【界面】监视器
 *
 * @author bajdcc
 */
public class UIMonitor implements IOSCodePage {
    @Override
    public String getName() {
        return "/ui/monitor";
    }

    @Override
    public String getCode() {
        return ResourceLoader.load(getClass());
    }
}
