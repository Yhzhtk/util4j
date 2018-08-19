package yh.util.ocr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.asprise.util.ocr.OCR;

/**
 * 使用AspriseOCR识别图片文字或者数据
 * @author yh
 * 创建时间：2012-4-17
 */
public class ParseOCR {

	/**
	 * 识别BufferedImage指定区域的文字
	 * @param image
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public static String parseCharacter(BufferedImage image,int x,int y,int width,int height) {
		String s = null;
		image=image.getSubimage(x, y, width, height);
		new ClosePrompt().start();
		s = new OCR().recognizeCharacters(image);
		System.out.println("图片识别结果是：" + s);

		return s;
	}

	/**
	 * 识别BufferedImage指定区域的数据
	 * @param image
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public static String parseEverything(BufferedImage image,int x,int y,int width,int height) {
		String s = null;
		image=image.getSubimage(x, y, width, height);
		new ClosePrompt().start();
		s = new OCR().recognizeEverything(image);
		System.out.println("图片识别结果是：" + s);

		return s;
	}
	
	/**
	 * 识别BufferedImage的文字
	 * @param image
	 * @return
	 */
	public static String parseCharacter(BufferedImage image) {
		String s = null;
		new ClosePrompt().start();
		s = new OCR().recognizeCharacters(image);
		System.out.println("图片识别结果是：" + s);

		return s;
	}

	/**
	 * 识别BufferedImage的数据
	 * @param image
	 * @return
	 */
	public static String parseEverything(BufferedImage image) {
		String s = null;
		new ClosePrompt().start();
		s = new OCR().recognizeEverything(image);
		System.out.println("图片识别结果是：" + s);

		return s;
	}
	
	/**
	 * 识别图像文件的文字
	 * @param fileName
	 * @return
	 */
	public static String parseCharacter(String fileName){
		String s = null;
		try {
			BufferedImage image = ImageIO.read(new File(fileName));
			new ClosePrompt().start();
			s = new OCR().recognizeCharacters(image);
			System.out.println("图片识别结果是：" + s);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}
	
	/**
	 * 识别图像文件的数据
	 * @param fileName
	 * @return
	 */
	public static String parseEverything(String fileName) {
		String s = null;
		try {
			BufferedImage image = ImageIO.read(new File(fileName));
			new ClosePrompt().start();
			s = new OCR().recognizeEverything(image);
			System.out.println("图片识别结果是：" + s);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}
}
