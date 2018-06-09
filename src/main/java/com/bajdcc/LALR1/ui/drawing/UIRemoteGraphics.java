package com.bajdcc.LALR1.ui.drawing;

import sun.awt.SunHints;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 【界面】远程显示屏
 *
 * @author bajdcc
 */
public class UIRemoteGraphics {

	private int width, height;
	private int x, y, old_x, old_y;
	private int lineWidth;
	private boolean svgmode, stringmode;
	private StringBuilder sb;
	private Queue<Character> queue;
	private Image image;
	private Graphics gimage;
	private Color bg, fg;
	private static Pattern pat = Pattern.compile("(\\w)\\s*([0-9-]+)?\\s*([0-9-]+)?");
	private StringBuilder cache;

	public UIRemoteGraphics(int width, int height) {
		this.width = width;
		this.height = height;
		this.x = 0;
		this.y = 0;
		this.old_x = 0;
		this.old_y = 0;
		this.lineWidth = 9999;
		this.svgmode = false;
		this.stringmode = false;
		this.queue = new LinkedBlockingQueue<>();
		this.cache = new StringBuilder(1024);
		this.sb = new StringBuilder(1024);
		this.bg = Color.white;
		this.fg = Color.black;
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.gimage = this.image.getGraphics();
		this.gimage.setColor(bg);
		this.gimage.fillRect(0, 0, width, height);
		this.gimage.setColor(fg);
		this.gimage.setFont(new Font("楷体", Font.PLAIN, 20));
		Graphics2D g2d = (Graphics2D) gimage;
		g2d.setRenderingHint(SunHints.KEY_ANTIALIASING, SunHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(SunHints.KEY_TEXT_ANTIALIASING, SunHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
		g2d.setRenderingHint(SunHints.KEY_STROKE_CONTROL, SunHints.VALUE_STROKE_DEFAULT);
		g2d.setRenderingHint(SunHints.KEY_TEXT_ANTIALIAS_LCD_CONTRAST, 140);
		g2d.setRenderingHint(SunHints.KEY_FRACTIONALMETRICS, SunHints.VALUE_FRACTIONALMETRICS_OFF);
		g2d.setRenderingHint(SunHints.KEY_RENDERING, SunHints.VALUE_RENDER_DEFAULT);
	}

	public void paint(Graphics2D g) {
		final char cmd_c = '\uffee';
		final char cmd_l = '\uffed';
		final char cmd_ml = '\uffec';
		while (true) {
			Character c = this.queue.poll();
			if (c == null) {
				g.drawImage(image, 0, 0, null);
				return;
			}
			if (c == '`')
				break;
			cache.append(c);
		}
		String cmd = cache.toString();
		for (int i = 0; i < cmd.length(); i++) {
			char c = cmd.charAt(i);
			if (c == cmd_c && !this.stringmode) {
				if (this.svgmode) {
					drawSVGPath(sb.toString());
				} else {
					sb.delete(0, sb.length());
				}
				this.svgmode = !this.svgmode;
			} else if ((c == cmd_l || c == cmd_ml) && !this.svgmode) {
				if (this.stringmode) {
					if (c == cmd_l)
						drawString(sb.toString());
					else
						drawStringMultiLine(sb.toString());
				} else {
					sb.delete(0, sb.length());
				}
				this.stringmode = !this.stringmode;
			} else {
				if (this.svgmode || this.stringmode) {
					sb.append(c);
				}
			}
		}
		cache.delete(0, cache.length());
		g.drawImage(image, 0, 0, null);
	}

	private void drawString(String s) {
		this.gimage.drawString(s, x, y);
	}

	private void drawStringMultiLine(String text) {
		FontMetrics m = this.gimage.getFontMetrics();
		drawStringMultiLine(text, m, x, y);
	}

	private void drawStringMultiLine(String text, FontMetrics m, int x, int y) {
		String[] words = text.split("\r?\n");
		if (words.length > 1) {
			for (String word : words) {
				drawStringMultiLine(word, m, x, y);
				y += m.getHeight();
			}
		} else if (m.stringWidth(text) < lineWidth) {
			this.gimage.drawString(text, x, y);
		} else {
			words = text.split("");
			StringBuilder currentLine = new StringBuilder(128);
			currentLine.append(words[0]);
			for (int i = 1; i < words.length; i++) {
				if (m.stringWidth(currentLine + words[i]) < lineWidth) {
					currentLine.append(words[i]);
				} else {
					this.gimage.drawString(currentLine.toString(), x, y);
					y += m.getHeight();
					currentLine.delete(0, currentLine.length());
					currentLine.append(words[i]);
				}
			}
			if (currentLine.toString().trim().length() > 0) {
				this.gimage.drawString(currentLine.toString(), x, y);
			}
		}
	}

	private void drawSVGPath(String s) {
		Matcher m = pat.matcher(s);
		if (m.find()) {
			String arg1 = m.group(2), arg2 = m.group(3);
			old_x = x;
			old_y = y;
			switch (m.group(1).charAt(0)) {
				case 'M':
					x = tryParse(arg1);
					y = tryParse(arg2);
					break;
				case 'm':
					x += tryParse(arg1);
					y += tryParse(arg2);
					break;
				case 'L':
					x = tryParse(arg1);
					y = tryParse(arg2);
					drawLine(old_x, old_y, x, y);
					break;
				case 'l':
					x += tryParse(arg1);
					y += tryParse(arg2);
					drawLine(old_x, old_y, x, y);
					break;
				case 'R':
					x = tryParse(arg1);
					y = tryParse(arg2);
					clear(old_x, old_y, x, y);
					break;
				case 'r':
					x += tryParse(arg1);
					y += tryParse(arg2);
					clear(old_x, old_y, x, y);
					break;
				case 'W':
					lineWidth = tryParse(arg1);
					break;
			}
		}
	}

	private void drawLine(int x1, int y1, int x2, int y2) {
		this.gimage.drawLine(x1, y1, x2, y2);
	}

	private static Integer tryParse(String str) {
		Integer retVal;
		try {
			retVal = Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			retVal = 0;
		}
		return retVal;
	}

	public void clear(int x1, int y1, int x2, int y2) {
		Color c = this.gimage.getColor();
		this.gimage.setColor(Color.white);
		this.gimage.fillRect(x1, y1, x2 - x1, y2 - y1);
		this.gimage.setColor(c);
	}

	public void drawText(char c) {
		this.queue.add(c);
	}
}
