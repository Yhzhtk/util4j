package yh.util.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 用于上传索引压缩解压
 * 
 * @author yh
 * 
 */
public class ZipUtil {
	static final int BUFFER = 2048;

	/**
	 * 递归压缩路径下所有文件
	 * @param inFile
	 * @param zos
	 * @param dir
	 * @throws IOException
	 */
	static void zipFile(File inFile, ZipOutputStream zos, String dir)
			throws IOException {
		if (inFile.isDirectory()) {
			File[] files = inFile.listFiles();
			for (File file : files)
				zipFile(file, zos, dir + File.separator + inFile.getName());
		} else {
			String entryName = null;
			if (!"".equals(dir))
				entryName = dir + File.separator + inFile.getName();
			else
				entryName = inFile.getName();
			ZipEntry entry = new ZipEntry(entryName);
			zos.putNextEntry(entry);
			InputStream is = new FileInputStream(inFile);
			int len = 0;
			while ((len = is.read()) != -1)
				zos.write(len);
			is.close();
		}
	}

	/**
	 * 只压缩下一层目录
	 * @param sourcePath
	 * @param desPath
	 */
	public static void zip(String sourcePath, String desPath) {
		try {
			BufferedInputStream bis = null;

			byte[] array = new byte[BUFFER];
			File f = new File(sourcePath);
			File file[] = f.listFiles();

			FileOutputStream fos = new FileOutputStream(desPath);
			ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(
					fos));

			for (int i = 0; i < file.length; i++) {
				FileInputStream fis = new FileInputStream(file[i]);
				bis = new BufferedInputStream(fis, BUFFER);
				ZipEntry zipEntry = new ZipEntry(file[i].getName());
				zos.putNextEntry(zipEntry);

				while ((bis.read(array, 0, BUFFER)) != -1) {
					zos.write(array, 0, BUFFER);
				}
				bis.close();
			}
			zos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解压文件，只解压一层
	 * @param sourcePath
	 * @param desPath
	 */
	public static void unzip(String sourcePath, String desPath) {
		try {
			ZipFile zipFile = new ZipFile(sourcePath);
			Enumeration<?> enu = zipFile.entries();

			while (enu.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) enu.nextElement();
				if (zipEntry.isDirectory()) {
					new File(desPath + zipEntry.getName()).mkdirs();
					continue;
				}
				BufferedInputStream bis = new BufferedInputStream(zipFile
						.getInputStream(zipEntry));
				File file = new File(desPath + zipEntry.getName());
				File parent = file.getParentFile();
				if (parent != null && !parent.exists()) {
					parent.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);

				byte[] array = new byte[BUFFER];
				while ((bis.read(array, 0, BUFFER)) != -1) {
					bos.write(array, 0, BUFFER);
				}

				bos.flush();
				bos.close();
				bis.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// ZipUtil.zip("D:\\WorkData\\IPhoneData\\iphoneindex\\igameIndex201207301531", "D:/indexGame.zip");
		ZipUtil.unzip("F:/work/gameSoft/index201207271235.zip",
				"F:/work/gameSoft/");
	}
}
