package com.bajdcc.LALR1.grammar.runtime.service;

import org.apache.log4j.Logger;
import com.bajdcc.LALR1.grammar.runtime.RuntimeObject;
import com.bajdcc.LALR1.grammar.runtime.RuntimeObjectType;
import com.bajdcc.LALR1.grammar.runtime.data.RuntimeArray;

import javax.swing.*;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

/**
 * 【运行时】运行时文件服务
 *
 * @author bajdcc
 */
public class RuntimeDialogService implements IRuntimeDialogService {

	private static final int MAX_DIALOG = 10;
	private static Logger logger = Logger.getLogger("dialog");
	private RuntimeService service;
	private DialogStruct arrDialogs[];
	private final Set<Integer> setDialogId;
	private int cyclePtr = 0;

	public RuntimeDialogService(RuntimeService service) {
		this.service = service;
		this.arrDialogs = new DialogStruct[MAX_DIALOG];
		this.setDialogId = new HashSet<>();
	}

	@Override
	public int create(String caption, String text, int mode, JPanel panel) {
		if (setDialogId.size() >= MAX_DIALOG) {
			return -1;
		}
		int handle;
		for (; ; ) {
			if (arrDialogs[cyclePtr] == null) {
				handle = cyclePtr;
				DialogStruct ds = new DialogStruct(handle, caption, text, mode, panel);
				synchronized (setDialogId) {
					setDialogId.add(cyclePtr);
					arrDialogs[cyclePtr++] = ds;
					if (cyclePtr >= MAX_DIALOG) {
						cyclePtr -= MAX_DIALOG;
					}
				}
				break;
			}
			cyclePtr++;
			if (cyclePtr >= MAX_DIALOG) {
				cyclePtr -= MAX_DIALOG;
			}
		}
		logger.debug("Dialog #" + handle + " '" + caption + "' created");
		return handle;
	}

	@Override
	public boolean show(int handle) {
		if (!setDialogId.contains(handle)) {
			return false;
		}
		DialogStruct ds = arrDialogs[handle];
		int mode = ds.mode;
		if (mode >= 0 && mode <= 4) {
			int type = mode - 1;
			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(ds.panel, ds.text, ds.caption, type);
				// 取得共享变量
				// RuntimeObject obj = service.getShareService().getSharing("DIALOG#DATA#" + handle, false);
				// 发送信号
				synchronized (setDialogId) {
					arrDialogs[handle] = null;
					setDialogId.remove(handle);
				}
				service.getPipeService().write(service.getPipeService().create("DIALOG#SIG#" + handle), '*');
			});
		} else if (mode >= 10 && mode <= 13) {
			int type = mode - 11;
			SwingUtilities.invokeLater(() -> {
				int value = JOptionPane.showConfirmDialog(ds.panel, ds.text, ds.caption, type);
				// 取得共享变量
				RuntimeObject obj = service.getShareService().getSharing("DIALOG#DATA#" + handle, false);
				assert obj.getType() == RuntimeObjectType.kArray;
				((RuntimeArray) obj.getObj()).add(new RuntimeObject(BigInteger.valueOf(value)));
				// 发送信号
				synchronized (setDialogId) {
					arrDialogs[handle] = null;
					setDialogId.remove(handle);
				}
				service.getPipeService().write(service.getPipeService().create("DIALOG#SIG#" + handle), '*');
			});
		} else if (mode >= 20 && mode <= 24) {
			int type = mode - 21;
			SwingUtilities.invokeLater(() -> {
				String input = JOptionPane.showInputDialog(ds.panel, ds.text, ds.caption, type);
				// 取得共享变量
				RuntimeObject obj = service.getShareService().getSharing("DIALOG#DATA#" + handle, false);
				assert obj.getType() == RuntimeObjectType.kArray;
				((RuntimeArray) obj.getObj()).add(new RuntimeObject(input));
				// 发送信号
				synchronized (setDialogId) {
					arrDialogs[handle] = null;
					setDialogId.remove(handle);
				}
				service.getPipeService().write(service.getPipeService().create("DIALOG#SIG#" + handle), '*');
			});
		} else {
			synchronized (setDialogId) {
				arrDialogs[handle] = null;
				setDialogId.remove(handle);
			}
			service.getPipeService().write(service.getPipeService().create("DIALOG#SIG#" + handle), '*');
		}
		return true;
	}

	class DialogStruct {
		private int handle;
		private String caption;
		private String text;
		private int mode;
		private JPanel panel;

		DialogStruct(int handle, String caption, String text, int mode, JPanel panel) {
			this.handle = handle;
			this.caption = caption;
			this.text = text;
			this.mode = mode;
			this.panel = panel;
		}
	}
}
