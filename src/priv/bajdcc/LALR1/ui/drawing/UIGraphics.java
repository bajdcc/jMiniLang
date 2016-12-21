package priv.bajdcc.LALR1.ui.drawing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 【界面】显示屏
 *
 * @author bajdcc
 */
public class UIGraphics {

	private int w, h, cols, rows, width, height, zoom, size;
	private char[] data;
	private int ptr_x, ptr_y;
	private Queue<Character> queue;
	private UIFontImage fontImage;
	private Image image;

	public UIGraphics(int w, int h, int cols, int rows, int width, int height, int zoom) {
		this.w = w;
		this.h = h;
		this.cols = cols;
		this.rows = rows;
		this.size = cols * rows;
		this.width = width * zoom;
		this.height = height * zoom;
		this.zoom = zoom;
		this.data = new char[cols * rows];
		this.ptr_x = 0;
		this.ptr_y = 0;
		this.queue = new LinkedBlockingQueue<>(1024);
		this.fontImage = new UIFontImage(this.width, this.height);
		this.image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		this.image.getGraphics().setColor(Color.white);
		this.image.getGraphics().fillRect(0, 0, w, h);
	}

	public void paint(Graphics2D g) {
		for (; ; ) {
			Character c = this.queue.poll();
			if (c == null)
				break;
			draw(g, c);
		}
		g.drawImage(image, 0, 0, null);
	}

	private void draw(Graphics2D g, char c) {
		if (c == '\n') {
			if (ptr_y == rows) {
				clear(g);
			} else {
				ptr_x = 0;
				ptr_y++;
			}
		} else if (ptr_x == cols) {
			if (ptr_y == rows) {
				clear(g);
				drawChar(g, c);
				ptr_x++;
			} else {
				drawChar(g, c);
				ptr_x = 0;
				ptr_y++;
			}
		} else {
			drawChar(g, c);
			ptr_x++;
		}
	}

	private void drawChar(Graphics2D g, char c) {
		this.data[ptr_y * rows + ptr_x] = c;
		image.getGraphics().drawImage(fontImage.getImage(c),
				ptr_x * width, ptr_y * height, null);
	}

	public void clear(Graphics2D g) {
		this.ptr_x = 0;
		this.ptr_y = 0;
		for (int i = 0; i < size; i++) {
			this.data[i] = '\0';
		}
		image.getGraphics().setColor(Color.white);
		image.getGraphics().fillRect(0, 0, w, h);
		g.drawImage(image, 0, 0, null);
	}

	public void drawText(char c) {
		this.queue.add(c);
	}
}
