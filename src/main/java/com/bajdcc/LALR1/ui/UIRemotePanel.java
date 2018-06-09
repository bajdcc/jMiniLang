package com.bajdcc.LALR1.ui;

import com.bajdcc.LALR1.ui.drawing.UIRemoteGraphics;

import javax.swing.*;
import java.awt.*;

/**
 * 【界面】远程渲染界面
 *
 * @author bajdcc
 */
public class UIRemotePanel extends JPanel {

	private UIRemoteGraphics graphics;

	public static int w = 890;
	public static int h = 655;

	public UIRemotePanel() {
		this.graphics = new UIRemoteGraphics(w, h);
		this.setFocusable(false);
	}

	public UIRemoteGraphics getUIGraphics() {
		return graphics;
	}

	public void paint(Graphics g) {
		graphics.paint((Graphics2D) g);
	}
}
