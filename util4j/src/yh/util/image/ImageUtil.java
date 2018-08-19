package yh.util.image;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;

import yh.util.image.gif.GifUtil;

import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * 图片处理类
 * @author yh
 * 2012-4-6 13:15
 */
public class ImageUtil {
	
	public static void main(String[] args) {

		ImageUtil.listFormat();

		String[]imageNames=new String[]{"D:\\car\\3\\Zoom0.25\\1.jpg",
				"D:\\car\\3\\Zoom0.25\\2.jpg","D:\\car\\3\\Zoom0.25\\3.jpg",
				"D:\\car\\3\\Zoom0.25\\4.jpg","D:\\car\\3\\Zoom0.25\\5.jpg"};
		
		try {
			GifUtil.writeGifFile(imageNames, "d:\\res2.gif");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/*试试启动jvm时加上 -Djava.awt.headless=true

	 找到解决的办法了
	 1.System.setProperty("java.awt.headless", "true");
	 2.启动tomcat加上 startup.sh -Djava.awt.headless=true */

	private static Component component = new Canvas();
	
	// ".pcx","tga",".tif"这三种格式目前还不支持； 这些定义的格式经过我测试过是可以支持的。
	private static String[] imageFormatArray = new String[] { ".jpg", ".jpeg",
			".gif", ".png", ".bmp" };

	/**
	 * 查看图像I/O库所支持的图像格式有哪些格式
	 */
	public synchronized static void listFormat() {
		String readerMIMETypes[] = ImageIO.getReaderMIMETypes();
		String writerMIMETypes[] = ImageIO.getWriterMIMETypes();

		System.out.println("ReaderMIMETypes:" + Arrays.asList(readerMIMETypes));
		System.out.println("WriterMIMETypes:" + Arrays.asList(writerMIMETypes));
	}

	/**
	 * 将目录下的所有图像进行放大缩小
	 * 
	 * @param strDir
	 *            图像的目录
	 * @param zoomRatio
	 *            放大缩小的倍率
	 * @param rebuild
	 *            是否重新创建，即已经存在的图像是否覆盖重建
	 * @throws Exception
	 *             Exception
	 */
	public synchronized static void zoom(String strDir, double zoomRatio,
			boolean rebuild) throws Exception {
		File fileDir = new File(strDir);
		if (!fileDir.exists()) {
			System.out.println("Not exist:" + strDir);
			return;
		}
		String dirTarget = strDir + "/Zoom" + zoomRatio;
		File fileTarget = new File(dirTarget);
		if (!fileTarget.exists()) {
			fileTarget.mkdir();
		}

		File[] files = fileDir.listFiles();
		StringBuilder stringBuilder;
		for (File file : files) {
			String fileFullName = file.getCanonicalPath();
			String fileShortName = file.getName();
			if (!new File(fileFullName).isDirectory())// 排除二级目录，如果想就再递归一次，这里省略
			{
				if (isZoomAble(fileShortName)) {
					System.out.println("Begin Zoom:" + fileFullName);

					stringBuilder = new StringBuilder();
					stringBuilder.append(dirTarget).append("/").append(
							fileShortName);
					if (!new File(stringBuilder.toString()).exists() || rebuild) {
						try {
							createZoomSizeImage(fileFullName, stringBuilder
									.toString(), zoomRatio);
						} catch (Exception e) {
							System.out.println("createZoomSizeImage Error:"
									+ fileFullName);
						}
					}
					System.out.println("End Zoom:" + fileFullName);

				} else {
					System.out.println("Can't Zoom:" + fileFullName);
				}
			}
		}
	}

	/**
	 * 校验图像文件的格式是否可以进行缩放
	 * 
	 * @param fileName
	 *            fileName
	 * @return boolean iszoom able
	 */
	public synchronized static boolean isZoomAble(String fileName) {
		boolean result = false;
		for (String imageFormat : imageFormatArray) {
			if (fileName.toLowerCase().lastIndexOf(imageFormat) == (fileName
					.length() - imageFormat.length())) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * 图片质量压缩，压缩后图片大小变小，图片的质量变差
	 * @param fileName 源图片文件
	 * @param targetFileName 压缩后目的文件
	 * @param compressRatio 压缩率
	 * @return
	 */
	public synchronized static boolean compressImage(String fileName,String targetFileName,float compressRatio){
		try {
			File _file = new File(fileName);
			Image src = ImageIO.read(_file);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(wideth, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);
			
			if(!new File(targetFileName).getParentFile().exists()){
				new File(targetFileName).getParentFile().mkdirs();
			}
			
			FileOutputStream out = new FileOutputStream(targetFileName);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(image);
			param.setQuality(compressRatio, true);// 默认0.75
			encoder.setJPEGEncodeParam(param);
			encoder.encode(image);
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 按比例进行放大缩小图像，zoomRatio = 1为原大，zoomRatio > 1为放大，zoomRatio < 1 为缩小
	 * 
	 * @param fileName
	 *            source file name
	 * @param fileNameTarget
	 *            target file name
	 * @param zoomRatio
	 *            zoom ratio
	 * @throws Exception
	 *             exception
	 */
	public synchronized static void createZoomSizeImage(String fileName,
			String fileNameTarget, double zoomRatio) throws Exception {
		Image image = ImageIO.read(new File(fileName));
		int width = new Double(image.getWidth(null) * zoomRatio).intValue();
		int height = new Double(image.getHeight(null) * zoomRatio).intValue();
		AreaAveragingScaleFilter areaAveragingScaleFilter = new AreaAveragingScaleFilter(
				width, height);
		FilteredImageSource filteredImageSource = new FilteredImageSource(image
				.getSource(), areaAveragingScaleFilter);
		BufferedImage bufferedImage = new BufferedImage(width, height,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics graphics = bufferedImage.createGraphics();
		graphics.drawImage(component.createImage(filteredImageSource), 0, 0,
				null);
		ImageIO.write(bufferedImage, "JPEG", new File(fileNameTarget));
	}

	/**
	 * 选择区域复制图像
	 * @param fileName 复制源
 	 * @param fileNameTarget 复制目的
	 * @param x 源图片x坐标
	 * @param y 源图片y坐标
	 * @param w 复制图片宽
	 * @param h 复制图片高
	 * @throws Exception
	 */
	public synchronized static void cropImage(String fileName,
			String fileNameTarget, int x, int y, int w, int h) throws Exception {
		Image sourceImage = ImageIO.read(new File(fileName));

		ImageFilter cropFilter = new CropImageFilter(x, y, w, h);
		
		FilteredImageSource filteredImageSource = new FilteredImageSource(
				sourceImage.getSource(), cropFilter);
		BufferedImage bufferedImage = new BufferedImage(w, h,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics graphics = bufferedImage.createGraphics();
		graphics.drawImage(component.createImage(filteredImageSource), 0, 0,
				null);
		ImageIO.write(bufferedImage, "JPEG", new File(fileNameTarget));
	}

	/**
	 * 把图片印刷到图片上
	 * 
	 * @param pressImg
	 *            -- 水印文件
	 * @param targetImg
	 *            -- 目标文件
	 * @param x
	 *            -- 偏移量x
	 * @param y
	 *            -- 偏移量y
	 */
	public synchronized static void pressImage(String pressImg,
			String targetImg, int x, int y) {
		try {
			File _file = new File(targetImg);
			Image src = ImageIO.read(_file);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(wideth, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);

			// 水印文件
			File _filebiao = new File(pressImg);
			Image src_biao = ImageIO.read(_filebiao);
			int wideth_biao = src_biao.getWidth(null);
			int height_biao = src_biao.getHeight(null);
			g.drawImage(src_biao, wideth - wideth_biao - x, height
					- height_biao - y, wideth_biao, height_biao, null);
			// /
			g.dispose();
			FileOutputStream out = new FileOutputStream(targetImg);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 打印文字水印图片
	 * @param pressText 打印的文字
	 * @param targetImg 目标图片
	 * @param font 字体
	 * @param color 颜色
	 * @param x 位置x(最右下脚为0,0)
	 * @param y 位置y(最右下脚为0,0)
	 */
	public synchronized static void pressText(String pressText,
			String targetImg, Font font, Color color, int x, int y) {
		try {
			File _file = new File(targetImg);
			Image src = ImageIO.read(_file);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(wideth, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics graphics = image.createGraphics();
			graphics.drawImage(src, 0, 0, wideth, height, null);

			graphics.setColor(color);
			graphics.setFont(font);

			int fontSize=font.getSize();
			graphics.drawString(pressText, wideth - fontSize - x, height
					- fontSize / 2 - y);
			graphics.dispose();
			FileOutputStream out = new FileOutputStream(targetImg);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			out.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * 打印文字水印图片
	 * 
	 * @param pressText
	 *            --文字
	 * @param targetImg
	 *            -- 目标图片
	 * @param fontName
	 *            -- 字体名(如宋体)
	 * @param fontStyle
	 *            -- 字体样式(Font.BOLD等)
	 * @param color
	 *            -- 字体颜色(十六进制如AABBCC)
	 * @param fontSize
	 *            -- 字体大小
	 * @param x
	 *            -- 偏移量x(最右下脚为0,0)
	 * @param y
	 *            -- 偏移量y(最右下脚为0,0)
	 */

	public synchronized static void pressText(String pressText,
			String targetImg, String fontName, int fontStyle, String color,
			int fontSize, int x, int y) {
		try {
			File _file = new File(targetImg);
			Image src = ImageIO.read(_file);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(wideth, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics graphics = image.createGraphics();
			graphics.drawImage(src, 0, 0, wideth, height, null);
			// String s="www.qhd.com.cn";
			int r = Integer.parseInt(color.substring(0, 2), 16);
			int g = Integer.parseInt(color.substring(2, 4), 16);
			int b = Integer.parseInt(color.substring(4), 16);

			graphics.setColor(new Color(r, g, b));
			graphics.setFont(new Font(fontName, fontStyle, fontSize));

			graphics.drawString(pressText, wideth - fontSize - x, height
					- fontSize / 2 - y);
			graphics.dispose();
			FileOutputStream out = new FileOutputStream(targetImg);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			out.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	
	
	/**
	 * 读取GIF文件，并进行缩放，存放于BufferedImage数组中
	 * 
	 * @param inputFileName
	 *            source image file
	 * @param zoomRatio
	 *            zoom ratio > 1 zoom in; < 1 zoom out;
	 * @return BufferedImage Array
	 * @throws IOException
	 *             IOException
	 */
	public synchronized static BufferedImage[] readGifFile(
			String inputFileName, double zoomRatio) throws IOException {
		Iterator<?> imageReaders = ImageIO.getImageReadersBySuffix("GIF");
		if (!imageReaders.hasNext()) {
			throw new IOException("no ImageReaders for GIF");
		}
		ImageReader imageReader = (ImageReader) imageReaders.next();
		File file = new File(inputFileName);
		if (!file.exists()) {
			throw new IOException("no file: " + file.getName());
		}
		imageReader.setInput(ImageIO.createImageInputStream(file));
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		for (int i = 0; true; ++i) {
			try {
				Image image = imageReader.read(i);
				int width = new Double(image.getWidth(null) * zoomRatio)
						.intValue();
				int height = new Double(image.getHeight(null) * zoomRatio)
						.intValue();
				if (width > 0 && height > 0) {
					AreaAveragingScaleFilter areaAveragingScaleFilter = new AreaAveragingScaleFilter(
							width, height);
					FilteredImageSource filteredImageSource = new FilteredImageSource(
							image.getSource(), areaAveragingScaleFilter);
					BufferedImage bufferedImage = new BufferedImage(width,
							height, BufferedImage.TYPE_3BYTE_BGR);
					Graphics graphics = bufferedImage.createGraphics();
					graphics.drawImage(component
							.createImage(filteredImageSource), 0, 0, null);
					images.add(bufferedImage);
				}
			} catch (IndexOutOfBoundsException e) {
				break;
			}
		}
		return images.toArray(new BufferedImage[images.size()]);
	}

	/**
	 * 根据imageNames路径的数组，写入到GIF文件中去
	 * 
	 * @param images
	 *            source images to put into GIF
	 * @param outputFileName
	 *            target file name
	 * @throws IOException 
	 */
	public synchronized static void writeGifFile(String[] imageNames,
			String outputFileName) throws IOException {
		BufferedImage[] images=new BufferedImage[imageNames.length];
		
		for(int i=0;i<images.length;i++){
			images[i]=ImageIO.read(new File(imageNames[i]));
		}
		
		writeGifFile(images,outputFileName);
	}
	
	/**
	 * 根据BufferedImage数组的数据，写入到GIF文件中去
	 * 
	 * @param images
	 *            source images to put into GIF
	 * @param outputFileName
	 *            target file name
	 * @throws java.io.IOException
	 *             IOException
	 */
	public synchronized static void writeGifFile(BufferedImage[] images,
			String outputFileName) throws IOException {
		Iterator<?> imageWriters = ImageIO.getImageWritersBySuffix("GIF");
		if (!imageWriters.hasNext()) {
			throw new IOException("no ImageWriters for GIF");
		}
		ImageWriter imageWriter = (ImageWriter) imageWriters.next();
		File file = new File(outputFileName);
		file.delete();
		imageWriter.setOutput(ImageIO.createImageOutputStream(file));
		if (imageWriter.canWriteSequence()) {
			System.out.println("Using writeToSequence for format GIF");

			imageWriter.prepareWriteSequence(null);
			for (BufferedImage image : images) {
				imageWriter.writeToSequence(new IIOImage(image, null, null),
						null);
			}
			imageWriter.endWriteSequence();
		} else {
			System.out.println("cross fingers for format GIF");

			for (BufferedImage image : images) {
				imageWriter.write(image);
			}
		}
	}

	/**
	 * 获取图片宽度
	 * @param fileName
	 * @return
	 */
	public synchronized static int getWidth(String fileName) {
		int width = 0;
		Image image;
		try {
			image = ImageIO.read(new File(fileName));
			width = image.getWidth(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return width;
	}

	/**
	 * 获取图片高度
	 * @param fileName
	 * @return
	 */
	public synchronized static int getHeight(String fileName) {
		int height = 0;
		Image image;
		try {
			image = ImageIO.read(new File(fileName));
			height = image.getHeight(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return height;
	}
}
