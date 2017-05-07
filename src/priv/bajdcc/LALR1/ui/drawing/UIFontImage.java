package priv.bajdcc.LALR1.ui.drawing;

import sun.awt.SunHints;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 【界面】点阵文字位图
 *
 * @author bajdcc
 */
public class UIFontImage {
	private int width, height;
	private Image[] images;

	public UIFontImage(int width, int height) {
		this.width = width;
		this.height = height;
		this.images = new BufferedImage[256];
		for (int i = 0; i < 256; i++) {
			this.images[i] = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = this.images[i].getGraphics();
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(SunHints.KEY_ANTIALIASING, SunHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(SunHints.KEY_TEXT_ANTIALIASING, SunHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
			g2d.setRenderingHint(SunHints.KEY_STROKE_CONTROL, SunHints.VALUE_STROKE_DEFAULT);
			g2d.setRenderingHint(SunHints.KEY_TEXT_ANTIALIAS_LCD_CONTRAST, 140);
			g2d.setRenderingHint(SunHints.KEY_FRACTIONALMETRICS, SunHints.VALUE_FRACTIONALMETRICS_OFF);
			g2d.setRenderingHint(SunHints.KEY_RENDERING, SunHints.VALUE_RENDER_DEFAULT);
			g.setColor(Color.white);
			g.fillRect(0, 0, width, height);
			if (!Character.isISOControl((char) i)) {
				g.setColor(Color.black);
				g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
				String str = Character.toString((char) i);
				int x = width / 2 - g.getFontMetrics().stringWidth(str) / 2;
				int y = height / 2 + g.getFontMetrics().getHeight() / 3;
				g.drawString(str, x, y);
			}
		}
	}

	public Image getImage(int c) {
		if (c >= 0 && c <= 255) {
			return this.images[c];
		}
		return this.images[0];
	}
}
