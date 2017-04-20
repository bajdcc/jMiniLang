package priv.bajdcc.LALR1.ui;

import priv.bajdcc.LALR1.interpret.module.ModuleUI;
import priv.bajdcc.LALR1.ui.drawing.UIGraphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 【界面】渲染界面
 *
 * @author bajdcc
 */
public class UIPanel extends JPanel {

	private UIGraphics graphics;
	private ModuleUI moduleUI;

	public UIPanel() {
		this.graphics = new UIGraphics(800, 600, 70, 23, 11, 25, 1);
		moduleUI = ModuleUI.getInstance();
		moduleUI.setGraphics(this.graphics);
		this.setFocusable(true);
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP:
						moduleUI.addInputChar('\ufff0');
						break;
					case KeyEvent.VK_BACK_SPACE:
						moduleUI.addInputChar('\b');
						break;
					default:
						if (e.isControlDown()) {
							switch (e.getKeyCode()) {
								case KeyEvent.VK_C:
									moduleUI.addDisplayChar('\uffee');
									break;
							}
						} else {
							moduleUI.addInputChar(e.getKeyChar());
						}
						break;
				}
			}
		});
	}

	public UIGraphics getUIGraphics() {
		return graphics;
	}

	public void paint(Graphics g) {
		//super.paint(g);
		graphics.paint((Graphics2D) g);
	}
}
