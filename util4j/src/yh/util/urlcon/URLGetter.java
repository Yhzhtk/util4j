package yh.util.urlcon;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.HttpClient;

/**
 * con连接参考
 * @author yh
 * 2012-4-6 11:27
 */
public class URLGetter {
	static private int RETRYCOUNT = 2;
	static private int TIMEOUT = 50000;
	String firstPage = "http://shouji.game.com.cn";

	public static void main(String args[]) throws Exception {
		URLGetter get = new URLGetter();
		get.downBinaryFile(
				"http://tu3.5755.com/download/pic/2009/5/12/e9e710126c37faf18d3f6fe763da71d1.gif","F:/1.gif");
	}

	public String getSourceCode(String _url, String ucs) throws Exception {
		int count = 0;
		while (true) {
			count++;
			try {
				URL url = new URL(_url);
				System.out.println(_url);
				URLConnection conn = url.openConnection();
				conn.setConnectTimeout(TIMEOUT);
				conn.setReadTimeout(TIMEOUT);
				conn.setRequestProperty("Referer", _url);
				conn.setRequestProperty("User-Agent",
						"Opera/9.64 (Windows NT 5.2; U; zh-cn) Presto/2.1.1");
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(conn.getInputStream(), ucs));
				StringBuilder sb = new StringBuilder();
				String str = reader.readLine();
				while (str != null) {
					sb.append(str);
					str = reader.readLine();
				}
				reader.close();
				return sb.toString();
			} catch (Exception e) {
				if (count < RETRYCOUNT) {
					Thread.sleep(2000);
				} else {
					return "";
				}
			}
		}
	}

	private String GetHeaders(String _url, String headerName) throws Exception {
		String headerValue;
		int count = 0;
		while (true) {
			count++;
			try {
				URL url = new URL(_url);
				URLConnection conn = url.openConnection();
				conn.setConnectTimeout(TIMEOUT);
				conn.setReadTimeout(TIMEOUT);
				headerValue = conn.getHeaderField(headerName);
				return headerValue;
			} catch (Exception e) {
				if (count < RETRYCOUNT) {
					Thread.sleep(2000);
				} else {
					throw e;
				}
			}
		}
	}

	public String getSourceCodeForTiger(String _url, String ucs)
			throws Exception {
		String cookie = GetHeaders(_url, "Set-Cookie");
		String[] cokieValue = cookie.split(";");
		int count = 0;
		while (true) {
			count++;
			try {
				URL url = new URL(_url);
				URLConnection conn = url.openConnection();
				conn.setConnectTimeout(TIMEOUT);
				conn.setReadTimeout(TIMEOUT);
				conn.setRequestProperty("User-Agent",
						"Opera/9.64 (Windows NT 5.2; U; zh-cn) Presto/2.1.1");
				conn.setRequestProperty("Host", "tigersw.cn");
				conn
						.addRequestProperty(
								"Accept",
								"text/html, application/xml;q=0.9, application/xhtml+xml, image/png, image/jpeg, image/gif, image/x-xbitmap, */*;q=0.1");
				conn.setRequestProperty("Accept-Language",
						"zh-CN,zh;q=0.9,en;q=0.8");
				conn.setRequestProperty("Accept-Charset",
						"iso-8859-1, utf-8, utf-16, *;q=0.1");
				conn.setRequestProperty("Accept-Encoding",
						"deflate, gzip, x-gzip, identity, *;q=0");
				conn.setRequestProperty("Cookie", cokieValue[0]);
				conn.setRequestProperty("Cookie2", "$Version=1");
				conn.setRequestProperty("Cache-Control", "no-cache");
				conn.setRequestProperty("Connection", "Keep-Alive, TE");
				conn.setRequestProperty("TE",
						"deflate, gzip, chunked, identity, trailers");
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(conn.getInputStream(), ucs));
				StringBuilder sb = new StringBuilder();
				String str = reader.readLine();
				while (str != null) {
					sb.append(str);
					str = reader.readLine();
				}
				reader.close();
				return sb.toString();
			} catch (Exception e) {
				if (count < RETRYCOUNT) {
					Thread.sleep(2000);
				} else {
					throw e;
				}
			}
		}
	}

	

	public int downSource(String urlFilePath, String sourceStorePath,
			String _ucs) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(urlFilePath));
		List<String> urlList = new LinkedList<String>();
		String url = reader.readLine();
		while (url != null) {
			urlList.add(url);
			url = reader.readLine();
		}
		reader.close();
		File file = new File(sourceStorePath);
		if (!file.exists() || !file.isDirectory())
			file.mkdirs();
		int count = 0;
		for (int i = 0; i < urlList.size(); i++) {
			try {
				url = urlList.get(i);
				String source = getSourceCode(url, _ucs);
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						sourceStorePath + "/" + (i + 1) + ".src"));
				writer.write(url);
				writer.newLine();
				writer.write(source);
				writer.newLine();
				writer.close();
				count++;
			} catch (Exception e) {
				System.out.println("DownSource FAILED: " + url);
				e.printStackTrace();
			}
		}
		return count;
	}

	public String getSourceCode(String _url) throws Exception {
		int count = 0;
		while (true) {
			count++;
			try {
				URL url = new URL(_url);
				URLConnection conn = url.openConnection();
				conn.setConnectTimeout(TIMEOUT);
				conn.setReadTimeout(TIMEOUT);
				conn.setRequestProperty("Referer", _url);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(conn.getInputStream(), "UTF-8"));
				StringBuilder sb = new StringBuilder();
				String str = reader.readLine();
				while (str != null) {
					sb.append(str);
					str = reader.readLine();
				}
				reader.close();
				return sb.toString();
			} catch (Exception e) {
				if (count < RETRYCOUNT) {
					Thread.sleep(2000);
				} else {
					throw e;
				}
			}
		}
	}

	public long downBinaryFile(String _url, String filePath) throws Exception {
		int count = 0;
		while (true) {
			count++;
			try {
				URL url = new URL(_url);
				System.out.println(url);
				URLConnection conn = url.openConnection();
				conn.setConnectTimeout(TIMEOUT);
				conn.setReadTimeout(TIMEOUT);
				conn.setRequestProperty("Referer", _url);
				File file = new File(filePath).getParentFile();
				if (!file.exists() || file.isFile()) {
					file.mkdirs();
				}
				System.out.println("下载文件的大小：" + conn.getContentLength());
				if (conn.getContentLength() < 10000
						|| conn.getContentLength() > 20000000) {

					return 0l;
				}
				BufferedInputStream in = new BufferedInputStream(conn
						.getInputStream());
				BufferedOutputStream out = new BufferedOutputStream(
						new FileOutputStream(filePath));
				byte[] buff = new byte[8 * 1024];
				int length = in.read(buff);
				long filesize = 0;
				while (length > 0) {
					out.write(buff, 0, length);
					filesize += length;
					length = in.read(buff);
				}
				in.close();
				out.close();
				return filesize;
			} catch (Exception e) {
				if (count < RETRYCOUNT) {
					Thread.sleep(2000);
				} else {
					throw e;
				}
			}
		}
	}


	public String getExt(String _url) throws Exception {
		try {
			URL url = new URL(_url);
			URLConnection conn = url.openConnection();
			String ext = conn.getHeaderField("Content-Disposition");
			ext = ext.substring(ext.lastIndexOf(".") + 1);
			return ext;
		} catch (Exception e) {
			throw e;
		}
	}

	public String getpaojiaoExt(String _url) throws Exception {
		try {
			URL url = new URL(_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.getResponseCode();
			URL url1 = conn.getURL();
			String path = url1.getPath();
			String ext = path.substring(path.lastIndexOf(".") + 1);
			return ext;
		} catch (Exception e) {
			throw e;
		}
	}

	public String getDangLeRealUrl(String url) {
		HttpClient hc = new HttpClient();
		GetMethod gm = new GetMethod(url);
		gm.setFollowRedirects(true);
		String newuri = "";
		try {
			hc.executeMethod(gm);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		gm.releaseConnection();
		int statuscode = gm.getStatusCode();
		if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY)
				|| (statuscode == HttpStatus.SC_MOVED_PERMANENTLY)
				|| (statuscode == HttpStatus.SC_SEE_OTHER)
				|| (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
			Header header = gm.getResponseHeader("location");
			if (header != null) {
				newuri = header.getValue();
//				newuri = RegexUtil.regexParseGroup("http://[^\\d]+.*", newuri,
//						0);
			}
		}
		return newuri;
	}
}
