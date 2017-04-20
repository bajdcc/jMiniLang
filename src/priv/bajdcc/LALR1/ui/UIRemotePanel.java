package priv.bajdcc.LALR1.ui;

import priv.bajdcc.LALR1.ui.drawing.UIGraphics;

import javax.swing.*;
import java.awt.*;

/**
 * 【界面】远程渲染界面
 *
 * @author bajdcc
 */
public class UIRemotePanel extends JPanel {

	private UIGraphics graphics;

	public UIRemotePanel() {
		this.graphics = new UIGraphics(800, 600, 70, 23, 11, 25, 1);
		this.setFocusable(true);
	}

	public UIGraphics getUIGraphics() {
		return graphics;
	}

	public void paint(Graphics g) {
		graphics.paint((Graphics2D) g);
	}
}
