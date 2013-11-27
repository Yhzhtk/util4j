package cn.yicha.tupo.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import cn.yicha.tupo.p2sp.distribute.bisect.BisectDistribute;
import cn.yicha.tupo.p2sp.entity.RangeInfo;

/**
 * http请求类
 * 
 * @author gudh
 * @date 2013-11-25
 */
public class HttpClientUtil {

	static boolean usePool = true;
	static RequestConfig config;
	static CloseableHttpClient httpClient;

	static {
		config = RequestConfig.custom()
				.setConnectTimeout(10000).setConnectionRequestTimeout(10000)
				.setSocketTimeout(10000).build();
		
		if (usePool) {
			// 使用http连接池
			PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
			connManager.setMaxTotal(Integer.MAX_VALUE);
			connManager.setDefaultMaxPerRoute(Integer.MAX_VALUE);
			SocketConfig defaultSocketConfig = SocketConfig.custom()
					.setTcpNoDelay(true).setSoKeepAlive(true)
					.setSoReuseAddress(true).build();
			connManager.setDefaultSocketConfig(defaultSocketConfig);

			httpClient = HttpClients
					.custom()
					.setConnectionManager(connManager)
					.setUserAgent(
							"Mozilla/5.0 (Windows NT 5.2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.41 Safari/537.36")
					.setDefaultRequestConfig(config).build();
		} else {
			httpClient = HttpClients.createDefault();
		}
	}

