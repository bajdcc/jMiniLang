package priv.bajdcc.LALR1.ui;

import priv.bajdcc.LALR1.ui.drawing.UIRemoteGraphics;

import javax.swing.*;
import java.awt.*;

/**
 * 【界面】远程渲染界面
 *
 * @author bajdcc
 */
public class UIRemotePanel extends JPanel {

	private UIRemoteGraphics graphics;

	public UIRemotePanel() {
		this.graphics = new UIRemoteGraphics(800, 600);
		this.setFocusable(true);
	}

	public UIRemoteGraphics getUIGraphics() {
		return graphics;
	}

	public void paint(Graphics g) {
		graphics.paint((Graphics2D) g);
	}
}
