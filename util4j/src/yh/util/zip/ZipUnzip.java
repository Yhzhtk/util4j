package yh.util.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 用于ZIP文件压缩解压
 * @author yh
 * 创建时间：2012-07-26
 */
public class ZipUnzip {

	public static void main(String[]args){
		try {
			ZipUnzip.zip("D:\\WorkDataLocal", "d:\\WorkDataLocal.zip","");
//			ZipUnzip.unZip("d:\\WorkDataLocal1.zip", "d:\\WorkDataLocal3");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 压缩文件
	 * @param inPath
	 * @param outZip
	 * @param comment
	 * @throws IOException
	 */
	public static synchronized void zip(String inPath,String outZip,String comment) throws IOException {
		File inFile = new File(inPath);
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outZip));
		zos.setComment(comment);
		zipFile(inFile, zos, "");
		zos.close();
	}

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
	 * 解压文件
	 * 
	 * @param zipFilename
	 * @param outputDirectory
	 * @throws IOException
	 */
	public static synchronized void unZip(String zipFilename,
			String outputDirectory) throws IOException {
		File outFile = new File(outputDirectory);
		if (!outFile.exists()) {
			outFile.mkdirs();
		}
		ZipFile zipFile = new ZipFile(zipFilename);
		Enumeration<?> en = zipFile.entries();
		ZipEntry zipEntry = null;
		while (en.hasMoreElements()) {
			zipEntry = (ZipEntry) en.nextElement();
			if (zipEntry.isDirectory()) {
				// mkdir directory
				String dirName = zipEntry.getName();
				dirName = dirName.substring(0, dirName.length() - 1);

				File f = new File(outFile.getPath() + File.separator + dirName);
				f.mkdirs();

			} else {
				// unzip file
				File f = new File(outFile.getPath() + File.separator
						+ zipEntry.getName());
				
				File path=new File(f.getParent());
				if(!path.exists()){
					path.mkdirs(); 
				}
				if (!f.exists()) {
					try {
						f.createNewFile();// 文本不存在,建立
					} catch (IOException e) {
						e.printStackTrace();
					} 
				}
				
				InputStream in = zipFile.getInputStream(zipEntry);
				FileOutputStream out = new FileOutputStream(f);
				try {
					int c;
					byte[] by = new byte[1024];
					while ((c = in.read(by)) != -1) {
						out.write(by, 0, c);
					}
					// out.flush();
				} catch (IOException e) {
					throw e;
				} finally {
					out.close();
					in.close();
				}
			}
		}
	}
}
