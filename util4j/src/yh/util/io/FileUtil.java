package yh.util.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
	/**
	 * 复制文件
	 * 
	 * @param from
	 * @param to
	 */
	static void copy(String from, String to) {
		InputStream in;
		if (!new File(to).getParentFile().exists()) {
			new File(to).getParentFile().mkdirs();
		}

		try {
			in = new BufferedInputStream(new FileInputStream(from));
			OutputStream out = new BufferedOutputStream(
					new FileOutputStream(to));
			for (int b = in.read(); b != -1; b = in.read()) {
				out.write(b);
			}
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("copy：" + from + "  to  " + to);
	}
}
