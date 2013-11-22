package cn.yicha.tupo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.yicha.tupo.p2sp.P2SPDownload;

public class Test {

	public static void main(String[] args) throws IOException {
		
		List<String> urls = new ArrayList<String>();
		urls.add("http://mirror01.idc.hinet.net/CentOS/6.4/os/i386/Packages/bacula-storage-common-5.0.0-12.el6.i686.rpm");
		urls.add("http://mirror.tversu.ru/centos/6.4/os/i386/Packages/bacula-storage-common-5.0.0-12.el6.i686.rpm");
		
		P2SPDownload p2sp = new P2SPDownload(urls , "F:/m.iso");
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
