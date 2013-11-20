package cn.yicha.tupo;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;

import cn.yicha.tupo.http.HttpClientUtil;
import cn.yicha.tupo.http.file.FileFactory;
import cn.yicha.tupo.p2sp.entity.P2SPDownload;
import cn.yicha.tupo.p2sp.entity.UriInfo;

public class Test {

	public static void main(String[] args) throws IOException {
//		UriInfo uri = UriFactory.getUriInstance("http://localhost/v/t.jar");
//		randomDown(uri, "F:/m.jar");
//		HttpClientUtil.downloadFile("http://localhost/v/t.rar", "F:/z.rar");
		
		List<String> urls = new ArrayList<String>();
		urls.add("http://localhost/v/t.rar");
		urls.add("http://localhost/v/t.rar");
		//urls.add("http://localhost/v/t.rar");
		//urls.add("http://localhost/v/t.rar");
		P2SPDownload p2sp = new P2SPDownload(urls , "F:/m.rar");
		p2sp.start();
		
		long start = System.currentTimeMillis();
		while(true){
			if(p2sp.isComplete()){
				break;
			}
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("总线程数：" + urls.size() + "  总耗时：" + (end - start));
		
	}
	
	public static void randomDown(UriInfo uri, String fileName) throws IOException {
		// 文件长度
		String url = uri.getUri();
		
		long len = HttpClientUtil.getFileSize(url);
		System.out.println(len);
		// 工厂获取写句柄
		MappedByteBuffer mbb = FileFactory.getMappedByteBuffer(fileName, len);
		
		int start = 0;
		int end = 0;
		while (len > 0) {
			end = start + 2000;
			long s = System.currentTimeMillis();
			
			len = HttpClientUtil.downloadFile(url, mbb, start, end);
			long e = System.currentTimeMillis();
			System.out.println(len + " " + (e - s));
			start = end;
		};
		
		// 完事释放
		FileFactory.releaseMappedByteBuffer(mbb);
	}
}
