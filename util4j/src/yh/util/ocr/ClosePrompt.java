package yh.util.ocr;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

/**
 * 启动线程自动关闭AspriseOCR的收费提示
 * @author yh
 * 创建时间：2012-4-17
 */
public class ClosePrompt extends Thread {
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		robot.keyPress(KeyEvent.VK_N);
		robot.keyRelease(KeyEvent.VK_N);
	}
}
