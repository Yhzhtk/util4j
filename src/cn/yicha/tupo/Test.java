package cn.yicha.tupo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.yicha.tupo.p2sp.P2SPDownload;

public class Test {

	public static void main(String[] args) throws IOException {
		
		List<String> urls = new ArrayList<String>();
		urls.add("http://mirror.astpage.ru/centos/6.4/updates/i386/repodata/edc53fcf6f3468f6cdd0af4dd68de92573e790724525c668c27a9d69858a0b7f-primary.xml.gz");
		urls.add("http://mirror.vilkam.ru/centos/6.4/updates/i386/repodata/edc53fcf6f3468f6cdd0af4dd68de92573e790724525c668c27a9d69858a0b7f-primary.xml.gz");
		urls.add("http://mirror.fairway.ne.jp/centos/6.4/updates/i386/repodata/edc53fcf6f3468f6cdd0af4dd68de92573e790724525c668c27a9d69858a0b7f-primary.xml.gz");
		
		P2SPDownload p2sp = new P2SPDownload(urls , "F:/mx.xml.rar");
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
}
