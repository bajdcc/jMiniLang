package priv.bajdcc.LALR1.grammar.runtime.service;

import priv.bajdcc.LALR1.grammar.runtime.RuntimeObject;

import javax.swing.*;

/**
 * 【运行时】运行时对话框服务接口
 *
 * @author bajdcc
 */
public interface IRuntimeDialogService {

	/**
	 * 创建对话框
	 *
	 * @param caption 标题
	 * @param text    内容
	 * @param mode    类型
	 * @param panel   父窗口
	 * @return 对话框句柄
	 */
	int create(String caption, String text, int mode, JPanel panel);


	/**
	 * 弹出对话框
	 *
	 * @param handle 句柄
	 * @return 是否成功
	 */
	boolean show(int handle);
}
