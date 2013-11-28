package cn.yicha.tupo.http.file;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 
 * @author gudh
 * @date 2013-11-28
 */
public class FileUtil {

	/**
	 * 设置字节
	 * 
	 * @param setTo
	 * @param startTo
	 * @param setFrom
	 * @param startFrom
	 * @param len
	 */
	public static void setBytes(byte[] setTo, int startTo, byte[] setFrom,
			int startFrom, int len) {
		for (int i = 0; i < len; i++) {
			setTo[startTo + i] = setFrom[startFrom + i];
		}
	}

	public static void saveFile(String fileName, byte[] bytes) {
		try {
			FileOutputStream fos = new FileOutputStream(fileName, true);
			fos.write(bytes);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
