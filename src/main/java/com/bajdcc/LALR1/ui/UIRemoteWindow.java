package com.bajdcc.LALR1.ui;

import com.bajdcc.LALR1.interpret.module.ModuleRemote;
import com.bajdcc.LALR1.ui.drawing.UIRemoteGraphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * 【界面】远程用户界面
 *
 * @author bajdcc
 */
public class UIRemoteWindow extends JFrame {
	private UIRemotePanel panel;
	private static final String remoteWndTitle = "jMiniOS Remote Window";

	public UIRemotePanel getPanel() {
		return panel;
	}

	public UIRemoteWindow() {
		panel = new UIRemotePanel();
		this.setTitle(remoteWndTitle);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setPreferredSize(new Dimension(UIRemotePanel.w, UIRemotePanel.h));
		this.setContentPane(panel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
		this.setTimer();
		ModuleRemote.showMainFrame();
	}

	private void close() {
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	public UIRemoteGraphics getUIGraphics() {
		return this.panel.getUIGraphics();
	}

	private void setTimer() {
		new Timer(33, e -> panel.repaint()).start();
	}
}
