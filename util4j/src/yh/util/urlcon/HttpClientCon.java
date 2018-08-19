package yh.util.urlcon;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * 使用org.apache.commons.httpclient.HttpClient和GetMethod访问URL获取信息
 * 
 * @author yh 默认连接超时为10秒，默认user-agent为firefox/10.0
 * 
 * 时间：2012-3-12-13:49
 * 
 * 需要 外部jar
 * /LittleUtil/lib/commons-httpclient-3.0.jar
 * /LittleUtil/lib/commons-codec-1.3.jar
 * /LittleUtil/lib/commons-logging.jar
 */
public class HttpClientCon {

	// 连接客户端
	static HttpClient httpClient = new HttpClient();

	static GetMethod method = null;

	static int connTimeout = 10000;

	static InputStream stream = null;
	
	static String defaultUA = "Mozilla/5.0 (Windows NT 5.2; rv:10.0) Gecko/20100101 Firefox/10.0";

	// 静态初始化
	static {
		httpClient.getParams().setParameter("http.connection.timeout",
				connTimeout);// 连接超时时间设置

		// 似乎用处不大
		httpClient.getParams().setParameter("http.socket.timeout", connTimeout);
		// 连接超时就释放连接，无用
		httpClient.getHttpConnectionManager().closeIdleConnections(4000);
	}

	public static int getConnTimeout() {
		return connTimeout;
	}

	public static void setConnTimeout(int connTimeout) {
		HttpClientCon.connTimeout = connTimeout;

		httpClient.getParams().setParameter("http.connection.timeout",
				connTimeout);// 连接超时时间设置

		// 似乎用处不大
		httpClient.getParams().setParameter("http.socket.timeout", connTimeout);
		// 连接超时就释放连接，无用
		httpClient.getHttpConnectionManager().closeIdleConnections(4000);
	}

	/**
	 * 获取URL文本 不保持连接
	 * 
	 * @param url
	 *            url地址
	 * @param charSet
	 *            字符集编码
	 * @return 获取的内容
	 */
	public static String getContent(String url, String charSet) {
		return getContent(url, charSet, defaultUA, false);
	}
	
	/**
	 * 以特定ua和charSet获取内容
	 * @param url
	 * @param charSet
	 * @param ua
	 * @return
	 * @Date:2013-1-28
	 * @Author:yh
	 * @Description:
	 */
	public static String getContent(String url, String charSet, String ua) {
		return getContent(url, charSet, ua, false);
	}

	/**
	 * 获取URL文本，自动关闭method
	 * 
	 * @param url
	 *            url地址
	 * @param charSet
	 *            字符集编码
	 * @param keepAlive
	 *            保持连接
	 * @return 获取的内容
	 */
	public static String getContent(String url, String charSet, String ua,
			boolean keepAlive) {
		// 返回文本内容
		String body = null;

		try {
			method = new GetMethod(url);
			// 自动跳转
			method.setFollowRedirects(true);

			// Mozilla/4.0 (compatible; MSIE6.0; Windows NT 5.0)
			method
					.setRequestHeader("User-Agent",
							ua);

			if (keepAlive) {
				// 保持连接
				method.setRequestHeader("Connection", "Keep-Alive");
			} else {
				// 每次使用完都完全关闭连接
				method.setRequestHeader("Connection", "close");
			}

			int statusCode = httpClient.executeMethod(method);

			if (statusCode == HttpStatus.SC_OK) {
				InputStream stream = method.getResponseBodyAsStream();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(stream, charSet));

				StringBuffer strBuf = new StringBuffer();
				char[] buf = new char[1024];
				int len;
				while ((len = reader.read(buf)) > 0) {
					strBuf.append(buf, 0, len);
				}
				body = strBuf.toString();
				reader.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		httpClient.getState().clearCookies();

		closeMethod();

		return body;
	}

	/**
	 * 关闭method
	 */
	public static void closeMethod() {
		if (method != null) {
			// 没有把链接关闭，只是将链接返 回给connection manager。
			method.releaseConnection();
			method = null;
		}
	}

	/**
	 * 获取url流,获取流并处理完毕后勿忘记closeMethod(); 不建议使用
	 * 
	 * @param url
	 *            Url地址
	 * @return
	 */
	public static InputStream getStream(String url) {
		try {
			method = new GetMethod(url);
			// 自动跳转
			method.setFollowRedirects(true);

			// Mozilla/4.0 (compatible; MSIE6.0; Windows NT 5.0)
			method
					.setRequestHeader("User-Agent",
							"Mozilla/5.0 (Windows NT 5.2; rv:10.0) Gecko/20100101 Firefox/10.0");

			method.setRequestHeader("Connection", "close");

			int statusCode = httpClient.executeMethod(method);

			if (statusCode == HttpStatus.SC_OK) {
				stream = method.getResponseBodyAsStream();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		httpClient.getState().clearCookies();

		return stream;
	}

	/**
	 * 保存url内容到文件,自动关闭Method
	 * 
	 * @param url
	 *            url地址
	 * @param fileName
	 *            文件路径
	 * @return
	 */
	public static boolean saveStream(String url, String fileName) {

		InputStream inputStream = getStream(url);

		boolean res = saveStream(inputStream, fileName);

		closeMethod();

		return res;
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
}
