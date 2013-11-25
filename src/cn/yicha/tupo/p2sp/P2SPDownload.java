package cn.yicha.tupo.p2sp;

import java.util.ArrayList;
import java.util.List;

import cn.yicha.tupo.http.HttpClientUtil;
import cn.yicha.tupo.http.RandomDown;
import cn.yicha.tupo.http.file.FileFactory;
import cn.yicha.tupo.http.file.JudgeRandomDown;
import cn.yicha.tupo.p2sp.distribute.bisect.BisectDistribute;
import cn.yicha.tupo.p2sp.entity.UriFactory;
import cn.yicha.tupo.p2sp.entity.UriInfo;

/**
 * P2SP下载，使用二分最大块分发算法
 */
public class P2SPDownload {

	private long maxAllowStopTime = 3000; // 最大允许没有下载的时间，单位毫秒
	private long minAllowSpeed = 1024; // 最小允许的下载速度，单位B/s
	
	private BisectDistribute distribute;
	private RandomDown[] threads;
	private String fileName;
	
	private int rCount;

	public P2SPDownload(List<String> urls, String fileName) {
		this.fileName = fileName;
		int fileSize = HttpClientUtil.getFileSize(urls.get(0));
		System.out.println("fileSize:" + fileSize);
		
		List<UriInfo> uris = new ArrayList<UriInfo>();
		for (int i = 0; i < urls.size(); i++) {
			UriInfo uri = UriFactory.getUriInstance(urls.get(i));
			uri.setIndex("U" + i);
			uris.add(uri);
		}
		distribute = new BisectDistribute(fileSize, uris);

		/**
		 * 初始化线程
		 */
		threads = new JudgeRandomDown[urls.size()];
		for (int i = 0; i < urls.size(); i++) {
			threads[i] = new JudgeRandomDown(distribute,
					uris.get(i), this);
			threads[i].setName("T" + i);
		}
	}

	/**
	 * 开始下载
	 */
	public void start() {
		for (Thread t : threads) {
			rCount++;
			t.start();
		}
	}
	
	/**
	 * 停止下载
	 */
	public void stop(){
		for (RandomDown t : threads) {
			t.stopFlag();
		}
	}
	
	/**
	 * 检查是否有资源以及不可用
	 * 
	 * 如果不可用则删除资源
	 * @return
	 */
	public synchronized boolean checkHasSlowThread(){
		boolean hasDead = false;
		for (RandomDown t : threads) {
			// 资源失效
			if(!t.isAvailable(maxAllowStopTime, minAllowSpeed)){
				t.stopFlag();
				distribute.deleteUri(t.getUriInfo());
				hasDead = true;
			}
		}
		return hasDead;
	}

	/**
	 * 提供子线程通信的接口
	 * @param tIndex
	 */
	public void completeOne(String tIndex) {
		rCount--;
	}

	/**
	 * 判断下载是否完成
	 * @return
	 */
	public boolean isComplete() {
		boolean res = false;
		res = (rCount == 0);
		if(res){
			// 结束后关闭文件
			close();
		}
		return res;
	}
	
	/**
	 * 下载结束时关闭文件等
	 */
	public void close(){
		FileFactory.closeFile(fileName);
	}

	public BisectDistribute getDistribute() {
		return distribute;
	}

	public Thread[] getThreads() {
		return threads;
	}

	public String getFileName() {
		return fileName;
	}
}
