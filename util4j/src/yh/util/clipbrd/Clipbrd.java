package yh.util.clipbrd;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 剪切板的操作
 *@Author: yh
 *@Date:2013-7-8
 */
public class Clipbrd {

	public static void main(String[] args) {

		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		robot.keyPress(154);
		robot.keyRelease(154);

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Image img=(Image)getClipboard();
		
		saveImage(img,"d:\\b.jpg");
	}

	/**
	 * 保存图片到本地
	 * @param img
	 * @param fileName
	 */
	public static void saveImage(Image img,String fileName) {
		int width = img.getWidth(null);
		int height = img.getHeight(null);
		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.getGraphics();
		g.drawImage(img, 0, 0, width, height, null);
		g.dispose();
		File f = new File(fileName);
		try {
			ImageIO.write(bi, fileName.substring(fileName.lastIndexOf('.')+1), f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取剪切板上的内容
	 * @return
	 */
	public static Object getClipboard() {

		Clipboard sysc = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable cc = sysc.getContents(null);
		Object obj = null;
		try {
			if (cc.isDataFlavorSupported(DataFlavor.imageFlavor)) {
				obj = (Image) cc.getTransferData(DataFlavor.imageFlavor);
			} else if (cc.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				obj = cc.getTransferData(DataFlavor.stringFlavor);
			}
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(obj);
		return obj;
	}

}
