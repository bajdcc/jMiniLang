package com.bajdcc.LALR1.ui.drawing;

import org.apache.log4j.Logger;
import java.awt.RenderingHints;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * 【界面】点阵文字位图
 *
 * @author bajdcc
 */
public class UIFontImage {

	private static Logger logger = Logger.getLogger("font");

	final static Font asciiFont = new Font(Font.MONOSPACED, Font.PLAIN, 18);
	final static Font unicodeFont = new Font("楷体", Font.PLAIN, 18);

	private int width, height;
	private Image[] imagesASCII;
	private Map<Integer, Image> imagesUnicode;
	private Color fgcolor;
	private Color bgcolor;
	private boolean enableColor;
	private boolean enableFGColor;
	private boolean enableBGColor;

	public UIFontImage(int width, int height) {
		this.width = width;
		this.height = height;
		this.imagesASCII = new BufferedImage[256];
		this.imagesUnicode = new HashMap<>();
		this.enableColor = false;
		this.enableFGColor = false;
		this.enableColor = false;
		this.fgcolor = Color.black;
		this.bgcolor = Color.white;
	}

	public static boolean isWideChar(char c) {
		return !Character.isISOControl(c) && c >= '\u00ff';
	}

	public static int calcWidth(String str) {
		int len = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (isWideChar(c))
				len += 2;
			else
				len++;
		}
		return len;
	}

	public void setFGColor(Color color) {
		this.fgcolor = color;
		enableColor = this.fgcolor.getRGB() != 0xff000000 || enableBGColor;
	}

	public void setBGColor(Color color) {
		this.bgcolor = color;
		enableColor = this.bgcolor.getRGB() != 0xffffffff || enableFGColor;
	}

	/**
	 * 按需生成字符图像，免去数秒的初始化，节省时间
	 *
	 * @param i 字符的编码
	 * @return 是否是原始宽度（不是汉字）
	 */
	private boolean drawImage(int i) {
		boolean isWide = isWideChar((char) i);
		int w = isWide ? (2 * width) : width;
		BufferedImage bi = new BufferedImage(w, height, BufferedImage.TYPE_INT_RGB);
		if (i < 256) {
			this.imagesASCII[i] = bi;
		} else {
			this.imagesUnicode.put(i, bi);
		}
		Graphics g = bi.getGraphics();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 140);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
		g.setColor(Color.white);
		g.fillRect(0, 0, w, height);
		g.setColor(Color.black);
		if (i == 7) {
			g.fillRect(0, 0, w, height);
		} else if (!Character.isISOControl((char) i)) {
			if (i < 256) {
				g.setFont(asciiFont);
				String str = Character.toString((char) i);
				int x = w / 2 - g.getFontMetrics().stringWidth(str) / 2;
				int y = height / 2 + g.getFontMetrics().getHeight() / 3;
				g.drawString(str, x, y);
			} else {
				g.setFont(unicodeFont);
				String str = Character.toString((char) i);
				int x = w / 2 - g.getFontMetrics().stringWidth(str) / 2;
				int y = height / 2 + g.getFontMetrics().getHeight() / 3;
				g.drawString(str, x, y);
				return true; // 假设是中文，比较宽
			}
		}
		return true;
	}

	private Image drawImageWithColor(int i) {
		boolean isWide = isWideChar((char) i);
		int w = isWide ? (2 * width) : width;
		BufferedImage bi = new BufferedImage(w, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.getGraphics();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 140);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
		g.setColor(bgcolor);
		g.fillRect(0, 0, w, height);
		g.setColor(fgcolor);
		if (!Character.isISOControl((char) i)) {
			if (i < 256) {
				g.setFont(asciiFont);
				String str = Character.toString((char) i);
				int x = w / 2 - g.getFontMetrics().stringWidth(str) / 2;
				int y = height / 2 + g.getFontMetrics().getHeight() / 3;
				g.drawString(str, x, y);
			} else {
				g.setFont(unicodeFont);
				String str = Character.toString((char) i);
				int x = w / 2 - g.getFontMetrics().stringWidth(str) / 2;
				int y = height / 2 + g.getFontMetrics().getHeight() / 3;
				g.drawString(str, x, y);
				return bi;
			}
		}
		return bi;
	}

	public Image getImage(int c) {
		if (!enableColor) {
			if (c >= 0 && c <= 65535) {
				if (c < 256) {
					if (this.imagesASCII[c] == null)
						drawImage(c);
					return this.imagesASCII[c];
				} else {
					if (this.imagesUnicode.get(c) == null)
						drawImage(c);
					return this.imagesUnicode.get(c);
				}
			}
			return this.imagesASCII[0];
		} else {
			return drawImageWithColor(c);
		}
	}
}
