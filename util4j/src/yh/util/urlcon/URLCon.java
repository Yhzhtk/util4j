package yh.util.urlcon;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * 使用java.net.URLConnection连接url并获取相关文本和流信息
 * @author yh
 * 时间：2012-3-12-13:49
 */
public class URLCon {

	// 超时时间，默认10秒
	private static int connTimeout = 10000;

	/**
	 * 获取url内容
	 * 
	 * @param url
	 *            url地址
	 * @param charSet
	 *            字符编码
	 * @return
	 */
	public static String getContent(String url, String charSet) {
		String content = "";

		try {
			URL Url = new URL(url);
			URLConnection con = Url.openConnection();
			con.setReadTimeout(connTimeout);
			con.setConnectTimeout(connTimeout);
			InputStream in = con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in,
					charSet));
			String str = null;
			while (null != (str = rd.readLine())) {
				content += str;
			}
		} catch (Exception e) {
			e.printStackTrace();
			content = null;
		}

		return content;
	}

	/**
	 * 获取流
	 * 
	 * @param url
	 *            url地址
	 * @return
	 */
	public static InputStream getStream(String url) {
		InputStream in = null;
		try {
			URL Url = new URL(url);
			URLConnection con;
			con = Url.openConnection();
			con.setReadTimeout(connTimeout);
			con.setConnectTimeout(connTimeout);
			in = con.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return in;
	}

	/**
	 * 保存url内容到文件
	 * 
	 * @param url
	 *            url地址
	 * @param fileName
	 *            文件路径
	 * @return
	 */
	public static boolean saveStream(String url, String fileName) {

		InputStream inputStream = getStream(url);

		return saveStream(inputStream, fileName);
	}

	/**
	 * 将流写入文件
	 * 
	 * @param inputStream
	 *            流数据
	 * @param fileName
	 *            文件路径
	 * @return 写入是否成功
	 */
	public static boolean saveStream(InputStream inputStream, String fileName) {

		boolean res = false;

		File file = new File(fileName);

		// 判断路径是否存在，并创建
		File path = new File(file.getParent());
		if (!path.exists())
			path.mkdirs();

		// 判断能否获取权限
		DataOutputStream dataoutputstream = null;
		try {
			dataoutputstream = new DataOutputStream(new FileOutputStream(file));
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			file.delete();
			return false;
		}

		// 开始写文件
		int length = 0;
		try {
			byte abyte0[] = new byte[2048];

			for (int i = 0; (i = inputStream.read(abyte0)) != -1;) {
				length += i;
				dataoutputstream.write(abyte0, 0, i);
			}

			System.out.println(fileName + "  读取大小为: " + length);
			System.out.println(fileName + "  文件大小为: " + file.length());
			System.out.println(fileName + "  保存成功！");
			res = true;

		} catch (Exception exception1) {
			System.out.println(exception1.getMessage());
		} finally {
			try {
				dataoutputstream.close();
				inputStream.close();
			} catch (Exception exception2) {
				System.out.println(exception2.getMessage());
				file.delete();
				res = false;
			}
		}

		return res;

	}

	public static int getConnTimeout() {
		return connTimeout;
	}

	public static void setConnTimeout(int connTimeout) {
		URLCon.connTimeout = connTimeout;
	}
}