	/**
	 * 爬行URL内容
	 * 
	 * @param url
	 * @param charSet
	 * @return
	 * @date:2013-11-19
	 * @author:gudaihui
	 */
	public static String getUrlContent(String url, String charSet) {
		String content = null;
		try {
			HttpGet httpGet = new HttpGet(url);

			CloseableHttpResponse response1 = httpClient.execute(httpGet);
			System.out.println(response1.getStatusLine());
			HttpEntity entity = response1.getEntity();
			try {
				content = EntityUtils.toString(entity, charSet);
			} finally {
				response1.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	/**
	 * 下载一个文件，返回下载文件的长度，如果返回-1则表示下载出错了
	 * 
	 * @param url
	 * @param fileName
	 * @return
	 * @date:2013-11-19
	 * @author:gudaihui
	 */
	public static long downloadFile(String url, String fileName) {
		long length = 0;
		try {
			HttpGet httpGet = new HttpGet(url);

			CloseableHttpResponse response1 = httpClient.execute(httpGet);
			System.out.println(response1.getStatusLine());
			HttpEntity entity = response1.getEntity();
			try {
				InputStream is = entity.getContent();
				FileOutputStream fos = new FileOutputStream(new File(fileName));
				int inByte;
				byte[] bytes = new byte[1024];
				while ((inByte = is.read(bytes)) != -1) {
					fos.write(bytes);
					length += inByte;
				}
				is.close();
				fos.close();
				return length;
			} finally {
				response1.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 下载文件指定的位置
	 * 
	 * @param url
	 * @param mbb
	 * @param startLoc
	 * @param endLoc
	 * @return
	 * @date:2013-11-20
	 * @author:gudaihui
	 */
	public static int downloadFile(RandomDown downThread, String url,
			MappedByteBuffer mbb, long startLoc, long endLoc) {
		try {
			HttpGet httpGet = new HttpGet(url);

			// 拼接请求范围
			StringBuffer sb = new StringBuffer();
			sb.append("bytes=").append(startLoc).append("-");
			if (endLoc > startLoc) {
				sb.append(endLoc);
			}
			httpGet.setHeader("Range", sb.toString());

			CloseableHttpResponse response1 = httpClient.execute(httpGet);
			// System.out.println(response1.getStatusLine());
			if (response1.getStatusLine().getStatusCode() == 206) {
				HttpEntity entity = response1.getEntity();
				try {
					mbb.position((int) startLoc);
					// rf.seek(startLoc);
					InputStream is = entity.getContent();
					int length = 0;
					int blen = 0;
					byte[] bytes = new byte[1024];
					while ((blen = is.read(bytes)) != -1
							&& !downThread.stopFlag) {
						// rf.write(bytes, 0, blen);
						mbb.put(bytes, 0, blen);
						length += blen;
					}
					return length;
				} finally {
					// 是否需要关闭
					// response1.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 以二分空闲区块下载文件
	 * 
	 * @param url
	 * @param bisect
	 * @param mbb
	 * @param startLoc
	 * @param endLoc
	 * @return
	 * @throws IOException 
	 */
	public static int downloadFile(RandomDown downThread, String url,
			BisectDistribute bisect, MappedByteBuffer mbb, RangeInfo emptyRange) throws Exception {
		int length = 0;
		try {
			HttpGet httpGet = new HttpGet(url);

			// 拼接请求范围
			StringBuffer sb = new StringBuffer();
			sb.append("bytes=").append(emptyRange.getStart()).append("-");
			if (emptyRange.getEnd() > emptyRange.getStart()) {
				sb.append(emptyRange.getEnd());
			}
			httpGet.setHeader("Range", sb.toString());
			httpGet.setConfig(config);
			
			try{
				CloseableHttpResponse response1 = httpClient.execute(httpGet);
				// System.out.println(response1.getStatusLine());
				if (response1.getStatusLine().getStatusCode() == 206) {
					HttpEntity entity = response1.getEntity();
					length = dealPartDown(downThread, entity, bisect, mbb, emptyRange);
				} else{
					throw new Exception("StatusCode is not 206");
				}
			} catch(Exception e1){
				httpGet.abort();
				throw e1;
			} finally{
				httpGet.releaseConnection();
			}
		} catch (Exception e) {
			throw e;
		}
		return length;
	}
	
	/**
	 * 使用BisectDistribute的读取数据操作
	 * @param downThread
	 * @param entity
	 * @param bisect
	 * @param mbb
	 * @param emptyRange
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private static int dealPartDown(RandomDown downThread, HttpEntity entity, BisectDistribute bisect, MappedByteBuffer mbb, RangeInfo emptyRange) throws Exception{
		int startLoc = emptyRange.getStart();
		mbb.position(startLoc);
		// rf.seek(startLoc);
		InputStream is = entity.getContent();
		
		int length = 0, lastLength = 0;
		long lastTime = System.currentTimeMillis();
		
		int blen = 0;
		byte[] bytes = new byte[bisect.getBaseSize()];
		
		try{
			while ((blen = is.read(bytes)) != -1
					&& !downThread.stopFlag) {
				// rf.write(bytes, 0, blen);
				mbb.put(bytes, 0, blen);
				length += blen;
				// 更新下载信息，已填充和空白更新
				bisect.setBytesOk(emptyRange, blen);
				
				if (emptyRange.getStart() >= emptyRange.getEnd() || bisect.hasBytesDown(emptyRange.getStart())) {
					System.out.println(Thread.currentThread().getName() + " Break --- " + emptyRange.getStart());
					break;
				}
				// 计算网速
				long t = System.currentTimeMillis() - lastTime;
				if (t >= 1000) {
					downThread.lastSpeed =  (length - lastLength) * 1000 / (int)t;
					
					System.out.println(Thread.currentThread().getName()
							+ " " + downThread.lastSpeed + "B/s "
							+ startLoc + "-" + emptyRange.getStart());
					
					lastLength = length;
					lastTime = System.currentTimeMillis();
					downThread.lastTime = lastTime;
				}
			}
		}catch(Exception e){
			throw e;
		}
		finally{
			is.close();
		}
		return length;
	}

	/**
	 * 获取文件大小
	 * 
	 * @param url
	 * @return
	 * @date:2013-11-19
	 * @author:gudaihui
	 */
	public static int getFileSize(String url) {
		int len = 0;

		HttpGet httpGet = new HttpGet(url);

		CloseableHttpResponse response1;
		try {
			response1 = httpClient.execute(httpGet);
			System.out.println(response1.getStatusLine());
			if (response1.getStatusLine().getStatusCode() == 200) {
				Header lenHeader = response1.getFirstHeader("Content-Length");
				if (lenHeader != null) {
					len = Integer.parseInt(lenHeader.getValue());
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return len;
	}
}
