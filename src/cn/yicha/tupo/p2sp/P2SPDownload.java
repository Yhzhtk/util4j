package cn.yicha.tupo.p2sp;  

import java.util.List;

import cn.yicha.tupo.http.HttpClientUtil;
import cn.yicha.tupo.http.RandomDownJT;
import cn.yicha.tupo.p2sp.distribute.Distribute;
import cn.yicha.tupo.p2sp.distribute.bisect.BisectDistribute;
import cn.yicha.tupo.p2sp.entity.UriFactory;
import cn.yicha.tupo.p2sp.entity.UriInfo;

public class P2SPDownload {
	
	private Distribute distribute;
	private Thread[] threads;
	private String fileName;
	
	private int rCount;
	
	public P2SPDownload(List<String> urls, String fileName){
		
		this.fileName = fileName;
		int fileSize = HttpClientUtil.getFileSize(urls.get(0));
		distribute = new BisectDistribute(fileSize);
		
		threads = new RandomDownJT[urls.size()];
		
		for(int i = 0; i < urls.size(); i++){
			UriInfo uri = UriFactory.getUriInstance(urls.get(i));
			uri.setIndex("t" + i);
			distribute.addUri(uri);
			
			threads[i] = new RandomDownJT((BisectDistribute)distribute, uri, this);
		}
	}
	
	public void start(){
		for(Thread t : threads){
			t.start();
			rCount++;
		}
	}
	
	public void completeOne(String tIndex){
		rCount--;
	}
	
	public boolean isComplete(){
		return rCount == 0;
	}

	public Distribute getDistribute() {
		return distribute;
	}

	public Thread[] getThreads() {
		return threads;
	}

	public String getFileName() {
		return fileName;
	}

}
