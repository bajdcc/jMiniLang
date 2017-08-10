package priv.bajdcc.LALR1.ui.drawing;

import org.apache.log4j.Logger;
import sun.awt.SunHints;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 【界面】点阵文字位图
 *
 * @author bajdcc
 */
public class UIFontImage {

	private static Logger logger = Logger.getLogger("font");

	private int width, height;
	private Image[] images;

	public UIFontImage(int width, int height) {
		this.width = width;
		this.height = height;
		this.images = new BufferedImage[65536];
	}

	/**
	 * 按需生成字符图像，免去数秒的初始化，节省时间
	 *
	 * @param i 字符的编码
	 */
	private void drawImage(int i) {
		Font asciiFont = new Font(Font.MONOSPACED, Font.PLAIN, 18);
		Font unicodeFont = new Font(Font.SERIF, Font.PLAIN, 12);
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
			if (i < 256) {
				g.setFont(asciiFont);
				String str = Character.toString((char) i);
				int x = width / 2 - g.getFontMetrics().stringWidth(str) / 2;
				int y = height / 2 + g.getFontMetrics().getHeight() / 3;
				g.drawString(str, x, y);
			} else {
				g.setFont(unicodeFont);
				String str = Character.toString((char) i);
				int x = width / 2 - g.getFontMetrics().stringWidth(str) / 2;
				int y = height / 2 + g.getFontMetrics().getHeight() / 2;
				g.drawString(str, x, y);
			}
		}
	}

	public Image getImage(int c) {
		if (c >= 0 && c <= 65535) {
			if (this.images[c] == null)
				drawImage(c);
			return this.images[c];
		}
		return this.images[0];
	}
}
