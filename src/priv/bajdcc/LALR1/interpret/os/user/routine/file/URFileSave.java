package priv.bajdcc.LALR1.interpret.os.user.routine.file;

import priv.bajdcc.LALR1.interpret.os.IOSCodePage;
import priv.bajdcc.util.ResourceLoader;

/**
 * 【用户态】写文件
 *
 * @author bajdcc
 */
public class URFileSave implements IOSCodePage {
    @Override
    public String getName() {
        return "/usr/p/>";
    }

    @Override
    public String getCode() {
        return ResourceLoader.load(getClass());
    }
}
