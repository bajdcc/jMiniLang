package com.bajdcc.LALR1.ui;

import com.bajdcc.LALR1.grammar.Grammar;
import com.bajdcc.LALR1.grammar.runtime.RuntimeCodePage;
import com.bajdcc.LALR1.grammar.runtime.RuntimeException;
import com.bajdcc.LALR1.interpret.Interpreter;
import com.bajdcc.LALR1.interpret.module.ModuleRemote;
import com.bajdcc.LALR1.interpret.os.IOSCodePage;
import com.bajdcc.LALR1.interpret.os.irq.IRPrint;
import com.bajdcc.LALR1.interpret.os.irq.IRRemote;
import com.bajdcc.LALR1.interpret.os.irq.IRSignal;
import com.bajdcc.LALR1.interpret.os.irq.IRTask;
import com.bajdcc.LALR1.interpret.os.kern.OSEntry;
import com.bajdcc.LALR1.interpret.os.kern.OSIrq;
import com.bajdcc.LALR1.interpret.os.kern.OSTask;
import com.bajdcc.LALR1.interpret.os.task.*;
import com.bajdcc.LALR1.interpret.os.ui.UIClock;
import com.bajdcc.LALR1.interpret.os.ui.UIHitokoto;
import com.bajdcc.LALR1.interpret.os.ui.UIMain;
import com.bajdcc.LALR1.interpret.os.ui.UIMonitor;
import com.bajdcc.LALR1.interpret.os.user.UserMain;
import com.bajdcc.LALR1.interpret.os.user.routine.*;
import com.bajdcc.LALR1.interpret.os.user.routine.file.URFileAppend;
import com.bajdcc.LALR1.interpret.os.user.routine.file.URFileLoad;
import com.bajdcc.LALR1.interpret.os.user.routine.file.URFileSave;
import com.bajdcc.LALR1.syntax.handler.SyntaxException;
import com.bajdcc.LALR1.ui.drawing.UIGraphics;
import com.bajdcc.util.lexer.error.RegexException;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 【界面】窗口
 *
 * @author bajdcc
 */
public class UIMainFrame extends JFrame {

	private static Logger logger = Logger.getLogger("window");
	private static final String mainWndTitle = "jMiniLang Command Window";

	private UIPanel panel;
	private boolean isExitNormally = false;
	private Interpreter interpreter;

	public UIPanel getPanel() {
		return panel;
	}

	public UIMainFrame() {
		initGlobalFont();
		panel = new UIPanel();
		this.setTitle(mainWndTitle);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setPreferredSize(new Dimension(panel.getUIGraphics().getWidth(), panel.getUIGraphics().getHeight()));
		this.setContentPane(panel);
		this.pack();
		this.setLocationRelativeTo(null);
		//this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (isExitNormally) {
					logger.info("Exit.");
				} else {
					logger.info("Exit by window closing.");
					UIMainFrame.this.stopOS();
				}
			}
		});
	}

	private static void initGlobalFont() {
		FontUIResource fontUIResource = new FontUIResource(new Font("楷体", Font.PLAIN, 18));
		for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, fontUIResource);
			}
		}
	}

	public static void main(String[] args) {
		UIMainFrame frame = new UIMainFrame();
		ModuleRemote.enabled();
		ModuleRemote.setMainFrame(frame);
		frame.setTimer();
		frame.startOS(frame.getPanel().getUIGraphics());
		frame.exit();
	}

	private void stopOS() {
		interpreter.stop();
	}

	private void exit() {
		isExitNormally = true;
		UIMainFrame.this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	private void setTimer() {
		new javax.swing.Timer(33, e -> panel.repaint()).start();
	}

	private void startOS(UIGraphics g) {
		IOSCodePage pages[] = new IOSCodePage[]{
				// OS
				new OSEntry(),
				new OSIrq(),
				new OSTask(),
				// IRQ
				new IRPrint(),
				new IRRemote(),
				new IRTask(),
				new IRSignal(),
				// TASK
				new TKSystem(),
				new TKUtil(),
				new TKUI(),
				new TKNet(),
				new TKStore(),
				new TKProc(),
				// UI
				new UIMain(),
				new UIClock(),
				new UIHitokoto(),
				new UIMonitor(),
				// USER
				new UserMain(),
				// USER ROUTINE
				new URShell(),
				new UREcho(),
				new URPipe(),
				new URDup(),
				new URGrep(),
				new URRange(),
				new URProc(),
				new URTask(),
				new URSleep(),
				new URTime(),
				new URCount(),
				new URTest(),
				new URMsg(),
				new URNews(),
				new URBash(),
				new URReplace(),
				new URUtil(),
				new URAI(),
				new URPC(),
				new URMusic(),
				// USER FILE
				new URFileLoad(),
				new URFileSave(),
				new URFileAppend(),
		};

		try {
			String code = "import \"sys.base\";\n" +
					"import \"sys.proc\";\n" +
					"call g_load_sync_x(\"/kern/entry\");\n";

			interpreter = new Interpreter();

			for (IOSCodePage page : pages) {
				interpreter.load(page);
			}

			Grammar grammar = new Grammar(code);
			//System.out.println(grammar.toString());
			RuntimeCodePage page = grammar.getCodePage();
			//System.out.println(page.toString());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			RuntimeCodePage.exportFromStream(page, baos);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			interpreter.run("@main", bais);

		} catch (RegexException e) {
			System.err.println();
			System.err.println(e.getPosition() + "," + e.getMessage());
			e.printStackTrace();
		} catch (SyntaxException e) {
			System.err.println();
			System.err.println(String.format("模块名：%s. 位置：%s. 错误：%s-%s(%s:%d)",
					e.getPageName(), e.getPosition(), e.getMessage(),
					e.getInfo(), e.getFileName(), e.getPosition().iLine + 1));
			e.printStackTrace();
		} catch (RuntimeException e) {
			System.err.println();
			System.err.println(e.getError().getMessage() + " " + e.getPosition() + ": " + e.getInfo());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println();
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void showDelay() {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				if (System.getProperty("os.name").startsWith("Windows")) {
					HWND hwnd = User32.INSTANCE.FindWindow("SunAwtFrame", mainWndTitle);
					if (hwnd != null) {
						User32.INSTANCE.SetForegroundWindow(hwnd);
						User32.INSTANCE.SetFocus(hwnd);
						return;
					}
				}
				setAlwaysOnTop(true);
			}
		}, 1000);
	}
}
