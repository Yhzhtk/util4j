package yh.util.image.gif;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

/**
 * Gif动态图片的操作
 * @author yh
 * 2012-4-6 14:32
 */
public class GifUtil {

	/**
	 * 根据imageNames路径的数组，写入到GIF文件中去
	 * 默认为无限重复，每张延时为100ms
	 * @param imageNames
	 * @param dstFile
	 * @throws IOException
	 * @throws AWTException
	 */
	public synchronized static void writeGifFile(String[] imageNames,
			String dstFile) throws IOException, AWTException {
		BufferedImage[] images=new BufferedImage[imageNames.length];
		
		for(int i=0;i<images.length;i++){
			images[i]=ImageIO.read(new File(imageNames[i]));
		}
		
		writeGifFile(images,dstFile,0,100);

	}
	
	/**
	 * 写gif图片
	 * @param images 源
	 * @param dstFile 目的地址
	 * @param repeatTime 重复次数，默认为1,无限次为0
	 * @param delay 每张延时
	 * @throws IOException
	 * @throws AWTException
	 */
	public synchronized static void writeGifFile(BufferedImage[] images,
			String dstFile,int repeatTime,int delay) throws IOException,
			AWTException {
		int count=images.length;
		AnimatedGifEncoder gif = new AnimatedGifEncoder();
		gif.start(dstFile);
		gif.setRepeat(repeatTime);
		gif.setTransparent(Color.white);
		
		for (int i = 0; i < count; ++i) {
			gif.setDelay(delay);
			gif.addFrame(images[i]);
		}
		gif.finish();
	}
	
	/**
	 * 改变gif大小
	 * @param srcFile
	 * @param dstFile
	 * @param wRatio
	 * @param hRatio
	 * @throws IOException
	 * @throws AWTException
	 */
	public synchronized static void resizeByRatio(String srcFile, String dstFile,
			double wRatio, double hRatio) throws IOException, AWTException {
		boolean isAnimated = isAnimateGif(srcFile);//src帧数是否大于等于2
		if (isAnimated) {
			resizeAnimatedImageByDecoder(srcFile, dstFile, wRatio, hRatio);
		} else {
			resizeNotAnimatedGif(srcFile, dstFile, wRatio, hRatio);
		}
	}

	/**
	 * 改变动态图片大小
	 * @param srcFile
	 * @param dstFile
	 * @param wRatio
	 * @param hRatio
	 * @throws IOException
	 * @throws AWTException
	 */
	public synchronized static void resizeAnimatedImageByDecoder(String srcFile,
			String dstFile, double wRatio, double hRatio) throws IOException,
			AWTException {
		GifDecoder decoder = new GifDecoder();
		decoder.read(srcFile);
		
		int count = decoder.getFrameCount();
		AnimatedGifEncoder e = new AnimatedGifEncoder();
		e.start(dstFile);
		e.setRepeat(decoder.getLoopCount());
		if (decoder.isTransparency()) {
			e.setTransparent(decoder.lastTransparencyColor);
		}

		for (int i = 0; i < count; ++i) {
			e.setDelay(decoder.getDelay(i));
			e.addFrame(resize(decoder.getFrame(i), wRatio, hRatio));
		}
		e.finish();
	}
	
	/**
	 * 改变非动态图片大小
	 * @param srcFile
	 * @param dstFile
	 * @param wRatio
	 * @param hRatio
	 * @throws IOException
	 * @throws AWTException
	 */
	public synchronized static void resizeNotAnimatedGif(String srcFile, String dstFile,
			double wRatio, double hRatio) throws IOException, AWTException {
		BufferedImage image = ImageIO.read(new File(srcFile));
		GIFEncoder encoder = new GIFEncoder(resize(image, wRatio, hRatio));
		encoder.Write(new FileOutputStream(dstFile));
	}

	/**
	 * 改变图片大小
	 * @param srcImage 原图片
	 * @param wRatio 宽度缩放倍率
	 * @param hRatio 高度缩放倍率
	 * @return
	 */
	public synchronized static BufferedImage resize(BufferedImage srcImage, double wRatio,
			double hRatio) {
		BufferedImage dstImage = null;
		AffineTransform transform = AffineTransform.getScaleInstance(wRatio,
				hRatio);// 返回表示缩放变换的变换
		AffineTransformOp op = new AffineTransformOp(transform,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		dstImage = op.filter(srcImage, null);
		return dstImage;
	}

	/**
	 * 判断是否是动态图片
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public synchronized static boolean isAnimateGif(String fileName) throws IOException {
		Iterator<?> imageReaders = ImageIO.getImageReadersBySuffix("GIF");
		if (!imageReaders.hasNext()) {
			throw new IOException("no ImageReaders for GIF");
		}
		File file = new File(fileName);
		if (!file.exists()) {
			throw new IOException("file " + fileName + " is not exists");
		}
		ImageReader imageReader = (ImageReader) imageReaders.next();
		imageReader.setInput(ImageIO.createImageInputStream(file));
		int i = 0;
		while (i < 2) {
			try {
				imageReader.read(i);
			} catch (IndexOutOfBoundsException ex) {
				break;
			}
			++i;
		}
		imageReader.abort();
		return i > 1;
	}
}
