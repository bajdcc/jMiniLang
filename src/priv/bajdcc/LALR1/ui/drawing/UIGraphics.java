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

	private static final int CARET_TIME = 20;
	private int w, h, cols, rows, width, height, zoom, size;
	private char[] data;
	private int ptr_x, ptr_y;
	private int ptr_mx, ptr_my;
	private Queue<Character> queue;
	private UIFontImage fontImage;
	private Image image;
	private boolean caret;
	private boolean caretPrev;
	private boolean caretState;
	private int caretTime;

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
		this.ptr_mx = 0;
		this.ptr_my = 0;
		this.queue = new LinkedBlockingQueue<>(1024);
		this.fontImage = new UIFontImage(this.width, this.height);
		this.image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		this.image.getGraphics().setColor(Color.white);
		this.image.getGraphics().fillRect(0, 0, w, h);
	}

	public void paint(Graphics2D g) {
		int len = 0;
		for (; ; ) {
			Character c = this.queue.poll();
			if (c == null)
				break;
			if (c == '\uffef') {
				markInput();
			} else {
				if (c == '\t')
					c = ' ';
				draw(g, c);
				len++;
			}
		}
		if (len > 0 && caretTime > 0) {
			caretTime = 0;
		}
		if (caret != caretPrev) {
			if (caretPrev && caretState) {
				hideCaret();
				caretTime = 0;
				caretState = false;
			}
			caretPrev = caret;
		} else if (caret) {
			if (caretState) {
				showCaret(g);
			} else {
				hideCaret();
			}
			if (caretTime++ >= CARET_TIME) {
				caretState = !caretState;
				caretTime = 0;
			}
		}
		g.drawImage(image, 0, 0, null);
	}

	private void showCaret(Graphics2D g) {
		if (ptr_x == cols) {
			ptr_x = 0;
			if (ptr_y == rows) {
				clear(g);
			} else {
				ptr_y++;
			}
		}
		drawChar('_');
	}

	private void hideCaret() {
		drawChar('\0');
	}

	private void draw(Graphics2D g, char c) {
		if (c == '\n') {
			if (ptr_y == rows - 1) {
				clear(g);
			} else {
				ptr_x = 0;
				ptr_y++;
			}
		} else if (c == '\b') {
			if (ptr_mx + ptr_my * cols < ptr_x + ptr_y * cols) {
				if (ptr_y == 0) {
					if (ptr_x != 0) {
						drawChar('\0');
						ptr_x--;
					}
				} else {
					if (ptr_x != 0) {
						drawChar('\0');
						ptr_x--;
					} else {
						drawChar('\0');
						ptr_x = cols - 1;
						ptr_y--;
					}
				}
			}
		} else if (c == '\r') {
			ptr_x = 0;
		} else if (ptr_x == cols - 1) {
			if (ptr_y == rows - 1) {
				clear(g);
				drawChar(c);
				ptr_x++;
			} else {
				drawChar(c);
				ptr_x = 0;
				ptr_y++;
			}
		} else {
			drawChar(c);
			ptr_x++;
		}
	}

	private void drawChar(char c) {
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

	public void setCaret(boolean caret) {
		if (this.caret != caret)
			this.caret = caret;
	}

	public boolean isHideCaret() {
		return !this.caretState && !this.caret;
	}

	private void markInput() {
		ptr_mx = ptr_x;
		ptr_my = ptr_y;
	}

	public void fallback() {
		int x = ptr_x, y = ptr_y;
		while (ptr_mx + ptr_my * cols < x + y * cols) {
			if (x == 0) {
				x = cols - 1;
				y--;
			}
			this.data[y * rows + x] = '\0';
			image.getGraphics().drawImage(fontImage.getImage('\0'),
					x * width, y * height, null);
			x--;
		}
		ptr_x = x;
		ptr_y = y;
	}
}
