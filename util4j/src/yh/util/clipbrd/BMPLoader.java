package yh.util.clipbrd;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * 比较两张图片的相似度
 * @author yh
 *
 */
public class BMPLoader {
	// 改变成二进制码
	public static String[][] getPX(String args) {
		int[] rgb = new int[3];

		File file = new File(args);
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(file);
		} catch (Exception e) {
			e.printStackTrace();
		}

		int width = bi.getWidth();
		int height = bi.getHeight();
		int minx = bi.getMinX();
		int miny = bi.getMinY();
		
		String[][] list = new String[width][height];
		for (int i = minx; i < width; i++) {
			for (int j = miny; j < height; j++) {
				int pixel = bi.getRGB(i, j);
				System.out.println(Integer.toHexString(pixel));
				rgb[0] = (pixel & 0xff0000) >> 16;
				rgb[1] = (pixel & 0xff00) >> 8;
				rgb[2] = (pixel & 0xff);
				list[i][j] = rgb[0] + "," + rgb[1] + "," + rgb[2];
			}
		}
		return list;
	}
	
	public static void compareImage(String imgPath1, String imgPath2){
		String[] images = {imgPath1, imgPath2};
		if (images.length == 0) {
			System.out.println("Usage >java BMPLoader ImageFile.bmp");
			System.exit(0);
		}

		// 分析图片相似度 begin
		String[][] list1 = getPX(images[0]);
		String[][] list2 = getPX(images[1]);
		
		int xiangsi = 0;
		int busi = 0;
		int i = 0, j = 0;
		for (String[] strings : list1) {
			if ((i + 1) == list1.length) {
				continue;
			}
			for (int m=0; m<strings.length; m++) {
				try {
					String[] value1 = list1[i][j].toString().split(",");
					String[] value2 = list2[i][j].toString().split(",");
					int k = 0;
					for (int n=0; n<value2.length; n++) {
						if (Math.abs(Integer.parseInt(value1[k]) - Integer.parseInt(value2[k])) < 5) {
							xiangsi++;
						} else {
							busi++;
						}
					}
				} catch (RuntimeException e) {
					continue;
				}
				j++;
			}
			i++;
		}

		list1 = getPX(images[1]);
		list2 = getPX(images[0]);
		i = 0;
		j = 0;
		for (String[] strings : list1) {
			if ((i + 1) == list1.length) {
				continue;
			}
			for (int m=0; m<strings.length; m++) {
				try {
					String[] value1 = list1[i][j].toString().split(",");
					String[] value2 = list2[i][j].toString().split(",");
					int k = 0;
					for (int n=0; n<value2.length; n++) {
						if (Math.abs(Integer.parseInt(value1[k]) - Integer.parseInt(value2[k])) < 5) {
							xiangsi++;
						} else {
							busi++;
						}
					}
				} catch (RuntimeException e) {
					continue;
				}
				j++;
			}
			i++;
		}
		String baifen = "";
		try {
			baifen = ((Double.parseDouble(xiangsi + "") / Double.parseDouble((busi + xiangsi) + "")) + "");
			baifen = baifen.substring(baifen.indexOf(".") + 1, baifen.indexOf(".") + 3);
		} catch (Exception e) {
			baifen = "0";
		}
		if (baifen.length() <= 0) {
			baifen = "0";
		}
		if(busi == 0){
			baifen="100";
		}

		System.out.println("相似像素数量：" + xiangsi + " 不相似像素数量：" + busi + " 相似率：" + Integer.parseInt(baifen) + "%");

	}
	
	public static void getPXX(String args1,String args2) {
		
		BufferedImage bi1 = null;
		BufferedImage bi2 = null;
		try {
			bi1 = ImageIO.read(new File(args1));
			bi2 = ImageIO.read(new File(args2));
		} catch (Exception e) {
			e.printStackTrace();
		}

		int width = bi1.getWidth();
		int height = bi1.getHeight();
		int minx = bi2.getMinX();
		int miny = bi2.getMinY();

		for (int i = minx; i < width; i++) {
			for (int j = miny; j < height; j++) {
				int pixel1 = bi1.getRGB(i, j);
				int pixel2 = bi2.getRGB(i, j);
				System.out.println(Integer.toHexString(pixel1)+"("+pixel1+")  "+Integer.toHexString(pixel2)+"("+pixel2+")");
			}
		}
	}

	public static void main(String[] args){
		
		getPXX("D:\\a.jpg","D:\\b.jpg");
		
//		BMPLoader.compareImage("D:\\a.jpg", "D:\\b.jpg");
	}
}