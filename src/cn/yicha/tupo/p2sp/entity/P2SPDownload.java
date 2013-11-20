package cn.yicha.tupo.p2sp.entity;  

import java.util.List;

import cn.yicha.tupo.http.HttpClientUtil;
import cn.yicha.tupo.http.RandomDownT;
import cn.yicha.tupo.p2sp.distribute.block.BlockDistribute;

public class P2SPDownload {
	
	private BlockDistribute distribute;
	private Thread[] threads;
	private String fileName;
	
	private int rCount;
	
	public P2SPDownload(List<String> urls, String fileName){
		
		this.fileName = fileName;
		long fileSize = HttpClientUtil.getFileSize(urls.get(0));
		distribute = new BlockDistribute(fileSize);
		
		threads = new RandomDownT[urls.size()];
		
		for(int i = 0; i < urls.size(); i++){
			UriInfo uri = UriFactory.getUriInstance(urls.get(i));
			uri.setIndex("t" + i);
			distribute.addUri(uri);
			
			threads[i] = new RandomDownT(distribute, uri, this);
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

	public BlockDistribute getDistribute() {
		return distribute;
	}

	public Thread[] getThreads() {
		return threads;
	}

	public String getFileName() {
		return fileName;
	}

}
